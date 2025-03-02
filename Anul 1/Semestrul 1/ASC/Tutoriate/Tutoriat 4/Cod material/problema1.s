.data
    str:.asciiz "Sir de caractere" # sirul de caractere
.text
main:

    li $t0, 0                      # counterul 
    lb $t1, str($t0)               # iau primul caracter din sir

    loop:

        beqz $t1, exit             # daca am ajuns la sfarsitul sirului ne oprim

        addi $t0, 1
        lb $t1, str($t0)           # ia urmatorul caracter din sir
        j loop

exit:

    move $a0, $t0
    li $v0, 1
    syscall

    li $v0, 10
    syscall