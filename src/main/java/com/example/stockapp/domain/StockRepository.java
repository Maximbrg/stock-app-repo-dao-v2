package com.example.stockapp.domain;



import com.example.stockapp.dto.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StockRepository {
    UserDTO createUser(String name) throws SQLException;
    Optional<UserDTO> findUser(int id) throws SQLException;
    List<UserDTO> findAllUsers() throws SQLException;
    StockPurchaseDTO recordPurchase(StockPurchaseDTO purchase) throws SQLException;
    List<StockPurchaseDTO> findPurchasesByUser(int userId) throws SQLException;
    public double createUserAndCalcPL(int userId, double selling_price) throws Exception;
}
