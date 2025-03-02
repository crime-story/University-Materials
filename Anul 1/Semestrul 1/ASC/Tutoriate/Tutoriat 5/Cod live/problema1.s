.data
    x:.word 3
    y:.word 4
.text

suma:

    subu $sp, 4
    sw $fp, 0($sp)
    # ($fp) (x) (y)

    addi $fp, $sp, 4
    #sp: ($fp) <fp pointeaza aici> (x) (y)

    subu $sp, 4
    sw $s0, 0($sp)

    subu $sp, 4
    sw $s1, 0($sp)
    # sp: ($s1) ($s0) ($fp) <fp pointeaza aici> (x) (y)

    lw $s0, 0($fp)
    lw $s1, 4($fp)

    add $s0, $s0, $s1

    sw $s0, 0($fp)

    lw $s1, -12($fp)
    lw $s0, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 12

    jr $ra

main:

    lw $t0, y
    subu $sp, 4
    sw $t0, 0($sp)

    lw $t0, x
    subu $sp, 4
    sw $t0, 0($sp)

    # sp: (x), (y)

    jal suma
    # sp: (rezultat), (y)
    lw $a0, 0($sp)
    # $ra -> intoarce-te aici

    addu $sp, 8

    li $v0, 1
    syscall

    li $v0, 10
    syscall