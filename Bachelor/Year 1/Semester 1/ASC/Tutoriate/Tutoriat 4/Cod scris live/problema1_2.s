.data

    n:.float 0.0            # numarul n, il vom tine si in memorie (doar in scop didactic)

.text
main:

    li $v0, 6               # citeste n
    syscall
    s.s $f0, n              # vom pastra in $f0 valoarea lui n

    li.s $f2, 0.1           # pasul cu care vom incrementa counterul
    li.s $f4, 0.0           # counterul nostru, initializat cu 0

    loop:

        c.le.s $f0, $f4     # daca counterul nostru este >= f0, atunci am terminat
                            # deci seteaza bitului de conditie valoarea 1
        bc1t exit           # daca bitul de conditie este 1, am terminat
                            # din pacate nu avem branching ca la word-uri
                            # si nici nu putem verifica daca un numar e mai mare(sau egal)
                            # cu altul, trebuie sa verificam daca sunt mai mici(sau egale)

        li $v0, 2           # afiseaza numarul curent
        mov.s $f12, $f4
        syscall

        li $v0, 11
        li $a0, ' '
        syscall

        add.s $f4, $f4, $f2 # incrementeaza counterul cu step
                            # adica $t4 = $t4 + $t2

        j loop

exit:

    li $v0, 10
    syscall