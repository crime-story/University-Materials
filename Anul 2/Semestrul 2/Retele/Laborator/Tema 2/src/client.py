# emitator Reliable UDP
import random
import time
import traceback
from hashlib import sha256

from helper import *
from argparse import ArgumentParser
import socket
import logging
import sys

logging.basicConfig(format=u'[LINE:%(lineno)d]# %(levelname)-8s [%(asctime)s]  %(message)s', level=logging.NOTSET)


def connect(sock, adresa_receptor):
    '''
    Functie care initializeaza conexiunea cu receptorul.
    Returneaza ack_nr de la receptor si window
    '''
    global seq_nr
    seq_nr = random.randint(0, MAX_UINT16)  #
    ack_nr = 0
    flags = ['SYN']
    checksum = 0  # mai intai voi creea headerul fara checksum, apoi calculez checksum la acel header si apoi bag checksum calculat in header

    octeti_header_fara_checksum = create_header(seq_nr, ack_nr, checksum, flags)
    checksum = calculeaza_checksum(octeti_header_fara_checksum)
    octeti_header_cu_checksum = create_header(seq_nr, ack_nr, checksum, flags)

    mesaj = octeti_header_cu_checksum

    # cat timp primesc timeout incerc sa primesc packetul
    while True:
        try:
            sock.sendto(mesaj, adresa_receptor)
            data, server = sock.recvfrom(MAX_SEGMENT)
            break
        except socket.timeout as e:
            logging.info("Timeout la connect, retrying...")

    logging.info("first contact established")
    if not verifica_checksum(data):
        # daca checksum nu e ok, mesajul de la receptor trebuie ignorat
        return -1, -1
    logging.info("connect checksum ok")
    seq_nr_server, ack_nr, checksum, flags, _ = parse_header(data)

    if 'ACK' not in flags or 'SYN' not in flags or ack_nr != seq_nr:
        return -1

    octeti_header_fara_checksum = create_header(0, seq_nr_server, 0, ['ACK'])
    checksum = calculeaza_checksum(octeti_header_fara_checksum)
    mesaj = create_header(0, seq_nr_server, checksum, ['ACK'])
    sock.sendto(mesaj, adresa_receptor)

    logging.info('Seq Nr: {}'.format(seq_nr))
    logging.info('Ack Nr: {}'.format(ack_nr))
    logging.info('Checksum: {}'.format(checksum))
    logging.info('Flags: {}'.format(flags))

    return ack_nr


def finalize(sock, adresa_receptor):
    '''
    Functie care trimite mesajul de finalizare
    cu seq_nr dat ca parametru.
    '''

    ack = send(sock, adresa_receptor, octeti_payload=b"", flags=["FIN"])

    return ack


def send(sock, adresa_receptor, octeti_payload, flags=['PSH']):
    '''
    Functie care trimite octeti ca payload catre receptor
    cu seq_nr dat ca parametru.
    Returneaza ack_nr si window curent primit de la server.
    '''
    global seq_nr
    seq_nr = int(sha256(str(time.time() + seq_nr).encode('UTF-8')).hexdigest(), 16) % MAX_UINT16
    hdr = create_header(seq_nr, 0, 0, flags) + octeti_payload
    checksum = calculeaza_checksum(hdr)
    hdr = create_header(seq_nr, 0, checksum, flags) + octeti_payload
    while True:
        try:
            sock.sendto(hdr, adresa_receptor)
            logging.info("msg trimis cu seq " + str(seq_nr))

            if 'FIN' in flags:
                return seq_nr

            data, address = sock.recvfrom(MAX_SEGMENT)

            logging.info("ack primit")
            if verifica_checksum(data):
                _, ack_nr, _, flags, _ = parse_header(data)

                if "ACK" in flags:
                    if ack_nr == seq_nr:
                        break

        except Exception as e:
            logging.info(e)

    return ack_nr


def main():
    parser = ArgumentParser(usage=__file__ + ' '
                                             '-a/--adresa IP '
                                             '-p/--port PORT'
                                             '-f/--fisier FILE_PATH',
                            description='Reliable UDP Emitter')

    parser.add_argument('-a', '--adresa',
                        dest='adresa',
                        default='receptor',
                        help='Adresa IP a receptorului (IP-ul containerului, localhost sau altceva)')

    parser.add_argument('-p', '--port',
                        dest='port',
                        default='10000',
                        help='Portul pe care asculta receptorul pentru mesaje')

    parser.add_argument('-f', '--fisier',
                        dest='fisier',
                        help='Calea catre fisierul care urmeaza a fi trimis')

    # Parse arguments
    args = vars(parser.parse_args())

    ip_receptor = args['adresa']
    port_receptor = args['port']
    fisier = args['fisier']

    adresa_receptor = (ip_receptor, int(port_receptor))
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, proto=socket.IPPROTO_UDP)
    # setam timeout pe socket in cazul in care recvfrom nu primeste nimic in 3 secunde
    sock.settimeout(3)
    try:
        '''
        TODO:
        1. initializeaza conexiune cu receptor
        2. deschide fisier, citeste segmente de octeti
        3. trimite `window` segmente catre receptor,
         send trebuie sa trimită o fereastră de window segmente
         până primșete confirmarea primirii tuturor segmentelor
        4. asteapta confirmarea segmentelor, 
        in cazul pierderilor, retransmite fereastra sau doar segmentele lipsa
        5. in functie de diferenta de timp dintre trimitere si receptia confirmarii,
        ajusteaza timeout
        6. la finalul trimiterilor, notifica receptorul ca fisierul s-a incheiat
        '''
        global seq_nr

        ack_nr = connect(sock, adresa_receptor)

        file_descriptor = open(fisier, 'rb')
        for segment in citeste_segment(file_descriptor):
            ack_nr = send(sock, adresa_receptor, segment)

        finalize(sock, adresa_receptor)
        file_descriptor.close()
    except Exception as e:
        logging.exception(traceback.format_exc())
        sock.close()


if __name__ == '__main__':
    main()
