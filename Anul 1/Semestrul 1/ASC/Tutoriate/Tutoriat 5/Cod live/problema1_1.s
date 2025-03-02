.data
    x:.word 7
.text

g:
    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $ra, 0($sp)
    # sp: ($ra) ($fp) <fp pointeaza aici> (x)

    lw $v0, 0($fp)
    addu $v0, 1

    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 8

    jr $ra

f:

    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $ra, 0($sp)

    subu $sp, 4
    sw $s0, 0($sp)

    # (s0) (ra) (fp) <fp pointeaza aici> (x)

    lw $s0, 0($fp)

    subu $sp, 4
    sw $s0, 0($sp)

    jal g
    add $v0, $v0, $v0

    addu $sp, 4

    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 12

    jr $ra

main:

    lw $t0, x
    subu $sp, 4
    sw $t0, 0($sp)

    jal f

    addu $sp, 4

    move $a0, $v0
    li $v0, 1
    syscall

    li $v0, 10
    syscall