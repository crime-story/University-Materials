import Data.List

--1
factori :: Int -> [Int]
factori x = [ d | d <- [1..abs(x)], mod (abs(x)) d == 0]

-- factori (-5)
-- rezolva problema considerand ca nr sunt pozitive fara a apela pe nr negative

-- Trebuie sa mearga si pe numere negative nu ?
-- Nu, nu neaparat. prefer sa termini problemele pe nr pozitive si daca vrei acasa sa --tratezi si cazul de negativ Aa ok

--2
prim :: Int -> Bool
prim x
    | length(factori x) == 2 = True
    | otherwise = False

--3
numerePrime :: Int -> [Int]
numerePrime x = [d | d <- [2..x], prim(d)]

--4
myzip3 :: [Int] -> [Int] -> [Int] -> [(Int, Int, Int)]
myzip3 list1 list2 list3 = [(x ,y, z) | ((x,y),z) <- zip (zip list1 list2) list3]

--5
ordonataNat :: [Int] -> Bool
ordonataNat xs = and [ a <= b | (a,b) <- zip xs (tail xs) ]

--6
ordonataNat1 :: [Int] -> Bool
ordonataNat1 [] = True
ordonataNat1 [x] = True
ordonataNat1 (x:y:s)
    | x < y = ordonataNat1 s
    | otherwise = False

--7
functie x y
    | x <= y = True
    | otherwise = False

functieDiv x y
    | mod x y == 0 = True
    | otherwise = False

ordonata :: [a] -> (a -> a -> Bool) -> Bool
ordonata [] functie = True
ordonata [x] functie = True
ordonata (x:y:s) functie
    | functie x y == False = False
    | otherwise = ordonata (y:s) functie

--8
infixr 6 *<*
(*<*) :: (Integer, Integer) -> (Integer, Integer) -> Bool
(x,y) *<* (z, w)
    | (x-y) /= (z-w) = True
    | otherwise = False

--9
compuneList :: (b -> c) -> [(a -> b)] -> [( a -> c)]
compuneList g lf = [ g . f | f <- lf ]

--10
aplicaList :: a -> [(a->b)] -> [b]
aplicaList x lf = [ f x | f <- lf ]

aplicaList2 x [] = [] 
aplicaList2 x (f: lf) = f x : aplicaList2 x lf 