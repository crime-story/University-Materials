{-
class Functor f where
    fmap : : ( a -> b ) -> f a -> f b
-}
newtype Identity a = Identity a
    deriving Show

instance Functor Identity where
    fmap fct (Identity x1) = Identity (fct x1)

data Pair a = Pair a a
    deriving Show

instance Functor Pair where
    fmap fct (Pair x1 x2) = Pair (fct x1) (fct x2)

data Constant a b = Constant b
    deriving Show

instance Functor (Constant a) where
    fmap fct (Constant x1) = Constant (fct x1)

data Two a b = Two a b
    deriving Show

instance Functor (Two a) where
    fmap fct (Two x1 x2) = Two x1 (fct x2)

data Three a b c = Three a b c
    deriving Show

instance Functor (Three a b) where
    fmap fct (Three x1 x2 x3) = Three x1 x2 (fct x3)

data Three' a b = Three' a b b
    deriving Show   

instance Functor (Three' a) where
    fmap fct (Three' x1 x2 x3) = Three' x1 (fct x2) (fct x3)

data Four a b c d = Four a b c d
    deriving Show

instance Functor (Four a b c) where
    fmap fct (Four x1 x2 x3 x4) = Four x1 x2 x3 (fct x4)

data Four'' a b = Four'' a a a b
    deriving Show

instance Functor (Four'' a) where
    fmap fct (Four'' x1 x2 x3 x4) = Four'' x1 x2 x3 (fct x4)

data Quant a b = Finance | Desk a | Bloor b
    deriving Show

instance Functor (Quant a) where
    fmap fct (Finance) = Finance
    fmap fct (Desk x1) = Desk x1
    fmap fct (Bloor x1) = Bloor (fct x1) 

data LiftItOut f a = LiftItOut (f a)
    deriving Show

instance Functor f => Functor (LiftItOut f) where
    fmap fct (LiftItOut x1) = LiftItOut (fmap fct x1)

data Parappa f g a = DaWrappa (f a) (g a)
    deriving Show

instance (Functor f, Functor g) => Functor (Parappa f g) where
    fmap fct (DaWrappa x1 x2) = DaWrappa (fmap fct x1) (fmap fct x2)

data IgnoreOne f g a b = IgnoringSomething (f a) (g b)
    deriving Show

instance Functor g => Functor (IgnoreOne f g a) where 
    fmap fct (IgnoringSomething x1 x2) = IgnoringSomething x1 (fmap fct x2)

data Notorious g o a t = Notorious (g o) (g a) (g t)
    deriving Show

instance (Functor g) => Functor (Notorious g o a) where
    fmap fct (Notorious x1 x2 x3) = Notorious x1 x2 (fmap fct x3)

data GoatLord a = NoGoat | OneGoat a | MoreGoats (GoatLord a) (GoatLord a) (GoatLord a)
    deriving Show

instance Functor GoatLord where
    fmap fct NoGoat = NoGoat
    fmap fct (OneGoat x1) = OneGoat (fct x1)
    fmap fct (MoreGoats x1 x2 x3) = MoreGoats (fmap fct x1) (fmap fct x2) (fmap fct x3)

data TalkToMe a = Halt | Print String a | Read (String -> a)

instance Functor TalkToMe where
    fmap fct Halt = Halt
    fmap fct (Print x1 x2) = Print x1 (fct x2)
    fmap fct (Read f) = Read (fct.f)