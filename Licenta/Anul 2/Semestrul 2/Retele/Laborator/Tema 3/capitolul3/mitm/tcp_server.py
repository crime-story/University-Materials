# TCP Server
import socket
import logging
import time

logging.basicConfig(format=u'[LINE:%(lineno)d]# %(levelname)-8s [%(asctime)s]  %(message)s', level=logging.NOTSET)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM, proto=socket.IPPROTO_TCP)
sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY,1)
sock.settimeout(5)
port = 10000
adresa = '198.10.0.2'
server_address = (adresa, port)
sock.bind(server_address)
logging.info("Serverul a pornit pe %s si portnul portul %d", adresa, port)
sock.listen(5)
logging.info('Asteptam conexiui...')
conexiune, address = sock.accept()
conexiune.settimeout(5)
logging.info("Handshake cu %s", address)
try:
    fout=open('elocal/poza_server.png', 'wb')
    while True:
        try:
            data = conexiune.recv(200)
            if len(data)==0:
                break
            print(data)
            # conexiune.send(b"Server a primit mesajul: " + data)
            fout.write(data)
            time.sleep(0.1)
        except Exception:
            break
        except KeyboardInterrupt:
            break
    fout.close()
finally:
    conexiune.close()
    sock.close()
