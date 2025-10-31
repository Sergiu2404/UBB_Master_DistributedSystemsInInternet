
# Understanding `jakarta.persistence.fetchgraph` in Jakarta Persistence (JPA)

## Overview

In Jakarta Persistence (JPA), `jakarta.persistence.fetchgraph` is a powerful hint that enables selective eager loading of entity attributes. By defining a "fetch graph," developers can specify which attributes should be loaded eagerly, while the remaining attributes follow their default fetching strategy. This approach allows for optimized performance, fetching only the necessary data for specific operations without altering the entity's default configuration.

### Key Concepts of `fetchgraph`

- **Selective Eager Loading**: Allows specific attributes to be fetched eagerly without overriding the entity’s default lazy or eager loading settings.
- **Performance Optimization**: Helps reduce database load and memory usage by only loading essential attributes.

## How to Use `fetchgraph`

1. **Define an Entity Graph**: Create an entity graph to indicate the attributes to load eagerly.
2. **Apply the Fetch Graph**: When retrieving the entity, apply the `fetchgraph` hint to use this graph during the fetch.

---

## Example Usage

Consider an example where we have an entity `Order` with a `OneToMany` relationship to `OrderDetails`. By default, `OrderDetails` might be lazily loaded, but for specific scenarios, we may want to eagerly load just the `OrderDetails`.

### Step 1: Define the Entity Graph

```java
@Entity
public class Order {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails;

    // Other fields, getters, and setters
}
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityGraph;
import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private EntityManager entityManager;

    public OrderService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Order getOrderWithDetails(Long orderId) {
        EntityGraph<Order> graph = entityManager.createEntityGraph(Order.class);
        graph.addAttributeNodes("orderDetails"); // Specify orderDetails to load eagerly

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", graph);

        return entityManager.find(Order.class, orderId, properties);
    }
}
