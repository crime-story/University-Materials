# Paranteze [(link infoarena)](https://www.infoarena.ro/problema/paranteze)
### Ţirbi tocmai a învăţat la un curs de Silverlight despre paranteze rotunde "(, )", drepte "[, ]" şi acolade "{, }". Un şir este parantezat corect dacă este construit conform regulilor:
    <şir parantezat corect> = <şirul vid>
    <şir parantezat corect> = "(" + <şir parantezat corect> + ")"
    <şir parantezat corect> = "[" + <şir parantezat corect> + "]"
    <şir parantezat corect> = "{" + <şir parantezat corect> + "}"
    <şir parantezat corect> = <şir parantezat corect> + <şir parantezat corect>

## Cerinţă
### Ca la orice curs, se dau teme de casă, iar Ţirbi a primit următoarea problemă: Având un şir cu N paranteze, să se afle lungimea maximă a unei secvenţe parantezată corect.

## Date de intrare
### Fişierul de intrare paranteze.in conţine pe prima linie numărul natural N, iar pe următoarea linie un şir cu N paranteze.

## Date de ieşire
### Fişierul de ieşire paranteze.out conţine lungimea maximă a unei secvenţe corect parantezată.

## Restricţii şi precizări:
    1 ≤ N ≤ 1 000 000
    Pentru 50% din teste N ≤ 1 000