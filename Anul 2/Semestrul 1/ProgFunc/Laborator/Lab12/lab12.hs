{-
class Functor f where
    fmap : : ( a -> b ) -> f a -> f b
-}
newtype Identity a = Identity a
    deriving Show

instance Functor Identity where
    fmap fct (Identity x) = Identity (fct x)

data Pair a = Pair a a
    deriving Show

instance Functor Pair where
    fmap fct (Pair x y) = Pair (fct x) (fct y)

data Constant a b = Constant b
    deriving Show

instance Functor (Constant a) where
    fmap fct (Constant x) = Constant (fct x)

data Two a b = Two a b
    deriving Show

instance Functor (Two a) where
    fmap fct (Two x y) = Two x (fct y)

data Three a b c = Three a b c
    deriving Show

instance Functor (Three a b) where
    fmap fct (Three x y z) = Three x y (fct z)

data Three' a b = Three' a b b
    deriving Show   

instance Functor (Three' a) where
    fmap fct (Three' x y z) = Three' x (fct y) (fct z)

data Four a b c d = Four a b c d
    deriving Show

instance Functor (Four a b c) where
    fmap fct (Four x y z w) = Four x y z (fct w)

data Four'' a b = Four'' a a a b
    deriving Show

instance Functor (Four'' a) where
    fmap fct (Four'' x y z w) = Four'' x y z (fct w)

data Quant a b = Finance | Desk a | Bloor b
    deriving Show

instance Functor (Quant a) where
    fmap fct (Finance) = Finance
    fmap fct (Desk x) = Desk x
    fmap fct (Bloor x) = Bloor (fct x) 

data LiftItOut f a = LiftItOut (f a)
    deriving Show

instance Functor f => Functor (LiftItOut f) where
    fmap fct (LiftItOut x) = LiftItOut (fmap fct x)

data Parappa f g a = DaWrappa (f a) (g a)
    deriving Show

instance (Functor f, Functor g) => Functor (Parappa f g) where
    fmap fct (DaWrappa x y) = DaWrappa (fmap fct x) (fmap fct y)

data IgnoreOne f g a b = IgnoringSomething (f a) (g b)
    deriving Show

instance Functor g => Functor (IgnoreOne f g a) where 
    fmap fct (IgnoringSomething x y) = IgnoringSomething x (fmap fct y)

data Notorious g o a t = Notorious (g o) (g a) (g t)
    deriving Show

instance (Functor g) => Functor (Notorious g o a) where
    fmap fct (Notorious x y z) = Notorious x y (fmap fct z)

data GoatLord a = NoGoat | OneGoat a | MoreGoats (GoatLord a) (GoatLord a) (GoatLord a)
    deriving Show

instance Functor GoatLord where
    fmap fct NoGoat = NoGoat
    fmap fct (OneGoat x) = OneGoat (fct x)
    fmap fct (MoreGoats x y z) = MoreGoats (fmap fct x) (fmap fct y) (fmap fct z)

data TalkToMe a = Halt | Print String a | Read (String -> a)

instance Functor TalkToMe where
    fmap fct Halt = Halt
    fmap fct (Print x y) = Print x (fct y)
    fmap fct (Read f) = Read (fct.f)