.data
    x:.word 7
.text

g:
    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $ra, 0($sp)   # punem $ra pe stiva

    subu $sp, 4
    sw $s0, 0($sp)

    lw $s0, 0($fp)

    addi $v0, $s0, 1

    lw $s0, -12($fp)
    lw $ra, -8($fp)  # scoatem $ra de pe stiva
    lw $fp, -4($fp)

    addu $sp, 12

    jr $ra

f:
    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4      # punem $ra pe stiva
    sw $ra, 0($sp)   # teoretic, doar in procedura f
                     # trebuie sa punem si sa scoatem
                     # $ra de pe stiva, dar pentru consistenta
                     # facem asta si in procedura g

    subu $sp, 4
    sw $s0, 0($sp)

    lw $s0, 0($fp)

    subu $sp, 4
    sw $s0, 0($sp)
    
    jal g

    addu $sp, 4
    
    add $v0, $v0, $v0

    lw $s0, -12($fp)
    lw $ra, -8($fp)  # scoatem $ra de pe stiva, pentru
                     # a ne putea intoarce inapoi in main
                     # daca nu faceam asta, atunci la jr $ra
                     # ne-am fi intors inapoi dupa jal g
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