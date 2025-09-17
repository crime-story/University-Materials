import Data.List
import Data.Char

main = putStrLn "Hello, world!"

--1
poly :: Double -> Double -> Double -> Double -> Double
poly a b c x = a*x^2+b*x+c

--2
eeny :: Integer -> String
eeny x = 
    if (even x) 
        then "eeny"
    else
        "even"

--3
fizzbuzz :: Integer -> String
fizzbuzz x =
    if ((mod x 3 == 0) && (mod x 5 == 0))
        then "FizzBuzz"
    else
        if (mod x 3 == 0)
            then "Fizz"
        else 
            if (mod x 5 == 0)
                then "Buzz"
            else
                ""
fizzbuzz2 :: Integer -> String
fizzbuz2z x
    | mod x 3 == 0 && mod x 5 == 0 = "fizzbuzz"
    | mod x 3 == 0 = "fizz"
    | mod x 5 == 0 = "buzz"
    | otherwise = ""

--4
--cazuri
tribonacci :: Integer -> Integer
tribonacci x
    | x <= 2 = x
    | x == 3 = 2
    | otherwise = tribonacci(x-1) + tribonacci(x-2) + tribonacci(x-3)

--ecuational
tribonacciEcuational :: Integer -> Integer
tribonacciEcuational 0 = 0
tribonacciEcuational 1 = 1
tribonacciEcuational 2 = 2
tribonacciEcuational 3 = 2
tribonacciEcuational x = 
    tribonacciEcuational (x-1) + tribonacciEcuational(x-2) + tribonacciEcuational(x-3)

--5
binomial :: Integer -> Integer -> Integer
binomial n k = 
    if (k == 0)
        then 1
    else if (n == 0)
            then 0
        else
            binomial (n-1) k +binomial(n-1) k-1

--6
--a)
verifL :: [Int] -> Bool
verifL v =
    if (even (length v))
        then True
    else
        False

--b)

takefinal :: [Int] -> Int -> [Int]
takefinal v n = 
    if (length v < n)
        then v
    else
        drop(length v - n) v

--c)
remove :: [Int] -> Int -> [Int]
remove v n = take (n-1) v ++ drop n v 

--7
--a)
myreplicate :: Int -> a -> [a]
myreplicate 0 _ = []
myreplicate n v =
    v : myreplicate(n-1) v

--b)
sumImp :: [Int] -> Int
sumImp xs 
    | length xs == 0 = 0
    | otherwise = (if (even (head xs)) then 0 else head xs ) + sumImp (tail xs)

--c)
totalLen :: [String] -> Int
totalLen (x:xs) = 
    if x /="" && head x == 'A'
        then (totalLen xs) + length x
    else
        totalLen xs