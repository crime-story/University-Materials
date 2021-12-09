-- Popescu Paullo Robertto Karloss
-- Grupa 2311
-- laborator 6

import Data.Char
import Data.List

-- Ex 1
-- Varianta 1 cu if si else
rotate :: Int -> [Char] -> [Char]
rotate n string = 
    if n <= 0 
        then error "n is negative"
    else
        if n > length string 
            then error "n is bigger than length of string"
        else
            drop n string ++ take n string

-- Varianta 2 cu garzi
rotate2 :: Int -> [Char] -> [Char]
rotate2 n string
    | n < 0 = error "n is negative"
    | n > length string = error "n is bigger than length of string"
    | otherwise = drop n string ++ take n string

-- Ex 2
prop_rotate :: Int -> [Char] -> Bool
prop_rotate n string = rotate (length string - m) (rotate m string) == string
                        where m = if length string == 0 then 0 
                                else mod n (length string)

-- prop_rotate verifica daca functia rotate se comporta corespunzar
-- mai precis, se verifica daca sirul returnat este acelasi cu cel initial
-- in urma efectuarii a doua rotatii

-- Ex 3
makeKey :: Int -> [(Char, Char)]
makeKey n = zip alphabet (rotate n alphabet)
            where alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

-- Ex 4
lookUp :: Char -> [(Char, Char)] -> Char
lookUp character list = head [y | (x, y) <- list ++ [(character, character)], x == character]

-- Ex 5
encipher :: Int -> Char -> Char
encipher n character = lookUp character (makeKey n)

-- Ex 6
lowerAlphabet :: [Char]
lowerAlphabet = "abcdefghijklmnopqrstuvwxyz"
upperAlphabet :: [Char]
upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
digits :: [Char]
digits = "0123456789"

-- Varianta 1 cu garzi
normalize :: String -> String
normalize [] = []
normalize (character:string)
    | character `elem` lowerAlphabet = toUpper character : normalize string
    | character `elem` upperAlphabet = character : normalize string
    | character `elem` digits = character : normalize string
    | otherwise = normalize string

-- Varianta 2 cu if si else
normalize2 :: String -> String
normalize2 [] = []
normalize2 (character:string) =
    if character `elem` lowerAlphabet
        then toUpper character : normalize2 string
    else
        if character `elem` upperAlphabet 
            then character : normalize2 string
        else
            if character `elem` digits
                then character : normalize2 string
            else
                normalize2 string

-- Ex 7
encipherStr :: Int -> String -> String
encipherStr n string = [encipher n character | character <- normalize string]

-- Ex 8
reverseKey :: [(Char, Char)] -> [(Char, Char)]
reverseKey keys = [(y, x) | (x, y) <- keys]

-- Ex 9
decipher :: Int -> Char -> Char
decipher n character = lookUp character (reverseKey (makeKey n))

deNormalize :: String -> String
deNormalize [] = []
deNormalize (character:string)
    | character `elem` upperAlphabet || character `elem` digits = character : deNormalize string
    | otherwise = deNormalize string

decipherStr :: Int -> String -> String
decipherStr n string = [decipher n character | character <- deNormalize string]