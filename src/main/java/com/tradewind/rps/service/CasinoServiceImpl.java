package com.tradewind.rps.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to manage casino state.
 */
@Service
public class CasinoServiceImpl {

    private final AtomicInteger balance = new AtomicInteger(0);

    /**
     * Updates the balance in the casino
     * @param change Amount to change the balance. Can be positive or negative.
     * @return The new balance
     */
    public int updateBalance(int change) {
        int prev, next;
        do {
            prev = balance.get();
            next = prev + change;
        } while (!balance.compareAndSet(prev, next));
        return next;
    }

    /**
     * Returns the current balance in the casino
     */
    public int getBalance() {
        return balance.get();
    }

    /**
     * Sets casino balance to zero.
     */
    public void resetBalance() {
        this.balance.getAndSet(0);
    }
}
