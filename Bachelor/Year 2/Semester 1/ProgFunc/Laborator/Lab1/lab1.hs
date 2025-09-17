import Data.List



myInt = 5555555555555555555555555555555555555555555555555555555555555555555555555555555555555



double :: Integer -> Integer

double x = x+x




-- Varianta 1

triple :: Integer -> Integer

triple x = x+x+x



-- Varianta 2

{- triple :: Integer -> Integer

triple x = double x + x -}



--maxim :: Integer -> Integer -> Integer

maxim x y = if (x > y)

then x

else y



maxim3 :: Integer -> Integer -> Integer -> Integer

maxim3 x y z =

let

t = (maxim x y)

in

(maxim t z)



maxim4 :: Integer -> Integer -> Integer -> Integer -> Integer

maxim4 x y z t =

let

u = (maxim3 x y z)

in

maxim u t



-- Exercitiul 6

-- a)

sumaPatrate :: Integer -> Integer -> Integer

sumaPatrate x y = (x * x) + (y * y)



-- b)

verifParitate :: Integer -> [Char]

verifParitate x =

if (mod x 2 == 0)

then "Par"

else

"Impar"




-- c)

factorial :: Integer -> Integer

factorial n =

if(n == 0)

then 1

else

n * factorial(n-1)



-- d)

verif :: Integer -> Integer -> Integer

verif m n =

maxim m (double n)