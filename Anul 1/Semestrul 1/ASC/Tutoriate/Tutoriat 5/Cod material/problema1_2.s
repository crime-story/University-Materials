.data
    n:.word 5
.text

fact:

    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4
    
    subu $sp, 4
    sw $ra, 0($sp)

    subu $sp, 4
    sw $s0, 0($sp)

    lw $s0, 0($fp)

    ble $s0, 1, cond # daca numarul pentru care a fost 
                     # apelata procedura este mai mic sau egal cu 1
                     # va returna 1

    subu $s0, 1

    subu $sp, 4      # calculeaza fact(n-1)
    sw $s0, 0($sp)

    jal fact
    addu $sp, 4

    addu $s0, 1
    mul $v0, $v0, $s0 # returneaza in v0
                      # valoarea n * fact(n-1)

    j exit            # nu returnam 1 pe cazul general
                      # doar daca avem parametru mai mic sau egal cu 1
cond:
    li $v0, 1
exit:

    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 12
    jr $ra

main:

    lw $t0, n
    subu $sp, 4
    sw $t0, 0($sp)

    jal fact

    addu $sp, 4
    
    move $a0, $v0
    li $v0, 1
    syscall

    li $v0, 10
    syscall