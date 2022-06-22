import copy
import struct
import socket
import logging

MAX_UINT16 = 0xFFFF
MAX_BITI_CHECKSUM = 16
MAX_SEGMENT = 100


def create_header(seq_nr, ack_nr, checksum, flags):
    b_flag = 0b00000
    if 'SYN' in flags:
        b_flag |= 0b10000
    if "SEQ" in flags:
        b_flag |= 0b01000
    if "ACK" in flags:
        b_flag |= 0b00100
    if "PSH" in flags:
        b_flag |= 0b00010
    if "FIN" in flags:
        b_flag |= 0b00001
    if b_flag == 0b00000:
        print("Error!")
        return None

    b_flag = b_flag << 11  # facem shiftare cu 11 biti deoarece mai avem nevoie de 11 biti pana la 16 biti
    octeti = struct.pack("!HHHH", seq_nr, ack_nr, checksum, b_flag)  # le impachetez folosind big endian
    logging.info('flags: '+ str(b_flag))
    return octeti


def parse_header(octeti):
    seq_nr, ack_nr, checksum, b_flag = struct.unpack("!HHHH", octeti[:8])
    b_flag >>= 11 #shiftez la dreapta cu 11 biti
    flags = []
    if b_flag & 0b10000:
        flags.append('SYN')
    if b_flag & 0b01000:
        flags.append('SEQ')
    if b_flag & 0b00100:
        flags.append('ACK')
    if b_flag & 0b00010:
        flags.append('PSH')
    if b_flag & 0b00001:
        flags.append('FIN')
    return seq_nr, ack_nr, checksum, flags, octeti[8:]


def citeste_segment(file_descriptor):
    '''
        generator, returneaza cate un segment de 92 de octeti dintr-un fisier
    '''
    yield file_descriptor.read(MAX_SEGMENT-8)


def calculeaza_checksum(octeti):
    # 1. convertim sirul octeti in numere pe 16 biti
    # 2. adunam numerele in complementul lui 1, ce depaseste 16 biti se aduna la coada
    # 3. cheksum = complementarea bitilor sumei
    aux = copy.deepcopy(octeti)
    if len(aux) % 2 == 1:  # daca are lungime impara mai adaug un bit de 0
        aux += b'0'
    checksum = 0
    for i in range(0, len(aux), 2):  # parcurg din 2 in 2 Bytes
        nr = struct.unpack("!H", aux[i:i + 2])  # iau numarul short format din 2 Bytes
        checksum += nr[0]  # il adun la suma
        while checksum > 0xFFFF:  # cat timp suma de control ocupa mai mult de 2 Bytes
            checksum = (checksum & 0xFFFF) + (checksum >> 16)  # mai readun odata ultimii 2 Bytes cu restul

    return 0xFFFF - checksum


def verifica_checksum(octeti):
    if calculeaza_checksum(octeti) == 0:
        return True
    logging.info("checksum incorect {}".format(calculeaza_checksum(octeti)))
    return False
