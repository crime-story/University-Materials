.data

    vf:.space 12
    v:.space 12

.text

aplica:
    subu $sp, 4
    sw $fp, 0($sp)
    addi $fp, $sp, 4
    subu $sp, 4
    sw $ra, 0($sp)
    subu $sp, 4
    sw $s0, 0($sp)
    subu $sp, 4
    sw $s1, 0($sp)

    lw $s0, 0($fp)       # adresa de memorie a procedurii
    lw $s1, 4($fp)       # argumentul pe care il vom da procedurii

    subu $sp, 4          # punem argumentul pe stiva
    sw $s1, 0($sp)
    la $ra, cont_apl     # lui $ra ii punem ca valoare adresa de memorie
                         # din program unde vor reveni procedurile f1, f2, f3
                         # dupa ce se executa
    jr $s0               # apelam procedura daca ca argument

    cont_apl:            # aici ajungem dupa ce se executa procedura

        addu $sp, 4

        lw $s1, -16($fp)
        lw $s0, -12($fp)
        lw $ra, -8($fp)
        lw $fp, -4($fp)

        addu $sp, 16

        j $ra

f1:
    subu $sp, 4
    sw $fp, 0($sp)    
    addi $fp, $sp, 4 
    subu $sp, 4
    sw $ra, 0($sp)
    subu $sp, 4
    sw $s0, 0($sp)
    lw $s0, 0($fp)
    add $v0, $s0, $s0 
    lw $s0, -12($fp)
    lw $ra, -8 ($fp)
    lw $fp, -4 ($fp)
    addu $sp, 12
    jr $ra

f2:
    subu $sp, 4
    sw $fp, 0($sp)    
    addi $fp, $sp, 4 
    subu $sp, 4
    sw $ra, 0($sp)
    subu $sp, 4
    sw $s0, 0($sp)
    lw $s0, 0($fp)
    mul $v0, $s0, $s0 
    lw $s0, -12($fp)
    lw $ra, -8 ($fp)
    lw $fp, -4 ($fp)
    addu $sp, 12
    jr $ra

f3:
    subu $sp, 4
    sw $fp, 0($sp)    
    addi $fp, $sp, 4 
    subu $sp, 4
    sw $ra, 0($sp)
    subu $sp, 4
    sw $s0, 0($sp)
    lw $s0, 0($fp)
    subu $v0, $zero, $s0
    lw $s0, -12($fp)
    lw $ra, -8 ($fp)
    lw $fp, -4 ($fp)
    addu $sp, 12
    jr $ra

main:

    la $t0, vf

    la $t1, f1
    sw $t1, 0($t0)       # punem procedura f1 in array

    la $t1, f2
    sw $t1, 4($t0)       # punem procedura f2 in array
    
    la $t1, f3
    sw $t1, 8($t0)       # punem procedura f3 in array

    li $t1, 0
    li $t2, 3

    loop:

        bge $t1, $t2, exit

        move $t3, $t1
        add $t3, $t3, $t3
        add $t3, $t3, $t3

        lw $t3, vf($t3)  # luam procedura de la pozitia i din array
        move $t4, $t1
        addi $t4, 1      # calculam valoarea argumentului curent dat procedurii

        subu $sp, 4      # punem argumentul pe stiva
        sw $t4, 0($sp)

        subu $sp, 4      # punem si adresa procedurii pe stiva
        sw $t3, 0($sp)

        jal aplica

        addu $sp, 8

        move $t3, $t1
        add $t3, $t3, $t3
        add $t3, $t3, $t3

        sw $v0, v($t3)   # punem in array-ul v valoarea rezultata

                         # am comentat afisarea deoarece
                         # cerinta nu cere sa si afisam rezultatele
                         # pe ecran
        # move $a0, $v0
        # li $v0, 1
        # syscall

        # li $a0, ' '
        # li $v0, 11
        # syscall

        addu $t1, 1

        j loop

exit:

    li $v0, 10
    syscall