import Data.Maybe
import Data.List
data Prop = Var String | True | False | Prop :&: Prop | Prop :|: Prop


evalInEnv :: [(String, Bool)] -> Prop -> Bool 
evalInEnv _ Main.True = Prelude.True
evalInEnv _ Main.False = Prelude.False
evalInEnv env (a :&: b) = (evalInEnv env a) && (evalInEnv env b)
evalInEnv env (a :|: b) = (evalInEnv env a) || (evalInEnv env b)
evalInEnv env (Var a) = fromJust (lookup a env)

getvars :: Prop -> [String]
getvars Main.True = []
getvars Main.False = []
getvars (a :&: b) = union (getvars a) (getvars b)
getvars (a :|: b) = union (getvars a) (getvars b)
getvars (Var a) = [a]

getVarEval :: Int -> String -> (String,Bool)
getVarEval bit var 
    |rem bit 2 == 1 = (var, Prelude.True)
    |otherwise = (var, Prelude.False)


getEnv :: Int -> [String] -> [(String,Bool)]
getEnv _ [] = []
getEnv nr (var:vars) = (getVarEval nr var) : (getEnv (nr `div` 2) vars)

evalGenerateNumbers :: Int -> [String] -> Prop -> [Bool]
evalGenerateNumbers nr vars p 
    | nr < 0 = evalGenerateNumbers 0 vars p -- in caz ca cineva apeleaza gresit
    | nr < 2 ^ (length vars) = ( evalInEnv (getEnv nr vars ) p ) : (evalGenerateNumbers (nr+1) vars p )
    | otherwise = []

evalGenerate :: [String] -> Prop -> [Bool]
evalGenerate vars p = evalGenerateNumbers 0 vars p

boolToInt :: Bool -> Int
boolToInt x
    | x == Prelude.True = 1
    | otherwise = 0

eval :: Prop -> [Int]
eval p = map boolToInt (evalGenerate (getvars p) p)


ghci> eval ((Var "X") :&: (Var "Y"))
[0,0,0,1]
ghci> eval ((Var "X") :&: (Var "X"))
[0,1]
ghci> eval ((Var "X") :|: (Var "X"))
[0,1]
ghci> eval ((Var "X") :|: (Var "Y"))
[0,1,1,1]
ghci> eval (((Var "maria") :&: (Var "ioana")) :|: (Main.False :|: (Var "andreea")))
[0,0,0,1,1,1,1,1]
ghci> eval (((Var "maria") :&: (Var "ioana")) :|: (Main.True :|: (Var "andreea")))
[1,1,1,1,1,1,1,1]
ghci> eval (((Main.True :&: (Var "maria")) :&: (Var "ioana")) :|: (Main.False :|: (Var "andreea")))
[0,0,0,1,1,1,1,1]
ghci> eval (Main.True :&: Main.False)
[0]
ghci> eval (Main.False :&: Main.False)
[0]
ghci> eval (Main.False :|: Main.False)
[0]
ghci> eval (Main.False :|: Main.True)
[1]
ghci> eval (Main.True :|: Main.False)
[1]