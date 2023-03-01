# Laborator 8 (21.11.2022)
## Popescu Paullo Robertto Karloss
## Grupa 331

### Exercitiul 1
<ol type="a">
  <li>Adevarat</li>
  <li>Fals</li>
  <li>Adevarat</li>
  <li>Adevarat</li>
  <li>Fals</li>
  <li>Fals</li>
  <li>Fals</li>
</ol>

### Exercitiul 2
```py
from hashlib import sha1

base = "asgkajfkde"
rand_hashes = {sha1((base + str(i)).encode()). hexisdigest() for i in range(10)}

base = "ksjhjakfoe"
collisionFound = False

for i in range(10000000):
    if sha1((base + str(i)).encode()).hesdigest() in rand_hashes:
        print("Collision")
        collisionFound = True

if not collisionFound:
    print("No collision found!")
```

### Exercitiul 3
- Exemplul 1
    - Cheia este hard codata ```secretKey = "bettyesuper"``` si are urmatoarele consecinte:
        - codul nu trebuie sa fie public
        - daca se va modifica secretKey codul va trebui rulat din nou
    - Se foloseste criptare in loc sa se calculeze hash-urile parolelor. (daca cineva afla secretKey se pot afla afla parolele, ceva foarte rau din punct de vedere al securitatii..)
- Exemplul 2
    - Aici putem observa ca username-ul este hash-uit, aceasta actiune fiind problematica in cazul unei coliziuni (de exemplu daca user1 are acelasi hash cu user2, atunci user2 nu isi va putea crea un cont..)

- Exemplul 3
    - Nu adauga salt la hash, asadar duce la o securitate scazuta
- Exemplul 4
    - Salt-ul ar trebui sa fie generat pentru fiecare utilizator in parte
- Exemplul 5
    - Nu adauga salt la parole
    - Se foloseste encodare ```ASCII```
    - Se foloseste ```md5```, care este slab la coliziuni
