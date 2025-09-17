import Data.List
import Data.Maybe

type Name = String

data  Value  =  VBool Bool
     |VInt Int
     |VFun (Value -> Value)
     |VError

data  Hask  = HTrue | HFalse
     |HIf Hask Hask Hask
     |HLit Int
     |Hask :==: Hask
     |Hask :+:  Hask
     |HVar Name
     |HLam Name Hask
     |Hask :$: Hask
     |Hask :*: Hask

infix 4 :==:
infixl 6 :+:
infixl 9 :$:
infixl 7 :*:

type  HEnv  =  [(Name, Value)]

-- Ex 1
instance Show Value where
     show (VBool x) = show x
     show (VInt x) = show x
     show (VFun x) = "Function"
     show (VError) = "Error"

-- Ex 2
instance Eq Value where
     VBool x == VBool y = x == y
     VInt x == VInt y = x == y
     _ == _ = error "Nu se pot compara!"

-- Ex 3
hEval :: Hask -> HEnv -> Value
hEval HTrue r      =  VBool True
hEval HFalse r        =  VBool False
hEval (HIf c d e) r   = hif (hEval c r) (hEval d r) (hEval e r)
  where  hif (VBool b) v w  =  if b then v else w
         hif _ _ _ = error "HIf"
hEval (HLit i) _ = VInt i
hEval (x :==: y) r = heq v1 v2
     where v1 = hEval x r
           v2 = hEval y r
           heq (VInt i) (VInt j) = VBool (i == j)
           heq _ _ = error ":==:"
hEval (x :+: y) r = hadd v1 v2
     where v1 = hEval x r
           v2 = hEval y r
           hadd (VInt i) (VInt j) = VInt (i + j)
           hadd _ _ = error ":+:"
hEval (HVar x) r = fromMaybe (error "HVar") (lookup x r)
hEval (HLam x y) r = VFun (\v -> hEval y ((x,v):r))
hEval (x :$: y) r = happ fct val
     where fct = hEval x r
           val = hEval y r
           happ (VFun f) v = f v
           happ _ _ = error ":$:"
hEval (x :*: y) r = hprod v1 v2
     where v1 = hEval x r
           v2 = hEval y r
           hprod (VInt i) (VInt j) = VInt (i * j)
           hprod _ _ = error ":*:"

-- Ex 4
run :: Hask -> String
run pg = show (hEval pg [])

-- Ex 4.1
program = (HLam "y" (HLam "x" (HVar "x" :+: HVar "y"))) :$: (HLit 4) :$: (HLit 3)

-- Ex 4.2 rez sus

-- Ex 4.3 rez sus