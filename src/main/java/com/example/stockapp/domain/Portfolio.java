package com.example.stockapp.domain;


import com.example.stockapp.dto.*;

import java.util.*;
import java.util.function.Function;

/**
 * Immutable snapshot of a user's positions & P/L.
 */
public final class Portfolio {

    private final Map<String, Position> map = new HashMap<>();
    private final List<StockPurchaseDTO> purchases;

    /** Builds portfolio from the list of historical purchases. */
    public Portfolio(List<StockPurchaseDTO> purchases) {
        // aggregate by symbol
        for (StockPurchaseDTO p : purchases) {
            map.merge(
                    p.symbol(),
                    new Position(p.symbol(), p.quantity(), p.quantity() * p.price()),
                    (a, b) -> new Position(
                            a.symbol(),
                            a.quantity() + b.quantity(),
                            a.totalCost() + b.totalCost())
            );
        }

        this.purchases = purchases;
    }

    /** Position value-object */
    public record Position(String symbol, int quantity, double totalCost) {}

    /** All positions (one per symbol). */
    public Collection<Position> positions() { return map.values(); }

    /**
     * Calculates total gain / loss for the portfolio.
     *
     * @param quoteFn function that returns the *current* market price for a symbol
     */
    public double totalGainLoss(double quoteFn) {

        double total = 0.0;
        for (StockPurchaseDTO purchase : this.purchases) {
            total += (quoteFn - purchase.price()) * purchase.quantity();
        }
        return total;
    }
}