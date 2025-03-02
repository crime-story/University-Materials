.data
    sir:.asciiz "Salutare!"
.text

inverseaza:

    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $ra, 0($sp)

    subu $sp, 4
    sw $s0, 0($sp)

    subu $sp, 4
    sw $s1, 0($sp)

    subu $sp, 4
    sw $s2, 0($sp)
    
    subu $sp, 4
    sw $s3, 0($sp)

    lw $s0, 0($fp)
    lw $s1, 4($fp)

    add $s1, $s0, $s1

    subu $s1, 1

    parc_sir:

        bge $s0, $s1, exit_parc_sir

        lb $s2, 0($s0)
        lb $s3, 0($s1)

        sb $s2, 0($s1)
        sb $s3, 0($s0)

        addu $s0, 1
        subu $s1, 1

        j parc_sir

exit_parc_sir:

    lw $s3, -24($fp)
    lw $s2, -20($fp)
    lw $s1, -16($fp)
    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 24

    jr $ra

main:

    li $t0, 0
    lb $t1, sir($t0)

    lung_str:

        beqz $t1, exit_lung_str

        addu $t0, 1
        lb $t1, sir($t0)        

        j lung_str

exit_lung_str:

    subu $sp, 4
    sw $t0, 0($sp)

    la $t0, sir
    subu $sp, 4
    sw $t0, 0($sp)

    jal inverseaza

    addu $sp, 8

    la $a0, sir
    li $v0, 4
    syscall

    li $v0, 10
    syscall