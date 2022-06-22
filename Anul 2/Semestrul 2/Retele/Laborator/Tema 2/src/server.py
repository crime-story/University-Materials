# receptor Reiable UDP
import random

from helper import *
from argparse import ArgumentParser
import socket
import logging

logging.basicConfig(format=u'[LINE:%(lineno)d]# %(levelname)-8s [%(asctime)s]  %(message)s', level=logging.NOTSET)


def main():
    parser = ArgumentParser(usage=__file__ + ' '
                                             '-p/--port PORT'
                                             '-f/--fisier FILE_PATH',
                            description='Reliable UDP Receptor')

    parser.add_argument('-p', '--port',
                        dest='port',
                        default='10000',
                        help='Portul pe care sa porneasca receptorul pentru a primi mesaje')

    parser.add_argument('-f', '--fisier',
                        dest='fisier',
                        help='Calea catre fisierul in care se vor scrie octetii primiti')

    # Parse arguments
    args = vars(parser.parse_args())
    port = args['port']
    fisier = args['fisier']
    fout = open(fisier, 'wb')

    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, proto=socket.IPPROTO_UDP)
    sock.settimeout(3)
    adresa = '0.0.0.0'
    server_address = (adresa, int(port))
    sock.bind(server_address)
    logging.info("Serverul a pornit pe {} si portnul portul {}".format(adresa, port))

    ok = False
    old_seq = None
    while True:
        seq_nr = random.randint(0, MAX_UINT16)  #
        logging.info('Asteptam mesaje...')
        try:
            data, address = sock.recvfrom(MAX_SEGMENT)
            if not verifica_checksum(data):
                continue
            seq_nr_client, ack_nr, _, flags, msg_received = parse_header(data)
            logging.info("Flags: " + str(flags))
            if 'SYN' in flags:

                msg_fara_check = create_header(seq_nr, seq_nr_client, 0, ['SYN', 'ACK'])
                check = calculeaza_checksum(msg_fara_check)
                msg = create_header(seq_nr, seq_nr_client, check, ['SYN', 'ACK'])

                logging.info("Send seq {} ack{}".format(seq_nr, seq_nr_client))
                sock.sendto(msg, address)

                data, address = sock.recvfrom(MAX_SEGMENT)
                if not verifica_checksum(data):
                    continue
                seq_nr_client, ack_nr, _, flags, _ = parse_header(data)

                if 'ACK' not in flags:
                    continue
                if ack_nr == seq_nr:
                    ok = True
            elif ok:  # facem doar daca 3way-handshake-ul a fost succesful
                if 'FIN' in flags:
                    break
                logging.info("primim msg")

                if 'PSH' in flags and old_seq != seq_nr_client:
                    fout.write(msg_received)
                    old_seq = seq_nr_client

                msg_fara_check = create_header(seq_nr, seq_nr_client, 0, ['ACK'])
                check = calculeaza_checksum(msg_fara_check)
                msg = create_header(seq_nr, seq_nr_client, check, ['ACK'])

                logging.info("Send seq {} ack{}".format(seq_nr, seq_nr_client))
                sock.sendto(msg, address)

        except Exception as e:
            logging.info(e)


if __name__ == '__main__':
    main()
