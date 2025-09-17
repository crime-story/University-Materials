-- Popescu Paullo Robertto Karloss Grupa 231
-- Laborator 9

-- Ex 1
-- Ana are mere. ! ? :
-- a)
sfChr :: Char -> Bool
sfChr x
    | x == '!' = True
    | x == '?' = True
    | x == '.' = True
    | x == ':' = True
    | otherwise = False

-- sfChr varianta 2
sfChr2 :: Char -> Bool
sfChr2 x = x `elem` "?!.:"

propozitii :: String -> Int
propozitii [] = 0
propozitii (caracter:prop) =
    if (sfChr(caracter))
        then 1 + propozitii(prop)
    else
        propozitii(prop)

-- b)
-- varianta 1
propozitii2 :: String -> Int
propozitii2 prop = sum([1 | x <- prop, sfChr2(x)])

-- varianta 2
propozitii3 :: String -> Int
propozitii3 prop = length([x | x <- prop, sfChr2(x)])

-- Ex 2
linie :: [Int] -> Bool
linie [] = True
linie (x:list)
    | x > 0 = linie(list)
    | otherwise = False

liniiN :: [[Int]] -> Int -> Bool
liniiN matrice n = foldr (&&) True (map linie (filter(\x -> length(x) == n) matrice))

-- Ex 3
data Punct = Pt [Int]
            deriving Show
data Arb = Vid | F Int | N Arb Arb
            deriving Show
class ToFromArb a where
            toArb :: a -> Arb
            fromArb :: Arb -> a

-- varianta 1
instance ToFromArb Punct where
            toArb (Pt []) = Vid
            toArb (Pt (x:lista)) = N (F x) (toArb (Pt lista))
            fromArb (x) = Pt (fromArb2 x)
fromArb2 :: Arb -> [Int]
fromArb2 Vid = []
fromArb2 (F x) = [x]
fromArb2 (N x y) = fromArb2(x) ++ fromArb2(y)

-- varianta 2
{-
instance ToFromArb Punct where
    toArb (Pt []) = Vid
    toArb (Pt (x:xs)) = N (F x) (toArb (Pt xs))
    fromArb Vid = Pt []
    fromArb (F x) = Pt [x]
    fromArb (N arb1 arb2) = fromArbAux (fromArb arb1) (fromArb arb2)

fromArbAux :: Punct -> Punct -> Punct
fromArbAux (Pt x) (Pt y) = Pt (x ++ y)
-}
