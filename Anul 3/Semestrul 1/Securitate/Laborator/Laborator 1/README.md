# Laborator 1 (03.10.2022)

### Exercitiul 1

- A - 3
- B - 1
- C - 5
- D - 2
- E - 4

### Exercitiul 3

- a)

```
Ziua mea = 17
17 + 10 = 27

27 | 1
13 | 1
 6 | 0
 3 | 1
 1 | 1

Numarul 27 in baza 2 este: 11011 (urcam de jos in sus ca sa formam numarul)
Verificare:
 - 11011 = 1 + 2^1 + 2^3 + 2^4 = 3 + 8 + 16 = 27
```

- b)

```
Alegem un numar hexadecimal oarecare: ABCD
Stim ca:
    A = 10 (baza 10) -> 1010 (baza 2)
    B = 11 (baza 10) -> 1011 (baza 2)
    C = 12 (baza 10) -> 1100 (baza 2)
    D = 13 (baza 10) -> 1101 (baza 2)

Verificare:
1010 1011 1100 1101 = 10 11 12 13 = ABCD
```

### Exercitiul 4

- a)

```
numele_meu = PAULLO ROBERTTO KARLOSS
Convertim numele_meu in ASCII:
numele_meu = 80 65 85 76 76 79 32 82 79 66 69 82 84 84 79 32 75 65 82 76 79 83 83
```

- b)

```
Convertim din ASCII in string:
66 82 65 86 79 = BRAVO
```

### Exercitiul 5

- a)

```
nume = POPESCU

Pasul 1 (convertim nume in ASCII i.e. base10):
    nume = 80 79 80 69 83 67 85

Pasul 2 (convertim fiecare cod ASCII din nume in binary/base2):
    nume = 01010000 01001111 01010000 01000101 01010011 01000011 01010101

Pasul 3 (impartim in calupuri de 6 biti, iar daca avem biti care nu formeaza octeti adaugam 0-uri la final):
    nume = 010100 000100 111101 010000 010001 010101 001101 000011 010101 010000

Observatie! Am adaugat la ultimul calup 4 biti de 0 pentru a forma un octet. :smiley:

Pasul 4 (convertim nume in base64):
    nume este UE9QRVNDVQA==
```

- b)

```
string = U3VudCBzdHVkZW50IGxhIEZNSS4=

Pasul 1 (convertim string din base64 in binary):
    string = 01010011 01110101 01101110 01110100
             00100000 01110011 01110100 01110101
             01100100 01100101 01101110 01110100
             00100000 01101100 01100001 00100000
             01000110 01001101 01001001 00101110

Pasul 2 (convertim din binary in cod ASCII):
    string = 83 117 110 116
             32 115 116 117
             100 101 110 116
             32 108 97 32
             70 77 73 46

Pasul 3 (pentru fiecare cod ASCII asociem litera corespunzatoare din tabel):
    string = Sunt student la FMI.
```

Convertor pentru verificare [aici](https://www.base64decode.org/).

### Exercitiul 6

- malware = reprezinta un software care este realizat special pentru a perturba, deteriora sau obține acces neautorizat la un sistem informatic.

- virus = reprezinta o bucata de cod care poate sa copieze singura si are de obicei un efect daunator, cum ar fi coruperea sistemului sau distrugerea datelor

- dropper = este un program realizat pentru a extrage alte fisiere din propriul cod sursa.

- downloader = reprezinta primirea datelor de la un sistem la distanta (de la un server) exemplu server web

- trojan = un tip de cod rău intenționat sau software care pare legitim, dar care poate prelua controlul asupra computerului dvs

- spyware = software care permite unui utilizator să obțină informații secrete despre activitățile computerului altuia prin transmiterea datelor în secret de pe hard diskul său.

- riskware = Riskware, un portmanteau de risc și software, este un cuvânt folosit pentru a descrie software-ul a cărui instalare și execuție prezintă un risc posibil, dar nedeterminat pentru un computer gazdă.

- ransomware = un tip de software rău intenționat conceput pentru a bloca accesul la un sistem informatic până când este plătită o sumă de bani.

- adware = software care afișează sau descarcă automat materiale publicitare, cum ar fi bannere sau ferestre pop-up atunci când un utilizator este online.

- worm = Un vierme de computer este un tip de malware care răspândește copii ale lui de la computer la computer.

- obfuscare = a face ceva greu de înțeles. Codul de programare este adesea ascuns pentru a proteja proprietatea intelectuală sau secretele comerciale și pentru a preveni un atacator să facă inginerie inversă a unui program software proprietar.
