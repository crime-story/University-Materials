# Laborator 3 (17.10.2022)

### Exercitiul 1

- A - 4
- B - 2
- C - 5
- D - 1
- E - 6
- F - 3

### Exercitiul 2

1. Confidentialitate
2. Disponibilitate
3. Integritate
4. Confidentialitate
5. Integritate

Exemple:

- Confidentialitatea: Criptarea asimetrica - ceva criptat cu cheia
  publica poate fi decriptat numai cu cheia privata.
- Integritatea: Criptare asimetrica - ceva criptat cu cheia privata este
  integru

### Exercitiul 3

1. FALS
2. ADEVARAT
3. FALS

### Exercitiul 4

1. F(x) = 2 = 1/(1/2) => trebuie 1/2 incercari – constant => este ne-neglijabila
2. F(x) = 1/2000 => trebuie 2000 incercari – constant => este ne-neglijabila
3. F(x) = 1/(n^2000) => trebuie n^2000 incercari – polynomial => este ne-neglijabila
4. F(x) = 1/(2^(n/2)) => trebuie 2^(n/2) incercari – exponential => este neglijabila
5. F(x) = F1(n) + F2(n) => neglijabil – se ia maximul dintre cele 2, care este neglijabil
6. F(X) = F1(n) + F2(n) => ne-neglijabil – avem o functie neneglijabila intre cele 2

### Exercitiul 5

Securitatea perfecta e imposibil de atins, de aceea nu e scop.
Securitatea computationala este preferata pentru ca incercam sa avem functii neglijabile.
Dezavantajul la OTP este cheia foarte lunga (la fel de lunga ca textul criptat), iar la securitatea
computationala cheia este mai scurta.

### Exercitiul 6

1. Pot exista 2^256 chei posibile
2. Ii va trebui 2^256 / 2^30 = 2^(256-30) = 2^226 secunde
3. Nu este un atac eficient intrucat este un atac lent (dureaza mult timp).
