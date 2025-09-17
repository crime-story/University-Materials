## Context

Identify design flaws and refactor the implementation using interfaces and abstractions to promote flexibility, reusability, and clarity.

## Exercise 1 – Payment System: Dynamic Processing

You are given a basic payment system with hardcoded logic that directly creates specific payment processor classes and uses conditional logic for processing. It lacks abstraction, extensibility, and testability. Your task is to redesign the system using a common interface for all payment processors, and eliminate the need for conditionals in business logic.

### Code to Analyze

```java
public class PaymentProcessorService {

    public void process(String method, double amount) {
        if (method.equalsIgnoreCase("paypal")) {
            PaypalProcessor paypal = new PaypalProcessor();
            paypal.sendPayment(amount);
        } else if (method.equalsIgnoreCase("credit")) {
            CreditCardProcessor credit = new CreditCardProcessor();
            credit.chargeCard(amount);
        } else if (method.equalsIgnoreCase("crypto")) {
            CryptoProcessor crypto = new CryptoProcessor();
            crypto.transferTokens(amount);
        } else {
            throw new IllegalArgumentException("Unsupported payment method");
        }
    }
}

class PaypalProcessor {
    public void sendPayment(double amount) {
        System.out.println("Processing $" + amount + " through PayPal.");
    }
}

class CreditCardProcessor {
    public void chargeCard(double amount) {
        System.out.println("Charging $" + amount + " to credit card.");
    }
}

class CryptoProcessor {
    public void transferTokens(double amount) {
        System.out.println("Transferring tokens worth $" + amount + " through crypto wallet.");
    }
}
````

## Exercise 2 – Shape Models: Unified Area and Perimeter

Restructure the code into a polymorphic model where shapes define their own behavior and follow a common contract.

### Code to Analyze

```java
public class GeometryCalculator {

    public static double computeArea(String type, double... params) {
        switch (type) {
            case "rectangle":
                return params[0] * params[1];
            case "circle":
                return Math.PI * params[0] * params[0];
            case "triangle":
                return 0.5 * params[0] * params[1];
            case "square":
                return params[0] * params[0];
            default:
                throw new IllegalArgumentException("Unknown shape type");
        }
    }

    public static double computePerimeter(String type, double... params) {
        switch (type) {
            case "rectangle":
                return 2 * (params[0] + params[1]);
            case "circle":
                return 2 * Math.PI * params[0];
            case "triangle":
                return params[0] + params[1] + params[2];
            case "square":
                return 4 * params[0];
            default:
                throw new IllegalArgumentException("Unknown shape type");
        }
    }
}
```

## Exercise 3 – Pokémon Battle System: Interface-Based Abilities

Given an object model that simulates Pokémon abilities, where all behaviors are defined directly in the concrete classes (as a result, unrelated abilities are present in Pokémon that shouldn't have them), extract interfaces representing distinct abilities and use composition or interface inheritance to restructure the model into a flexible and scalable design.

### Code to Analyze

```java
public class Pikachu {
    public void attack() {
        System.out.println("Pikachu uses Thunderbolt!");
    }

    public void swim() {
        System.out.println("Pikachu swims across the river.");
    }

    public void fly() {
        System.out.println("Pikachu tries to fly... and falls.");
    }
}

public class Charizard {
    public void attack() {
        System.out.println("Charizard uses Flamethrower!");
    }

    public void swim() {
        System.out.println("Charizard sinks in water.");
    }

    public void fly() {
        System.out.println("Charizard flies high into the sky.");
    }
}

public class Squirtle {
    public void attack() {
        System.out.println("Squirtle uses Water Gun!");
    }

    public void swim() {
        System.out.println("Squirtle swims skillfully.");
    }

    public void fly() {
        System.out.println("Squirtle can't fly.");
    }
}
```
