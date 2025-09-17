# Laborator 5 (31.10.2022)

### Notite Tabla Laborator:
![image](https://user-images.githubusercontent.com/73616883/200144582-e4348adf-1b32-402f-9bca-e0f12ea1a8e8.png)
![image](https://user-images.githubusercontent.com/73616883/200144589-6cd29553-c19d-4087-86b3-5286ce18d0df.png)
![image](https://user-images.githubusercontent.com/73616883/200144596-7d5e8e01-f711-4339-8b45-0bec94811e3a.png)
![image](https://user-images.githubusercontent.com/73616883/200144603-55bae279-5e3d-44e1-ab9d-e1606e26e573.png)

### Exercitiul 1

```python
seed = int(input("Introduceti seed "))

# Candidate 1
try:
    while True:
        print(seed)
        seed = seed ^ seed
except KeyboardInterrupt:
    pass

# Candidate 2
try:
    while True:
        print(seed)
        seed = int(seed + seed / 2)
except KeyboardInterrupt:
    pass
```

- Pentru Candidate 1: Nu este un PRG pentru ca a^a = 0, deci secventa de numere afisata va fi mereu seed, 0, 0, 0, ... care in mod evident nu este un PRG.

- Pentru Candidate 2: Observam ca numerele tind spre infinit, iar acest lucru din punct de vedere computational, necesita o putere de calcul mai mare. Sirul nostru poate fi ghicit foarte usor (inmultim termenii nostri cu 3/2)

- Pentru Candidate 3: Nu este un PRG pentru ca este constant, sirul va avea forma seed >> 2, seed >> 2, ... care in mod evident nu este pseudo-random.

### Exercitiul 2

```python
import secrets
import hashlib
import os


# Generează o parolă de minim 10 caractere care conține cel puțin o literă mare,
# o literă mică, o cifră și un caracter special (.!$@).
def generatePassword():
    alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    numbers = "1234567890"
    characters = alphabet.lower() + alphabet + numbers + ".!$@"
    #print("characters este ", characters)
    base = ''.join([secrets.choice(l) for l in [alphabet.lower(), alphabet, numbers, ".!$@"]])
    return base + ''.join(secrets.choice(characters) for _ in range(6))


print(generatePassword())

# Generează un string URL-safe de (cel puțin) 32 caractere.
print(secrets.token_urlsafe(32))
# Poate fi folosit drept token pentru autentificare intr-o sesiune

# Generează un token hexazecimal de (cel puțin) 32 cifre hexazecimale.
print(secrets.token_hex(32))
# Poate fi folosit pentru generarea unor key random pentru criptarea mesajelor

print(secrets.compare_digest("gigel", "gigel"))

def hashPassword(password):
    salt = os.urandom(32)
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode('utf-8'),
        salt,
        100000
    )
    return salt, key

# Am folosit libraria hashlib pentru hashuirea parolei
```

### Exercitul 3

- Cod 1 Seed este static final, adica constant. Asta contrazice faptul ca seed-ul este un secret criptografic, care nu trebuie sa fie hardodat in cod sau mereu acelasi. Cod 2 Seed-ul este chiar id-ul user-ului. Asta inseamna ca oricine care stie id-ul unui user poate sa isi genereze local care este sessionID-ul userului, si sa il impersoneze.

- ID-ul in lista "CWE" este 336 (https://cwe.mitre.org/data/definitions/336.html). Problema pe care o ridica este situatia in care un PRNG foloseste mereu acelasi seed la initializare.

- Daca spatiul seedurilor este mic, atunci ajungem la o alta vulnerabilitate - CWE339 (https://cwe.mitre.org/data/definitions/339.html). Seed-urilor pot fi brut-force-uite, si PRG-ul spart.

- Spatiul seed-ului este un element central al atacului - Reduce search space: Find ways to reduce the secret space. The smaller the attacker can make the space they need to search for the secret value, the greater their chances for success. There are a great many ways in which the search space may be reduced.. Daca spatiul este suficient de mic, putem sa il brute-force-uim.

- De exemplu, avem folositea de PRG-uri care sa nu fie rezistente la atacuri criptografice, cum ar fi CWE338.

- Doar unul 2022
  https://cve.mitre.org/cgi-bin/cvekey.cgi?keyword=PRNG
