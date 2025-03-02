.data
    v:.space 400
.text

suma_cifrelor_para:

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

    lw $s0, 0($fp)
    li $s1, 0

    parc_cifre:

        ble $s0, 0, exit_parc_cifre

        rem $s2, $s0, 10
        add $s1, $s1, $s2

        div $s0, $s0, 10

        j parc_cifre

exit_parc_cifre:

    rem $s1, $s1, 2

    seq $v0, $s1, 0
    
    lw $s2, -20($fp)
    lw $s1, -16($fp)
    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 20

    jr $ra

evalueaza:

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

    subu $sp, 4
    sw $s4, 0($sp)

    subu $sp, 4
    sw $s5, 0($sp)

    lw $s0, 4($fp) # s0 e n
    li $s1, 0 # coutner
    # s2 uz general
    # s3 uz general
    # s4 suma
    li $s4, 0

    iter_vec:

        beq $s0, $s1, exit_iter_vec

        # s2 pointer vector
        move $s2, $s1
        add $s2, $s2, $s2
        add $s2, $s2, $s2

        lw $s3, 0($fp)
        add $s2, $s2, $s3

        lw $s2, 0($s2)

        subu $sp, 4
        sw $s2, 0($sp)

        jal suma_cifrelor_para

        addu $sp, 4

        #move $a0, $v0
        #li $v0, 1
        #syscall

        #li $a0, ' '
        #li $v0, 11
        #syscall

        beq $v0, 0, eval_form
        j cont_loop

        eval_form:

            lw $s3, 8($fp)
            rem $s2, $s2, $s3

            add $s4, $s4, $s2

            lw $s5, 12($fp)
            lw $s2, 16($fp)
            div $s2, $s2, 3
            sub $s5, $s5, $s2

            add $s5, $s5, $s1
            move $s2, $s5

            mul $s5, $s5, $s5
            mul $s5, $s5, $s2

            add $s4, $s4, $s5

        cont_loop:

            addu $s1, 1

            j iter_vec

exit_iter_vec:

    sw $s4, 0($fp)

    lw $s5, -32($fp)
    lw $s4, -28($fp)
    lw $s3, -24($fp)
    lw $s2, -20($fp)
    lw $s1, -16($fp)
    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 32

    jr $ra

main:

    li $v0, 5
    syscall
    move $t0, $v0

    li $t1, 0
    li $t2, 0

    read_vec:

        beq $t1, $t0, exit_read

        li $v0, 5
        syscall
        sw $v0, v($t2)

        addu $t1, 1
        addu $t2, 4

        j read_vec

exit_read:

    li $v0, 5
    syscall
    move $t1, $v0

    li $v0, 5
    syscall
    move $t2, $v0    

    li $v0, 5
    syscall
    move $t3, $v0

    # z pe stiva
    subu $sp, 4
    sw $t3, 0($sp)

    # y pe stiva
    subu $sp, 4
    sw $t2, 0($sp)

    # x pe stiva
    subu $sp, 4
    sw $t1, 0($sp)

    # n pe stiva
    subu $sp, 4
    sw $t0, 0($sp)

    # v pe stiva
    la $t0, v
    subu $sp, 4
    sw $t0, 0($sp)

    jal evalueaza

    lw $t0, 0($sp)
    addu $sp, 20

    move $a0, $t0
    li $v0, 1
    syscall

    li $v0, 10
    syscall