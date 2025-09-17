# Specificații formale

[Specificații formale](https://en.wikipedia.org/wiki/Formal_specification)

## Specificații algebrice

[Specificații algebrice](https://en.wikipedia.org/wiki/Algebraic_specification)

- [OBJ](https://en.wikipedia.org/wiki/OBJ_(programming_language))
- [Maude](http://maude.cs.illinois.edu/w/index.php/The_Maude_System)
- [CASL](https://www.informatik.uni-bremen.de/agbkb/forschung/formal_methods/CoFI/CASL/casl.pdf)
- [K framework](https://kframework.org/)

### Exemplu: Tonomat de cafea

Diagrame UML de clasă și de secvență

```mermaid
%%{init: {'theme':'neutral'}}%%
classDiagram
  User o-- Wallet
  class Credit{
    -Num balance
    +Credit(amount)
    +value() Num
  }
  class Selection{
    +Selection(coffeeType)
  }
  class Coffee{
    +Coffee(coffeeType)
  }
  class CoffeeType{
    +Num price
    +price() Num
  }
  class VM{
    +start(Init)
    +noCoffees() Num
    +totalPrice() Num
  }
  VM o-- Credit
  VM o-- Selection
  VM o-- Coffee
  Coffee o-- CoffeeType
  Selection o-- CoffeeType
```

```mermaid
%%{init: {'theme':'neutral'}}%%
graph
  VM[Init : VM]
  none[none : CoffeeType]
  espresso[espresso : CoffeeType]
  americano[americano : CoffeeType]
```

```mermaid 
sequenceDiagram
 
    actor user
    participant VM
    
    VM ->> VM: start
    activate user
    loop as many times as needed
        user ->> +VM: select
        deactivate user
        activate VM
        VM ->> user: selected
        deactivate VM
        activate user
        loop as many times as needed
            user ->> VM: addBill
            deactivate user
            activate VM 
            VM ->> user: wallet--
            deactivate VM
            activate user
        end
        user ->> VM: pay
        deactivate user
        activate VM 
        VM ->> VM: brew
        VM ->> user: giveChange
        deactivate VM
        activate user
        user ->> user: gulp!
        deactivate user
    end
    
``` 




