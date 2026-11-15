package com.example.compute_resource_scheduler.transaction.deadlock_management;

import java.util.*;

public class WaitForGraph {
    private final Map<Long, Set<Long>> edges = new HashMap<>();

    public void addWaitEdges(long waiter, Set<Long> holders) {
        Set<Long> waitsFor = new HashSet<>(holders);
        waitsFor.remove(waiter);
        edges.put(waiter, waitsFor);
    }

    public void removeWaitEdges(long waiter) { // when lock granted or transaction aborts
        this.edges.remove(waiter);
    }

    public void removeTransaction(long transactionId) {
        edges.remove(transactionId);

        for (Set<Long> waitsFor : edges.values()) {
            waitsFor.remove(transactionId);
        }
    }

    public List<Long> findCycleFrom(long start) {
        List<Long> path = new ArrayList<>();

        if (dfs(start, start, new HashSet<>(), path)) {
            return path;
        }

        return List.of();
    }

    private boolean dfs(long current, long start, Set<Long> visited, List<Long> path) {
        path.add(current);
        visited.add(current);

        for (long next : edges.getOrDefault(current, Set.of())) {
            if (next == start) {
                return true;
            }
            if (!visited.contains(next) && dfs(next, start, visited, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }
}
