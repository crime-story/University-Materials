# Laborator 2 (10.10.2022)

### Notite Tabla Laborator:

![WhatsApp Image 2022-10-14 at 23 19 35](https://user-images.githubusercontent.com/73616883/196347549-8ed58373-ae8c-4d8c-b1ac-bb1eb81b5ad7.jpg)

### Exercitiul 1.1

```
mesaj_base64 = o9/khC3Pf3/9CyNCbdzHPy5oorccEawZSFt3mgCicRnihDSM8Obhlp3vviAVuBbiOtCSz6husBWqhfF0Q
/8EZ+6iI9KygD3hAfFgnzyv9w==

cheie_hex = ecb181a479a6121add5b42264db9b44b4b48d7d93c62c56a3c3e1aba64c7517a90ed44f8919484b6ed8a
cc4670db62c249b9f5bada4ed474c9e4d111308b614788cd4fbdc1e949c1629e12fa5fdbd9
```

- Pentru a decripta mesajul vom face urmatorii pasi:

```
Pasul 1) Decodam mesajul din base64 in binary:
    mesaj_binary = 10100011 11011111 11100100 10000100
                   00101101 11001111 01111111 01111111
                   11111101 00001011 00100011 01000010
                   01101101 11011100 11000111 00111111
                   00101110 01101000 10100010 10110111
                   00011100 00010001 10101100 00011001
                   01001000 01011011 01110111 10011010
                   00000000 10100010 01110001 00011001
                   11100010 10000100 00110100 10001100
                   11110000 11100110 11100001 10010110
                   10011101 11101111 10111110 00100000
                   00010101 10111000 00010110 11100010
                   00111010 11010000 10010010 11001111
                   10101000 01101110 10110000 00010101
                   10101010 10000101 11110001 01110100
                   01000011 11111111 00000100 01100111
                   11101110 10100010 00100011 11010010
                   10110010 10000000 00111101 11100001
                   00000001 11110001 01100000 10011111
                   00111100 10101111 11110111

Pasul 2) Decriptam cheia din Hexa in binary:
    cheie_binary = 11101100 10110001 10000001 10100100
                   01111001 10100110 00010010 00011010
                   11011101 01011011 01000010 00100110
                   01001101 10111001 10110100 01001011
                   01001011 01001000 11010111 11011001
                   00111100 01100010 11000101 01101010
                   00111100 00111110 00011010 10111010
                   01100100 11000111 01010001 01111010
                   10010000 11101101 01000100 11111000
                   10010001 10010100 10000100 10110110
                   11101101 10001010 11001100 01000110
                   01110000 11011011 01100010 11000010
                   01001001 10111001 11110101 10111010
                   11011010 01001110 11010100 01110100
                   11001001 11100100 11010001 00010001
                   00110000 10001011 01100001 01000111
                   10001000 11001101 01001111 10111101
                   11000001 11101001 01001001 11000001
                   01100010 10011110 00010010 11111010
                   01011111 11011011 11011001

Pasul 3) Aplicam XOR intre cele 2 valori binary (mesaj si cheie):
    rezultat XOR = 01001111 01101110 01100101 00100000
                   01010100 01101001 01101101 01100101
                   00100000 01010000 01100001 01100100
                   00100000 01100101 01110011 01110100
                   01100101 00100000 01110101 01101110
                   00100000 01110011 01101001 01110011
                   01110100 01100101 01101101 00100000
                   01100100 01100101 00100000 01100011
                   01110010 01101001 01110000 01110100
                   01100001 01110010 01100101 00100000
                   01110000 01100101 01110010 01100110
                   01100101 01100011 01110100 00100000
                   01110011 01101001 01100111 01110101
                   01110010 00100000 01100100 01100001
                   01100011 01100001 00100000 01100101
                   01110011 01110100 01100101 00100000
                   01100110 01101111 01101100 01101111
                   01110011 01101001 01110100 00100000
                   01100011 01101111 01110010 01100101
                   01100011 01110100 00101110

Pasul 4) Convertim rezultatul de la pasul 3 in ASCII:
    rezultat in ASCII = One Time Pad este un sistem de criptare perfect sigur daca este folosit corect.
```

### Exercitiul 1.2

- Pentru a gasii o alta cheie care sa decripteze mesajul pct. 1 vom parcurge urmatorii pasi:

```
text = 'Orice text clar poate obtinut dintr-un text criptat cu OTP dar cu alta cheie..'

Pasul 1) Convertim textul in binary:
rezultat_binary = 01001111 01110010 01101001 01100011
                  01100101 00100000 01110100 01100101
                  01111000 01110100 00100000 01100011
                  01101100 01100001 01110010 00100000
                  01110000 01101111 01100001 01110100
                  01100101 00100000 01101111 01100010
                  01110100 01101001 01101110 01110101
                  01110100 00100000 01100100 01101001
                  01101110 01110100 01110010 00101101
                  01110101 01101110 00100000 01110100
                  01100101 01111000 01110100 00100000
                  01100011 01110010 01101001 01110000
                  01110100 01100001 01110100 00100000
                  01100011 01110101 00100000 01001111
                  01010100 01010000 00100000 01100100
                  01100001 01110010 00100000 01100011
                  01110101 00100000 01100001 01101100
                  01110100 01100001 00100000 01100011
                  01101000 01100101 01101001 01100101
                  00101110 00101110 00000000

Pasul 2) Facem XOR intre rezultatul de la pasul 1 si mesajul in binary de la pasul 1 de la exercitiul 1:
    rezultat XOR = 10100011 11011111 11100100 10000100
                   00101101 11001111 01111111 01111111
                   11111101 00001011 00100011 01000010
                   01101101 11011100 11000111 00111111
                   00101110 01101000 10100010 10110111
                   00011100 00010001 10101100 00011001
                   01001000 01011011 01110111 10011010
                   00000000 10100010 01110001 00011001
                   11100010 10000100 00110100 10001100
                   11110000 11100110 11100001 10010110
                   10011101 11101111 10111110 00100000
                   00010101 10111000 00010110 11100010
                   00111010 11010000 10010010 11001111
                   10101000 01101110 10110000 00010101
                   10101010 10000101 11110001 01110100
                   01000011 11111111 00000100 01100111
                   11101110 10100010 00100011 11010010
                   10110010 10000000 00111101 11100001
                   00000001 11110001 01100000 10011111
                   00111100 10101111 11110111

Pasul 3) Convertim in hexa rezultatul XOR, si am gasit cheia:
    cheie = A3DFE4842DCF7F7FFD0B23426DDCC73F2E68A2B71C11AC19485B779A00A27119E284348CF0E6E1969DEFBE2015B816E23AD092CFA86EB015AA85F17443FF0467EEA223D2B2803DE101F1609F3CAFF7
```

### Exercitiul 1.3

Daca refolosim cheia de mai multe ori vor aparea problemme de securitate.

Exemplu: Daca am trimis 2 mesaje m1 si m2, criptate cu aceeasi cheie k, cineva poate intercepta m1^k si m2^k, si daca face operatia XOR intre ele poate afla m1^m2.

### Exercitiul 2

#### a) Substitutie

Cel mai clasic sistem istoric de criptare cu substitutie este cifrul lui Caesar. Fiecare litera este inlocuita cu o litera `X` pozitii mai departe in alfabet. Sistemul nu este foarte securizat, avand in vedere ca exista numai 26 de posibilitati.

Exemplu, putem sa ne uitam la cifrul Caesar cu rotatie de `1`. `Hello Robertto!` => `Ifmmp Spcfsuup!`

Pentru a decripta mesajul, este suficient sa facem transformarea inversa, scazand `1` din fiecare caracter (eventual si adaugand 26 daca trece de `a`).

Pentru a sparge sistemul, este suficient sa verificam cele `26` de posibilitati.

#### b) Transpozitie

Cel mai simplu algoritm de criptare prin transpozitie este transpozitia prin coloane, in care scriem un cuvant pe linii si il citim pe coloane.

exemplu, daca avem cuvantul `Hello there Robertto`, si encodam pe 5 coloane, vom avea:

| H   | e   | l   | l   | o   |
| --- | --- | --- | --- | --- |
|     | t   | h   | e   | r   |
| --- | --- | --- | --- | --- |
| e   |     | R   | o   | b   |
| --- | --- | --- | --- | --- |
| e   | r   | t   | t   | o   |

Care se traduce in `H eeet rlhRtleotorbo`.

Pentru a il decripta, este suficient sa calculam cate linii sunt necesare (parte intreaga superioara de lungime pe numar de coloane), sa il scrim pe coloane si sa il citim din nou pe linii.

Pentru a sparge algoritmul, este suficient sa ne variem cate linii sunt, sa scriem cuvantul pe coloane si sa vedem daca este cel bun.

Securitatea criptarii nu este una inalta.

### Exercitiul 3

Text criptat:

```
ENHFJ EWK LML EOJ GDJ BMONKC PMCG YEPMAC FOVQGMROEQDHF FMAQNJ. CHWFJ
GDJHO HWUJWGHMW HW 1978, GDJV DEUJ EG MWFJ LJJW FENNJK HWCJQEOELNJ, EWK
DEUJ LJJW GDJ CALXJFG MY WAPJOMAC KHUMOFJC, GOEUJNC, EWK GMOPJWGC. HW GDJ
JWCAHWR VJEOC, MGDJO FDEOEFGJOC DEUJ XMHWJK GDJHO FOVQGMROEQDHF
YEPHNV. GDJOJC JUJ, GDJ QECCHUJ EWK CALPHCCHUJ JEUJCKOMQQJO, PENNMOV GDJ
PENHFHMAC EGGEFTJO, EWK GOJWG, GOACGJK LV ENN, XACG GM WEPJ E YJB. BDHNJ
ENHFJ, LML, EWK GDJHO JSGJWKJK YEPHNV BJOJ MOHRHWENNV ACJK GM JSQNEHW
DMB QALNHF TJV FOVQGMROEQDV BMOTC, GDJV DEUJ CHWFJ LJFMPJ BHKJNV ACJK
EFOMCC MGDJO CFHJWFJ EWK JWRHWJJOHWR KMPEHWC. GDJHO HWYNAJWFJ
FMWGHWAJC GM ROMB MAGCHKJ MY EFEKJPHE EC BJNN: ENHFJ EWK LML EOJ WMB E
QEOG MY RJJT NMOJ, EWK CALXJFG GM WEOOEGHUJC EWK UHCAEN KJQHFGHMWC
GDEG FMPLHWJ QJKERMRV BHGD HW-XMTJC, MYGJW OJYNJFGHWR MY GDJ CJSHCG EWK
DJGJOMWMOPEGHUJ JWUHOMWPJWGC HW BDHFD GDJV BJOJ LMOW EWK FMWGHWAJ
GM LJ ACJK. PMOJ GDEW XACG GDJ BMONKC PMCG YEPMAC FOVQGMROEQDHF FMAQNJ,
ENHFJ EWK LML DEUJ LJFMPJ EW EOFDJGVQJ MY KHRHGEN JSFDEWRJ, EWK E NJWC
GDOMARD BDHFD GM UHJB LOMEKJO KHRHGEN FANGAOJ. I.KAQMWG EWK E.FEGGEQEW
FOVQGMFMAQNJ
```

Grafic most common letters in english [(here)](https://www3.nd.edu/~busiforc/handouts/cryptography/340px-English-slf.png).

Cel mai frecvent apar:

- J: 122 ori - inlocuim cu E
- E de 79 ori - inlocuim cu A
- G: 71 ori - inlocuim cu T
- M: 66 ori - inlocuim cu O
- W: 66 ori - inlocuim cu N
- H: 62 ori - inlocuim cu I

Textul decriptat:

```
ALICE AND BOB ARE THE WORLDS MOST FAMOUS CRYPTOGRAPHIC COUPLE. SINCE THEIR INVENTION IN 1978, THEY HAVE AT ONCE BEEN CALLED INSEPARABLE, AND HAVE BEEN THE SUBJECT OF NUMEROUS DIVORCES, TRAVELS, AND TORMENTS. IN THE ENSUING YEARS, OTHER CHARACTERS HAVE JOINED THEIR CRYPTOGRAPHIC FAMILY. THERES EVE, THE PASSIVE AND SUBMISSIVE EAVESDROPPER, MALLORY THE MALICIOUS ATTACKER, AND TRENT, TRUSTED BY ALL, JUST TO NAME A FEW. WHILE ALICE, BOB, AND THEIR EXTENDED FAMILY WERE ORIGINALLY USED TO EXPLAIN HOW PUBLIC KEY CRYPTOGRAPHY WORKS, THEY HAVE SINCE BECOME WIDELY USED ACROSS OTHER SCIENCE AND ENGINEERING DOMAINS. THEIR INFLUENCE CONTINUES TO GROW OUTSIDE OF ACADEMIA AS WELL: ALICE AND BOB ARE NOW A PART OF GEEK LORE, AND SUBJECT TO NARRATIVES AND VISUAL DEPICTIONS THAT COMBINE PEDAGOGY WITH IN-JOKES, OFTEN REFLECTING OF THE SEXIST AND HETERONORMATIVE ENVIRONMENTS IN WHICH THEY WERE BORN AND CONTINUE TO BE USED. MORE THAN JUST THE WORLDS MOST FAMOUS CRYPTOGRAPHIC COUPLE, ALICE AND BOB HAVE BECOME AN ARCHETYPE OF DIGITAL EXCHANGE, AND A LENS THROUGH WHICH TO VIEW BROADER DIGITAL CULTURE. Q.DUPONT AND A.CATTAPAN CRYPTOCOUPLE
```

### Exercitiul 4

Luam ziua 20 de [(aici)](https://operationturing.tumblr.com/image/143702293214).

```
I IV III | 03 24 26 | AO BU CJ DE GQ HP KW MX NV ST | KPF
```

```
numele meu = ROBERTO
```

Introducem datele in [masina](https://www.101computing.net/enigma-machine-emulator/)

![image](https://user-images.githubusercontent.com/73616883/196350739-ddd277c8-5fed-445f-93d6-3b0f061b9191.png)
![image](https://user-images.githubusercontent.com/73616883/196350888-14994398-e7ec-48b2-ad15-79c80afd3db7.png)

Pentru a decripta introducem in masina valorile default.

Observatie! La criptare/decriptare spre exemplu litera T nu poate coresupunde cu T! Mereu difera!
