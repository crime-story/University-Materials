# TCP client
import socket
import logging
import time
import random

logging.basicConfig(format=u'[LINE:%(lineno)d]# %(levelname)-8s [%(asctime)s]  %(message)s', level=logging.NOTSET)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM, proto=socket.IPPROTO_TCP)
sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY,1)

port = 10000
adresa = '198.10.0.2'
server_address = (adresa, port)

try:
    fin = open('elocal/poza_client.png', 'rb')
    logging.info('Handshake cu %s', str(server_address))
    sock.connect(server_address)
    while True:
        mesaj = fin.read(100)
        if len(mesaj)==0:
            break
        print(mesaj)
        sock.send(mesaj)
        time.sleep(0.1)
    fin.close()
except KeyboardInterrupt:
    pass
finally:
    logging.info('closing socket')
    sock.close()
