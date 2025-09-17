.data
    x:.word 3
    y:.word 6
.text

suma:

    subu $sp, 4       # pun frame pointerul pe stiva
    sw $fp, 0($sp)    # aceste doua linii de la inceputul procedurii
                      # trebuie sa le scrieti la inceputul fiecarei proceduri
                      # aceasta este voia maestrului
                      # acum stiva arata asa: ($fp), (x), (y) 

    addi $fp, $sp, 4  # face ca $fp sa pointeze la inceputul cadrului de apel
                      # adica ($fp), <fp_pointeaza_aici> (x), (y)

    subu $sp, 4       # pune s0 pe stivă
    sw $s0, 0($sp)    # acum stiva arata asa ($s0), ($fp), (x), (y)

    subu $sp, 4       # pune $s1 pe stivă
    sw $s1, 0($sp)    # acum stiv arata asa ($s1), ($s0), ($fp), (x), (y)

                      # Motivul pentru care punem fp, s0 și s1 pe stivă este
                      # deoarece noi le vom considera ca pe niște variabile locale,
                      # dar ideea este ca si alte proceduri le consideră tot ca pe niste
                      # variabile locale, deci in cadrul nostru de apel trebuie să aiba unele
                      # valori, iar in alte cadre de apel trebuie să aibă alte valori.
                      # De accea. noi cand intram in procedura salvam  pe stiva valorile cu care au venit
                      # iar cand terminam proocedura, rstituim valorile bune pentru ca 
                      # acești registri să aiba valorile bune și în cadrul
                      # celorlalte proceduri, ci nu valorile pe care l-am folosit
                      # in procedura noastra 
    
    lw $s0, 0($fp)    # incarc in s0 prima valoare catre care pointeaza
                      # frame pointer-ul nostru, adica x
    lw $s1, 4($fp)    # incarc in s1 a doua valoare catre care pointeaza
                      # frame pointer-ul nostru, adica y
                      # tinem minte ca fp pointeaza catre (x), (y)

    add $v0, $s0, $s1 # facem adunarea si punem rezultatul 
                      # in registrul $v0
    
    lw $s1, -12 ($fp) # restitui $s1, asa cum am vazut mai devreme
                      # ca trebuie sa facem
    lw $s0, -8 ($fp)  # analog, restitui $s0
    lw $fp, -4 ($fp)  # analog, restitui $fp

    addu $sp, 12      # acum ca am restituit $s1, $s1 si $fp
                      # trebuie sa le dau pop de pe stiva

    jr $ra            # acum ma intor in main
                      # la linia urmatoare apelului
                      # procedurii

main:

    lw $t0, y 
    subu $sp, 4
    sw $t0, 0($sp)    # il pun pe y pe stiva
                      # acum stiva arata asa: (y)

    lw $t0, x
    subu $sp, 4
    sw $t0, 0($sp)    # il pun pe x pe stiva
                      # acum stiva arata asa (x), (y)
 
    jal suma          # apelez procedura care face suma

    addu $sp, 8       # dau pop la x si y de pe stiva
                      # din moment ce nu imi trebuie valorile lor
                      # nu le mai salvez

    move $a0, $v0     # afisez pe ecran rezultatul 
                      # (pe care procedura) mi l-a returnat
                      # prin registrul $v0 
    li $v0, 1
    syscall

    li $v0, 10
    syscall