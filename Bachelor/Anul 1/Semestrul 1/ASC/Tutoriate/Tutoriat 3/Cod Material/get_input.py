

inputfile = open("raw_input.txt")                           # deschide fisierul din care citim
outputfile = open("processed_input.txt", "a")               # deschide fisierul in care scriem

numbers = [int(x) for x in inputfile.readline().split(",")] # parseaza vectorul de numere

outputfile.write(str(len(numbers)) + "\n")                  # scrie numarul de elemente din vector

for number in numbers:                                      # parcurge array-ul
    outputfile.write(str(number) + "\n")                    # scrie numerele separate prin rânduri noi

outputfile.close()                                          # închide fișierul în care scriem
inputfile.close()                                           # închide fișierul din care citim
                                                            # la colocviu la PA să nu uitați să
                                                            # închideți fișierele
                                                            # cei care corectează au și lucrui de genul
                                                            # în vedere

