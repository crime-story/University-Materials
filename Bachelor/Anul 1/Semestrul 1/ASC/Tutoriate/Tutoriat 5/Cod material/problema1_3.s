.data
    v:.word 5, 13, 27, 3, 11, 29
    n:.word 6
    ch:.byte ' '
.text

afis:

    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $s0, 0($sp)

    subu $sp, 4
    sw $s1, 0($sp)

    lw $s0, 0($fp)  # iau adresa de memorie a vectorului
                    # de pe stiva
    lw $s1, 4($fp)  # iau si lungimea lui de pe stiva
    li $t0, 0       # vom folosi $t0 aici
                    # puteam foarte bine sa folosim si $s2 in loc
                    # dar pentru asta trebuia sa il punem pe stiva
                    # si sa il scoatem la sfarsit
                    # pe $t-uri nu avem astfel de conventii

    loop:           # parcurg vectorul normal

        bge $t0, $s1, exit

        lw $a0, 0($s0)
        li $v0, 1
        syscall

        lb $a0, ch
        li $v0, 11
        syscall

        addu $s0, 4 # incrementez adresa de memorie
                    # curenta a vectorului
                    # pentru a trece la urmatorul
                    # element
        addu $t0, 1

        j loop

    exit:

    lw $s1, -12($fp)
    lw $s0, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 12

    j $ra

main:

    lw $t0, n       # pun pe stiva lungimea array-ului
    subu $sp, 4
    sw $t0, 0($sp)

    la $t0, v       # pun pe stiva adresa de memorie
    subu $sp, 4     # de unde incepe array-ul
    sw $t0, 0($sp)

    jal afis        # fac afis(v, n), unde v
                    # este adresa de memorie unde incepe v

    addu $sp, 8

    li $v0, 10
    syscall