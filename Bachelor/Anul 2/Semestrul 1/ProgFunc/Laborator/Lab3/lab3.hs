import Data.List
import Data.Char

--1
nrVocale :: [String] -> Int
nrVocale [] = 0
nrVocale (c:s) = (if palindrom c then nrV c else 0) + nrVocale s 
   where 
     palindrom n = n == reverse n
     vocale =  "aeiouAEIOU"
     nrV "" = 0 
     nrV (c:sir) 
        | c `elem` vocale = 1 + nrV sir 
        | otherwise = nrV sir

--2
f :: Int -> [Int] -> [Int]
f x [] = []
f x (c:s) = if (even c) then c : x : f x s
            else
                c : f x s

--Exemplu
--semiPareComp :: [Int] -> [Int]
--semiPareComp l = [x `div` 2 | x <- l, even x]

--3
divizori :: Int -> [Int]
divizori x = [ d | d <- [1..x], mod x d == 0]

--4
listadiv :: [Int] -> [[Int]]
listadiv [] = []
listadiv l = [ divizori d | d <- l]

--5 a
inIntervalRec :: Int -> Int -> [Int] -> [Int]
inIntervalRec x y [] = []
inIntervalRec x y (c:s) = if c >= x && c <= y
                            then c : inIntervalRec x y s
                        else
                            inIntervalRec x y s

--5 b
inIntervalComp :: Int -> Int -> [Int] -> [Int]
inIntervalComp x y l = [d | d <- l, d >= x && d <= y]

--6a
pozitiveRec :: [Int] -> Int
pozitiveRec [] = 0
pozitiveRec (c:s) = if c >= 0
                        then 1 + pozitiveRec s
                    else
                        pozitiveRec s
--6b
pozitiveComp :: [Int] -> Int
pozitiveComp l = length[d | d <- l, d >= 0]

--7a
pozitiiImpareRec :: [Int] -> Int -> [Int]
pozitiiImpareRec [] poz = []
pozitiiImpareRec (c:s) poz
    | c `mod` 2 == 1 = poz : pozitiiImpareRec s (poz + 1) --sau odd c pentru impar
    | otherwise = pozitiiImpareRec s (poz + 1)

--7b
pozitiiImpareComp :: [Int] -> [Int]
pozitiiImpareComp l = [poz | (x,poz) <- l `zip` [0..], odd x]

--8a
multDigitsRec :: String -> Int
multDigitsRec [] = 1
multDigitsRec (c:s)
    | isDigit c = digitToInt c * multDigitsRec s 
    | otherwise = multDigitsRec s

--8b
multDigitsComp :: String -> Int
multDigitsComp l = product[digitToInt x | x <- l, isDigit x]