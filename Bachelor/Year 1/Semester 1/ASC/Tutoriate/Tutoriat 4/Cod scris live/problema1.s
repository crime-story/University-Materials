.data

    str:.asciiz "sir de caractere"

.text

main:

    li $t0, 0
    lb $t1, str($t0)

    loop:

        beqz $t1, exit

        li $v0, 11
        move $a0, $t1
        syscall

        addi $t0, $t0, 1
        lb $t1, str($t0)

        j loop

exit:

    move $a0, $t0
    li $v0, 1
    syscall

    li $v0, 10
    syscall