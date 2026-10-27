# FleetHealth Architecture

## Backend Architecture

```
Controller → Service → Repository → Spring Data JPA → MySQL
```

## Package Structure

```
com.fleethealth
├── controller    → REST API endpoints
├── service       → Business logic & rule engine
├── repository    → Spring Data JPA interfaces
├── entity        → JPA entity classes
├── dto           → Data Transfer Objects
└── exception     → Custom exceptions & global handler
```

## Maintenance Rule Engine

The rule engine evaluates vehicle health data and generates alerts:

| Condition                       | Severity | Message                            |
|---------------------------------|----------|------------------------------------|
| Oil life < 10%                  | HIGH     | Oil life below 10%                 |
| Tire pressure < 30 PSI         | MEDIUM   | Tire pressure below 30 PSI        |
| Engine temperature > 100°C      | HIGH     | Engine temperature is high         |
| DTC code exists                 | HIGH     | Diagnostic trouble code detected   |
