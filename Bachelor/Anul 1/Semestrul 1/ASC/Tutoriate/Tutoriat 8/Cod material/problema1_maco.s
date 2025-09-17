.data

    v:.space 400                    # vectorul pe care vom lucra

.text

suma_cifrelor_para:

    subu $sp, 4                     # punem $fp pe stiva
    sw $fp, 0($sp)                  # stiva este $sp: ($fp) (z) (celelalte_lucruri_care_sunt_deja_pe_stiva)

    addi $fp, $sp, 4                # face fp sa pointeeze catre cadrul nostru de apel
                                    # avem $sp: ($fp) <fp_pointeaza_aici> (z) (celelalte_lucruri_care_sunt_deja_pe_stiva)

    subu $sp, 4                     # pune $ra pe stiva
    sw $ra, 0($sp)                  # stiva este $sp: ($ra) ($fp) <fp_pointeaza_aici> (z) (celelalte_lucruri_care_sunt_deja_pe_stiva)

    subu $sp, 4                     # pune $s0 pe stiva
    sw $s0, 0($sp)                  # stiva este $sp: ($s0) ($ra) ($fp) <fp_pointeaza_aici> (z) (celelalte_lucruri_care_sunt_deja_pe_stiva)

    subu $sp, 4                     # pune $s1 pe stiva
    sw $s1, 0($sp)                  # stiva este $sp: ($s1) ($s0) ($ra) ($fp) <fp_pointeaza_aici> (z) (celelalte_lucruri_care_sunt_deja_pe_stiva)

    lw $s0, 0($fp)                  # in $s0 il vom avea pe z si il vom imparti succesiv la 10
                                    # pana cand va avea valoarea 0 
    li $v0, 0                       # initial vom folosi $v0 pentru suma, iar apoi pentru rezultatul final

    parc_cifre:

        beq $s0, 0, exit_parc_cifre # am ajuns la 0, deci numarul nu mai are cifre

        rem $s1, $s0, 10            # in $s0 vom avea ultima cifra a numarului
        add $v0, $v0, $s1           # adunam cifra la suma

        div $s0, $s0, 10            # impartim numarul la 10, adica scoatem ultima lui cifra
                                    # de la sfarsit

        j parc_cifre                # parcurgem cifrele

exit_parc_cifre:                    # am terminat de parcurs cifrele

    rem $s0, $v0, 2                 # in $s0 avem result impartirii sumei cifrelor la 2
                                    # pentru a decide daca e par sau nu

    seq $v0, $s0, 0                 # daca suma e para, in $v0 pune 1, altfel 0

    lw $s1, -16($fp)                # restauram registrii de pe stiva
    lw $s0, -12($fp)                # adica (s1, s0, ra, fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 16                    # da pop la stiva

    jr $ra                          # iese din functie

evalueaza:

    subu $sp, 4                     # punem fp pe stiva
    sw $fp, 0($sp)                  # stiva este $sp: ($fp) (pointer_catre_v) (n) (x) (y) (z)

    addi $fp, $sp, 4                # face fp sa pointeeze catre cadrul nostru de apel
                                    # avem $sp: ($fp) <fp_pointeaza_aici> (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # punem $ra pe stiva
    sw $ra, 0($sp)                  # stiva este $sp: ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s0 pe stiva
    sw $s0, 0($sp)                  # stiva este $sp: ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s1 pe stiva
    sw $s1, 0($sp)                  # stiva este $sp: ($s1) ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s2 pe stiva
    sw $s2, 0($sp)                  # stiva este $sp: ($s2) ($s1) ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s3 pe stiva
    sw $s3, 0($sp)                  # stiva este $sp: ($s3) ($s2) ($s1) ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s4 pe stiva
    sw $s4, 0($sp)                  # stiva este $sp: ($s4) ($s3) ($s2) ($s1) ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    subu $sp, 4                     # pune $s5 pe stiva
    sw $s5, 0($sp)                  # stiva este $sp: ($s5) ($s4) ($s3) ($s2) ($s1) ($s0) ($ra) ($fp) (pointer_catre_v) (n) (x) (y) (z)

    lw $s0, 4($fp)                  # in $s0 il vom avea pe n
    li $s1, 0                       # $s1 va fi counterul (adică i din formula)
    lw $s2, 0($fp)                  # $s2 va fi un pointer catre pzoitia curenta din vector
                                    # nu vom folosi un registru pentru a tine adresa de memorie
                                    # a vectorului si un alt registru pentru counterul din vector.
                                    # in schimb vom avea un signur registru cu adresa de memorie
                                    # din vector in care ne aflam
                          
                                    # $s3 il vom folosi pentru uz general
    li $s4, 0                       # in $s4 vom acumula suma
                                    # $s5 il vom folosi pentru uz general

    parcurg:                        # parcurge vectorul

        beq $s0, $s1, fin_ev 

        lw $s3, 0($s2)              # momentan, in $s3 vom tine valoarea lui v[i] 

        subu $sp 4                  # punem v[i] pe stiva pentru a apela procedura suma_cifrelor_para
        sw $s3, 0($sp)              # stiva: $sp: (v[i]) (celelalte_lucruri_care_sunt_deja_pe_stiva)

        jal suma_cifrelor_para      # apelam procedura suma_cifrelor_para

        addu $sp, 4                 # ii dam pop lui v[i] de pe stiva.
                                    # stiva: $sp: (celelalte_lucruri_care_sunt_deja_pe_stiva)

                                    # in v0 am rezultatul lui suma_cifrelor_para(v[i])

        beq $v0, 0, ev_add_cond     # daca in v0 am vloarea 1 nu ma intereseaza (pentru ca avem (1-1)*ceva in formula, 
                                    # adica 0*ceva=0)

        j ev_cont_parc

        ev_add_cond:                # daca $v0 are valoarea 0, adunam a doua parte din formula la suma
                                    # adica $s4 += (v[i] mod x + (y - (z div 3) + i)^3)

            lw $s5, 8($fp)          # in $s5 il punem pe x
            rem $s5, $s3, $s5       # in $s5 punem v[i] mod x (momentan $s3 are tot valoarea v[i])

            add $s4, $s4, $s5       # adunam la suma pe v[i] mod x
                                    # deci ne-a ramas de calculat doar (y - (z div 3) + i)^3

            lw $s3, 12($fp)         # in $s3 il punem pe y
            lw $s5, 16($fp)         # in $s5 il punem pe z
            div $s5, $s5, 3         # in $s5 punem (z div 3)
            
            subu $s3, $s3, $s5      # calculam x - (x div 3) si tinem valoarea in $s3

            add $s3, $s3, $s1       # adunam i, deci avem x - (x div 3) + i in $s3
            move $s5, $s3           # copiem aceeasi valoare si in $s5, adica $s5 = x - (x div 3) + i

            mul $s3, $s3, $s3       # in $s3 avem (x - (x div 3) + i)^2
            mul $s3, $s3, $s5       # acum in $s3 avem (x - (x div 3) + i)^3

            add $s4, $s4, $s3       # adunam (x - (x div 3) + i)^3 la suma

        ev_cont_parc:

            addu $s1, 1             # incrementez counterul
            addu $s2, 4             # incrementez adresa de memorie curenta din vector

            j parcurg               # continui parcurgerea

    fin_ev:

    move $v0, $s4                   # returnam prin $v0 rezultatul

    lw $s5, -32($fp)                # restauram toti registrii de care ne-am folosit
    lw $s4, -28($fp)                # adica ($s0..$s5, $fp, $ra)
    lw $s3, -24($fp)
    lw $s2, -20($fp)
    lw $s1, -16($fp)
    lw $s0, -12($fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 32                    # dam pop de pe stiva acestor registrii

    jr $ra                          # iesim din functie

main:

    li $v0, 5                       # citim n
    syscall
    move $t0, $v0

    li $t1, 0
    li $t2, 0

    read:                           # citim cele n numere din vector

        beq $t1, $t0, exit_read

        li $v0, 5
        syscall
        sw $v0, v($t2)

        addu $t1, 1
        addu $t2, 4

        j read

exit_read:

    li $v0, 5                       # citim x
    syscall
    move $t1, $v0

    li $v0, 5                       # citim y
    syscall
    move $t2, $v0

    li $v0, 5                       # citim z
    syscall
    move $t3, $v0

    subu $sp, 4                     # punem z pe stiva
    sw $t3, 0($sp)

    subu $sp, 4                     # punem y pe stiva
    sw $t2, 0($sp)

    subu $sp, 4                     # punem x pe stiva
    sw $t1, 0($sp)

    subu $sp, 4                     # punem n pe stiva
    sw $t0, 0($sp)

    subu $sp, 4                     # punem pointer catre v pe stiva
    la $t0, v
    sw $t0, 0($sp)

                                    # stiva noastra este:
                                    # $sp: (pointer_catre_v) (n) (x) (y) (z) 
    jal evalueaza                   # apelam procedura evaluaeaza

    addu $sp, 20                    # dam pop la partea din stiva unde sunt argumentele
                                    # 5 (nr_de argumente) * 4 (sizeof word) bytes = 20

    move $a0, $v0                   # afiseaza pe ecran rezolutatul
    li $v0, 1
    syscall

    li $v0, 10
    syscall