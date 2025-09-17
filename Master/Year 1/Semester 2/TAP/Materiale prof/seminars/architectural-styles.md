## exercise 1

Design a platform for a large hotel chain that includes:

- Room reservations

- Payment processing

- Guest management (profiles, history)

- Loyalty and rewards program

- Housekeeping and maintenance

Tasks:

- Clearly identify and name at least three bounded contexts from the scenario.
- For each bounded context, define the core domain language (ubiquitous language) and relevant domain entities or aggregates.
- Draw a simple context map that illustrates relationships between the bounded contexts, marking explicit relationships (customer/supplier, anti-corruption layer, shared kernel, etc.).

## excercise 2

Your e-commerce platform integrates with legacy inventory and pricing systems. These external systems have their own data models and terminology.

Tasks:

- Clearly describe how you would use an Anti-Corruption Layer to protect your domain from external complexity.
- Clearly state what risks or complexities the ACL explicitly mitigates.

## exercise 3

You are designing an IoT-based agricultural monitoring system tracking sensors that measure temperature, humidity, soil quality, and equipment health across farms in real-time.

Tasks:

- Clearly describe the architecture needed to support real-time data ingestion and analytics, considering scalability and latency requirements.
- Provide a simple diagram clearly showing your real-time architecture and data flow from sensors to analytics dashboard.