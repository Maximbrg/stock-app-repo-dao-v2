package com.example.stockapp.dataaccess.dao;

import com.example.stockapp.dto.*;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseDAO {
    StockPurchaseDTO save(StockPurchaseDTO purchase) throws SQLException;
    List<StockPurchaseDTO> findByUserId(int userId) throws SQLException;
}
