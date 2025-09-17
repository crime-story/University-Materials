.data

    x:.float 11.4    # x este un single declarat in memorie cu valoarea implicita 11.4
    y:.float 34.5    # y -> single cu valoarea 34.5

.text
main:

    l.s $f0, x       # incarc in $f0 valoarea din x
    l.s $f2, y       # incarc in $f2 valoarea din y
                     # am ales $f2 in loc de $f1
                     # deoarece in unele medii de lucru
                     # nu putem folosi $f-urile impare
                     # nici pentru operatii cu single

    s.s $f0, y       # pun in y valoarea din f0 (adica ce era inainte in x)
    s.s $f2, x       # pun in x valoarea din f2 (adica ce era inainte in y)

    li $v0, 2        # codul pentru a afisa un single pe ecran
    mov.s $f12, $f2  # pun in $v12 valoarea din f2 (adica ce avem acum in x)
    syscall          # afisez pe ecran

    li $v0, 11       # afisez spatiu intre rezultate
    li $a0, ' '      # pun in a0 caracter pentru spatiu
                     # in unele medii (spim) putem face direct
                     # fara a mai declara in memorie spatiul
    syscall

    li $v0, 2
    mov.s $f12, $f0  # acum afisez valoarea din $f0 (adica ce avem acum in y)
    syscall

    li $v0, 10
    syscall