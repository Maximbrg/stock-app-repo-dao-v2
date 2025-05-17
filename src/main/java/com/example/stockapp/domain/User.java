package com.example.stockapp.domain;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import com.example.stockapp.dto.*;

public class User {
    protected final int userId;
    private final StockRepository repo;   // concrete impl injected
    protected Portfolio port;

    public User(int userId, StockRepository repo) {
        this.userId = userId;
        this.repo   = repo;
        this.port   = null;
    }


    public List<StockPurchaseDTO> purchases() throws SQLException {   // raw deals
        return repo.findPurchasesByUser(userId);
    }

    /** Higher-level helper: how many distinct tickers? */
    public long distinctSymbols() throws SQLException {
        return purchases().stream().map(StockPurchaseDTO::symbol).distinct().count();
    }

    /** Delegates to Portfolio business object (see below). */
    public void portfolio() throws Exception {
        this.port =  new Portfolio(purchases());
    }

}