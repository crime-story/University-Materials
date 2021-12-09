import Data.List (nub)
import Data.Maybe (fromJust)


type Nume = String
data Prop
  = Var Nume
  | F
  | T
  | Not Prop
  | Prop :|: Prop
  | Prop :&: Prop
  | Prop :<->: Prop
  | Prop :->: Prop
  deriving (Eq, Read)
infixr 2 :|:
infixr 3 :&:

{-
(Var "P" :|: Var "Q") :&: (Var "P" :&: Var "Q")
  | Prop1 :&: Prop2 din 2 bucati: 
  Prop1 : (Var "P" :|: Var "Q")
  Prop2 : Var "P" :&: Var "Q")
  Prop1 e Prop3 :|: Prop4
-}

--1
p1 :: Prop
p1 = (Var "P" :|: Var "Q") :&: (Var "P" :&: Var "Q")

p2 :: Prop
p2 = (Var "P" :|: Var "Q") :&: (Not (Var "P") :|: Not (Var "Q"))

p3 :: Prop
p3 = (Var "P" :&: (Var "Q" :|: Var "R")) :&: ((Not (Var "P") :|: Not (Var "Q")) :&: (Not (Var "P") :|: Not (Var "R")))

--2
instance Show Prop where
  show T = "A"
  show F = "F"
  show (Var nume) = nume
  show (prop1 :&: prop2) = "(" ++ show prop1 ++ "&" ++ show prop2 ++ ")"
  show (prop1 :|: prop2) = "(" ++ show prop1 ++ "|" ++ show prop2 ++ ")"
  show (Not prop) = "(~" ++ show prop ++ ")"
  show (prop1 :<->: prop2) = "(" ++ show prop1 ++ "<->" ++ show prop2 ++ ")"
  show (prop1 :->: prop2) = "(" ++ show prop1 ++ "->" ++ show prop2 ++ ")"

test_ShowProp :: Bool
test_ShowProp =
    show (Not (Var "P") :&: Var "Q") == "((~P)&Q)"

--3
type Env = [(Nume, Bool)]

impureLookup :: Eq a => a -> [(a,b)] -> b
impureLookup a = fromJust . lookup a

eval :: Prop -> Env -> Bool
eval T env = True
eval F env = False
eval (Var prop) env = impureLookup prop env
eval (Not prop) env = not (eval prop env)
eval (prop1 :&: prop2) env =  (eval prop1 env) && (eval prop2 env)
eval (prop1 :|: prop2) env = (eval prop1 env) || (eval prop2 env)
eval (prop1 :<->: prop2) env = eval prop1 env == eval prop2 env
eval (prop1 :->: prop2) env = not (eval prop1 env) || eval prop2 env

test_eval = eval  (Var "P" :|: Var "Q") [("P", True), ("Q", False)] == True

--4
variabile :: Prop -> [Nume]
variabile T = []
variabile F = []
variabile (Var p) = [p]
variabile (Not prop) = variabile prop
variabile (prop1 :&: prop2) = nub (variabile prop1 ++ variabile prop2)
variabile (prop1 :|: prop2) = nub (variabile prop1 ++ variabile prop2)
variabile (prop1 :<->: prop2) = nub (variabile prop1 ++ variabile prop2)
variabile (prop1 :->: prop2) = nub (variabile prop1 ++ variabile prop2)
test_variabile =
  variabile (Not (Var "P") :&: Var "P") == ["P", "Q"]


--5
envs :: [Nume] -> [Env]
envs [] = [[]]
envs (x:xs) = [ (x,False) : e | e <- envs xs ] ++ [ (x,True) : e | e <- envs xs ]

{-
[2,3,4]
2 : 3 : 4
[2, 5] ++ [3,4]

["P","Q","R"]
envs "P":["Q","R"] => [("P", False) : e | e <- envs ["Q", "R"]] ++
                      [("P", True) : e | e <- envs ["Q", "R"]]
envs "Q":["R"] => [("Q", False) : e | e <- envs ["R"]] ++
                  [("Q", True) : e | e <- envs ["R"]]
envs "R":[] => [("R", False), ("R", True)]
iti ia asta si ti-l baga mai sus
envs "Q":["R"] => [[("Q", False),("R", False)],[("Q", False),("R", True)]]
                  ++ [[("Q", True),("R", False)],[("Q", True),("R", True)]]

-}

test_envs =
    envs ["P", "Q"]
    ==
    [ [ ("P",False)
      , ("Q",False)
      ]
    , [ ("P",False)
      , ("Q",True)
      ]
    , [ ("P",True)
      , ("Q",False)
      ]
    , [ ("P",True)
      , ("Q",True)
      ]
    ]

--6

satisfiabila :: Prop -> Bool
satisfiabila prop = or [eval prop env| env <- envs (variabile prop)]

test_satisfiabila1 :: Bool
test_satisfiabila1 = satisfiabila (Not (Var "P") :&: Var "Q") == True
test_satisfiabila2 = satisfiabila (Not (Var "P") :&: Var "P") == False


valida :: Prop -> Bool
valida prop = not (satisfiabila (Not prop))

test_valida1 = valida (Not (Var "P") :&: Var "Q") == False
test_valida2 = valida (Not (Var "P") :|: Var "P") == True

--10
echivalenta :: Prop -> Prop -> Bool
echivalenta prop1 prop2 = and [eval (prop1:<->:prop2) env| env <- envs(variabile(prop1:<->:prop2))]

--sau
echivalenta2 :: Prop -> Prop -> Bool 
echivalenta2 prop1 prop2 = valida ( prop1:<->:prop2 )

test_echivalenta1 =
  True
  ==
  (Var "P" :&: Var "Q") `echivalenta` (Not (Not (Var "P") :|: Not (Var "Q")))
test_echivalenta2 =
  False
  ==
  (Var "P") `echivalenta` (Var "Q")
test_echivalenta3 =
  True
  ==
  (Var "R" :|: Not (Var "R")) `echivalenta` (Var "Q" :|: Not (Var "Q"))

{-
  Var "P" pentru P = True -> True 
  Var "P" pentru P = False -> False
  Var "Q" pentru Q = True -> True 
  Var "Q" pentru Q = False -> False

  ("P", False), ("Q", False)
  ("P", False), ("Q", True)
  ("P", True), ("Q", False)
  ("P", True), ("Q", True)
-}