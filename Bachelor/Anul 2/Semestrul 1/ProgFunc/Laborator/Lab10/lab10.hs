-- Popescu Paullo Robertto Karloss Grupa 231
-- Laborator 10

-- Ex 1
data Expr = Const Int -- integer constant
          | Expr :+: Expr -- addition
          | Expr :*: Expr -- multiplication
           deriving Eq

data Operation = Add | Mult deriving (Eq, Show)
-- Popescu Paullo Robertto Karloss Grupa 231
-- Laborator 10

data Tree = Lf Int -- leaf
          | Node Operation Tree Tree -- branch
           deriving (Eq, Show)

-- Ex 1.1
instance Show Expr where
    show (Const x) = show x
    show (x1 :+: x2) = "(" ++ (show x1) ++ " + " ++ (show x2) ++ ")"
    show (x1 :*: x2) = "(" ++ (show x1) ++ " * " ++ (show x2) ++ ")"

-- Ex 1.2
evalExp :: Expr -> Int
evalExp (Const x) = x
evalExp (x :+: y) = evalExp x + evalExp y
evalExp (x :*: y) = evalExp x * evalExp y

exp1 = ((Const 2 :*: Const 3) :+: (Const 0 :*: Const 5))
exp2 = (Const 2 :*: (Const 3 :+: Const 4))
exp3 = (Const 4 :+: (Const 3 :*: Const 3))
exp4 = (((Const 1 :*: Const 2) :*: (Const 3 :+: Const 1)) :*: Const 2)
test11 = evalExp exp1 == 6
test12 = evalExp exp2 == 14
test13 = evalExp exp3 == 13
test14 = evalExp exp4 == 16

-- Ex 1.3
evalArb :: Tree -> Int
evalArb (Lf x) = x
evalArb (Node Add x1 x2) = evalArb x1 + evalArb x2
evalArb (Node Mult x1 x2) = evalArb x1 * evalArb x2

-- Ex 1.4
expToArb :: Expr -> Tree
expToArb (Const x) = Lf x
expToArb (x :+: y) = Node Add (expToArb x) (expToArb y)
expToArb (x :*: y) = Node Mult (expToArb x) (expToArb y)

arb1 = Node Add (Node Mult (Lf 2) (Lf 3)) (Node Mult (Lf 0)(Lf 5))
arb2 = Node Mult (Lf 2) (Node Add (Lf 3)(Lf 4))
arb3 = Node Add (Lf 4) (Node Mult (Lf 3)(Lf 3))
arb4 = Node Mult (Node Mult (Node Mult (Lf 1) (Lf 2)) (Node Add (Lf 3)(Lf 1))) (Lf 2)

test21 = evalArb arb1 == 6
test22 = evalArb arb2 == 14
test23 = evalArb arb3 == 13
test24 = evalArb arb4 == 16

class Collection c where
  empty :: c key value
  singleton :: key -> value -> c key value
  insert
      :: Ord key
      => key -> value -> c key value -> c key value
  clookup :: Ord key => key -> c key value -> Maybe value
  delete :: Ord key => key -> c key value -> c key value
  keys :: c key value -> [key]
  keys c = map fst (toList c)
  values :: c key value -> [value]
  values c = map snd (toList c)
  toList :: c key value -> [(key, value)]
  fromList :: Ord key => [(key,value)] -> c key value
  fromList list = foldr (uncurry insert) empty list
--   fromList [] = empty
--   fromList ((k,v):xs) = insert k v (fromList xs)

newtype PairList k v
  = PairList { getPairList :: [(k, v)] }

-- Ex 2.2
instance Collection PairList where
    empty = PairList []
    singleton k v = PairList [(k, v)] 
    insert k v (PairList c) = PairList ((k, v) : (filter (\(x, y) -> x /= k) c)) 
    clookup k (PairList c)
        | length result == 0 = Nothing 
        | otherwise = Just (snd (head result))
        where result = (filter (\(x, y) -> x == k) c)
    delete k (PairList c) = PairList (filter (\(x, y) -> x /= k) c)
    toList (PairList []) = []
    toList (PairList (x:c)) = x:(toList (PairList c)) 

-- PairList [(2, 3), (3, 4), (4, 2)]
data SearchTree key value
  = Empty
  | BNode
      (SearchTree key value) -- elemente cu cheia mai mica
      key                    -- cheia elementului
      (Maybe value)          -- valoarea elementului
      (SearchTree key value) -- elemente cu cheia mai mare

-- Ex 2.3
instance Collection SearchTree where
    empty = Empty
    singleton k v = BNode Empty k (Just v) Empty
    insert k v Empty = singleton k v
    insert k v (BNode arbs key value arbd)
        | k == key = BNode arbs key (Just v) arbd
        | k < key = BNode (insert k v arbs) key value arbd
        | k > key = BNode arbs key value (insert k v arbd)
    clookup k Empty = Nothing
    clookup k (BNode arbs key value arbd)
        | k == key = value
        | k < key = clookup k arbs
        | k > key = clookup k arbd
    delete k Empty = Empty
    delete k (BNode arbs key value arbd)
        | k == key = BNode arbs key Nothing arbd
        | k < key = BNode (delete k arbs) key value arbd
        | k > key = BNode arbs key value (delete k arbd)
    toList Empty = []
    toList (BNode arbs key Nothing arbd) = toList arbs ++ toList arbd
    toList (BNode arbs key (Just value) arbd) = (key, value) : (toList arbs ++ toList arbd)