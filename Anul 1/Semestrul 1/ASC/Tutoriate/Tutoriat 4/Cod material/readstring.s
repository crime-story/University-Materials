.data
    str:.space 100 # sir de caractere de lungime 99
                   # lungimea este 99, nu 100
                   # deoarece ultimul caracter
                   # este '\0'
.text
main:

                   # citeste sirul de caractere
    la $a0, str    # pune in $a0 adresa de memorie la 
                   # care vreau sa retin sirul de caractere
    li $a1, 99     # dimensiunea maximă a sirului 
    li $v0, 8      # codul pentru apelul de sistem este 8
    syscall

                   # afiseaza sirul de caractere
    la $a0, str
    li $v0, 4
    syscall
                   # exit
    li $v0, 10
    syscall