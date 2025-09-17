
# Architecture comparisson

**Context:** An e-commerce website, that allows ordering of products, payments at checkout, handles dispatch and stock, emits invoices and handles errors that could appear throughout the lifecycle of the product. 

Compare how the application components integration the 3 examples, evaluating how the data flows, what needs to be developped to handle error cases and how the application fares in regards to data consistency. 

## Message Broker integration

```mermaid 

flowchart TD

%% Components
User([User])
EcommerceWeb[E-commerce Website]
OrderService[Order Service]
PaymentService[Payment Service]
LogisticsService[Logistics Service]
AccountingService[Accounting Service]

%% Message Broker
Broker[(Message Broker)]
OrderQueue>Order Queue]
PaymentQueue>Payment Queue]
ShipmentQueue>Shipment Queue]
AccountingQueue>Accounting Queue]

%% Additional Components for Production
RetryHandler[Retry Handler]
DLQ>Dead Letter Queue]
AuditService[Audit Service]

%% User Interaction
User --> EcommerceWeb
EcommerceWeb --> OrderService

%% Messaging Flow
OrderService -->|Publish Order| OrderQueue
OrderQueue --> Broker

Broker -->|Consume Order| PaymentService
PaymentService -->|Publish Payment Result| PaymentQueue
PaymentQueue --> Broker

Broker -->|Consume Payment Result| LogisticsService
Broker -->|Consume Payment Result| AccountingService

LogisticsService -->|Publish Shipment Info| ShipmentQueue
ShipmentQueue --> Broker

AccountingService -->|Publish Invoice Info| AccountingQueue
AccountingQueue --> Broker

%% Production Grade Functionalities
Broker --> RetryHandler
RetryHandler --> DLQ

OrderService --> AuditService
PaymentService --> AuditService
LogisticsService --> AuditService
AccountingService --> AuditService

%% Style
classDef queue fill:#ffd966,stroke:#e69138
classDef broker fill:#f4b183,stroke:#d07941
class OrderQueue,PaymentQueue,ShipmentQueue,AccountingQueue,DLQ queue
class Broker broker


```

## Webservice integration

```mermaid
flowchart TD

%% Components
User([User])
EcommerceWeb[E-commerce Website]
OrderService[Order Service]
PaymentService[Payment Service API]
LogisticsService[Logistics Service API]
AccountingService[Accounting Service API]

%% Extra Components for Robust REST API Integration
CircuitBreaker[Circuit Breaker]
RetryMechanism[Retry Mechanism]
AuditService[Audit Service]

%% User Interaction
User --> EcommerceWeb
EcommerceWeb --> OrderService

%% REST API Flow
OrderService -->|HTTP POST /orders| PaymentService
PaymentService -->|HTTP Response| OrderService

OrderService -->|HTTP POST /shipments| LogisticsService
LogisticsService -->|HTTP Response| OrderService

OrderService -->|HTTP POST /invoices| AccountingService
AccountingService -->|HTTP Response| OrderService

%% Production Grade Functionalities
OrderService --> CircuitBreaker
CircuitBreaker --> RetryMechanism

PaymentService --> AuditService
LogisticsService --> AuditService
AccountingService --> AuditService

%% Style
classDef api fill:#9fc5e8,stroke:#6fa8dc
class PaymentService,LogisticsService,AccountingService api
```

## Modular Monolith

```mermaid
flowchart TD

%% Components
User([User])

%% Internal Modules
OrderModule[Order Module]
PaymentModule[Payment Module]
LogisticsModule[Logistics Module]
AccountingModule[Accounting Module]

%% User Interaction
User --> Monolith

subgraph Monolith
    OrderModule
    PaymentModule
    LogisticsModule
    AccountingModule
end

%% Internal Function Calls
OrderModule --> PaymentModule
PaymentModule --> OrderModule

OrderModule --> LogisticsModule
LogisticsModule --> OrderModule

OrderModule --> AccountingModule
AccountingModule --> OrderModule

%% Highlight Differences
classDef monolith fill:#FFF,stroke:#a64d79
class Monolith monolith
```

