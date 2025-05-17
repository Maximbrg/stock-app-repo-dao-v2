package com.example.stockapp.dto;

import java.time.Instant;

/**
 * Data Transfer Object representing a single stock purchase transaction.
 */
public record StockPurchaseDTO(
        Integer id,
        Integer userId,
        String symbol,
        int quantity,
        double price,
        Instant purchasedAt
) {}