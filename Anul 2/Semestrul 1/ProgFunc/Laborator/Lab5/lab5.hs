import Data.Char
--fst (1, 2) = 1
--snd (1, 2) = 2

--1
firstEl :: [(a, b)] -> [a]
firstEl list = map fst list

--2
sumList :: [[Int]] -> [Int]
sumList list = map (foldr (+) 0) list

--3
prel2 :: [Int] -> [Int]
prel2 list = map (\x -> if even x then x `div` 2 else x*2) list

--4
cInSiruri :: Char -> [String] -> [String]
cInSiruri char list = filter (elem char) list

--5
elementePatrate :: [Int] -> [Int]
elementePatrate list = map (^2) (filter odd list) 

--6
pozitiiPatrate :: [Int] -> [Int]
pozitiiPatrate list = map ((^2).fst) (filter (odd.snd) (list `zip` [0..]))

--7
numaiVocale :: [String] -> [String]
numaiVocale list = map (filter (\x -> if x `elem` "AEIOUaeiou" then True else False)) list

--8
mymap :: (a->b) -> [a] -> [b]
mymap _ [] = []
mymap func (elem:list) = func elem : mymap func list
 
myfilter :: (a-> Bool) -> [a] -> [a]
myfilter _ [] = []
myfilter func (elem:list)
    |func elem = elem : myfilter func list
    |otherwise = myfilter func list

--9
sumPatImp :: [Int] -> Int 
sumPatImp list = foldr (+) 0 (map (^2) (filter odd list))

--10
allTrue :: [Bool] -> Bool
allTrue list = foldr (&&) True list

--11

--a
rmChar :: Char -> String -> String 
rmChar caracter cuvant = filter (\x -> if x==caracter then False else True) cuvant

--b
rmCharsRec :: String -> String -> String 
rmCharsRec [] x = x
rmCharsRec (caracter:caractereSterse) cuvant = rmCharsRec caractereSterse (rmChar caracter cuvant)

--c
rmCharsFold :: String -> String -> String
rmCharsFold caractere cuvant = foldr rmChar cuvant caractere