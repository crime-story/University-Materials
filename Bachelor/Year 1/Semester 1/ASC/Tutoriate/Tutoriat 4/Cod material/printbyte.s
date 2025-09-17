.data
    ch:.byte 'a'
.text
main:

    lb $a0, ch # copiem in $a0 caracterul pe care vrem sa-l afisam
    li $v0, 11 # punem in $v0 codul pentru apelul de sistem, adica 11
    syscall    # afiseaza caracterul pe ecran

    li $v0, 10
    syscall