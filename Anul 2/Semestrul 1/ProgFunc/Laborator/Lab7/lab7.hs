data Fruct
     = Mar String Bool
     | Portocala String Int

ionatanFaraVierme = Mar "Ionatan" False
goldenCuVierme = Mar "Golden Delicious" True
portocalaSicilia10 = Portocala "Sanguinello" 10

listaFructe = [Mar "Ionatan" False,
  Portocala "Sanguinello" 10,
  Portocala "Valencia" 22,
  Mar "Golden Delicious" True,
  Portocala "Sanguinello" 15,
  Portocala "Moro" 12,
  Portocala "Tarocco" 3,
  Portocala "Moro" 12,
  Portocala "Valencia" 2,
  Mar "Golden Delicious" False,
  Mar "Golden" False,
  Mar "Golden" True]

soiuriS = ["Tarocco", "Moro" , "Sanguinello"]

ePortocalaDeSicilia :: Fruct -> Bool
ePortocalaDeSicilia x = case x of 
   Portocala s i -> elem s soiuriS 
   Mar _ _ -> False 

ePortocala (Portocala s i) = True 
ePortocala (Mar _ _ ) = False 

ePortocalaDeSicilia2 :: Fruct -> Bool
ePortocalaDeSicilia2 (Portocala s _ ) = elem s soiuriS 
ePortocalaDeSicilia2 (Mar _ _ ) = False 

nrFeliiSicilia :: [Fruct] -> Int
nrFeliiSicilia [] = 0 
nrFeliiSicilia (Portocala s i : lfructe)  = if elem s soiuriS then i + nrFeliiSicilia lfructe else nrFeliiSicilia lfructe
nrFeliiSicilia (Mar _ _ : lfructe) = nrFeliiSicilia lfructe 

nrFeliiSicilia1 :: [Fruct] -> Int 
nrFeliiSicilia1 lista = sum [ i | Portocala s i <- lista, elem s soiuriS]

nrFeliiSicilia2 lista = foldr (+) 0 ( map (\ (Portocala s i ) -> i ) (filter ePortocalaDeSicilia lista) )

-- c)
nrMereViermi :: [Fruct] -> Int
nrMereViermi lista = length[i | Mar s i <- lista, i]

nrMereViermi2 :: [Fruct] -> Int
nrMereViermi2 lista = sum[1 | Mar s i <- lista, i]

-- Ex2
type NumeA = String
type Rasa = String
data Animal = Pisica NumeA | Caine NumeA Rasa
    deriving Show

-- a)
vorbeste :: Animal -> String
vorbeste x = case x of
            (Pisica s) -> "Meow"
            (Caine s _) -> "Woof"

vorbeste2 :: Animal -> String
vorbeste2 (Pisica s) = "Meow"
vorbeste2 (Caine s _) = "Woof"

-- b)
-- data Maybe a = Nothing | Just a
rasa :: Animal -> Maybe String
rasa x = case x of
        (Pisica _) -> Nothing
        (Caine _ r) -> Just r

rasa2 :: Animal -> Maybe String
rasa2 (Pisica _) = Nothing
rasa2 (Caine _ r) = Just r

-- Ex 3
data Linie = L [Int]
    deriving Show
data Matrice = M [Linie]
    deriving Show

-- a)
verifica :: Matrice -> Int -> Bool
verifica (M matrice) n = foldr (&&) True (map (\(L x) -> sum x == n) matrice)

-- b)
doarPozN :: Matrice -> Int -> Bool
doarPozN (M matrice) n = foldr (&&) True (map (\(L x) -> length x == length(filter (\y -> y >= 0) x)) (filter (\(L x) -> length x == n) matrice))

-- c)
liniiEgale :: [Int] -> Bool
liniiEgale [x] = True
liniiEgale (x:y:list)
  | x == y = liniiEgale (y:list)
  | otherwise = False
-- 1 2 3 4 5
-- verifica 1 2
-- dupa ramanea 3 4 5
-- verifica 3 4
-- ramanea 5
corect :: Matrice -> Bool
corect (M matrice) = liniiEgale(map (\(L x) -> length x) matrice)

-- Varianta cu foldr
corect3 :: Matrice -> Bool
corect3 (M [] ) = True
corect3 (M ((L l1): m))  = foldr (&&) True ( map (\(L x) -> length x == length l1) m)