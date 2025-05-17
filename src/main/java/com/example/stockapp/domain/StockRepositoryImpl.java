package com.example.stockapp.domain;

import com.example.stockapp.dataaccess.dao.JdbcPurchaseDAO;
import com.example.stockapp.dataaccess.dao.JdbcUserDAO;
import com.example.stockapp.dataaccess.dao.PurchaseDAO;
import com.example.stockapp.dataaccess.dao.UserDAO;

import com.example.stockapp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StockRepositoryImpl implements StockRepository {
    private static final Logger log = LoggerFactory.getLogger(StockRepositoryImpl.class);
    private final UserDAO userDAO;
    private final PurchaseDAO purchaseDAO;

    public StockRepositoryImpl() {
        this.userDAO = new JdbcUserDAO();
        this.purchaseDAO = new JdbcPurchaseDAO();
    }

    @Override
    public UserDTO createUser(String name) throws SQLException {
        log.debug("createUser('{}')", name);
        return userDAO.save(new UserDTO(null, name));
    }

    @Override
    public Optional<UserDTO> findUser(int id) throws SQLException {
        log.debug("findUser({})", id);
        return userDAO.findById(id);
    }

    @Override
    public List<UserDTO> findAllUsers() throws SQLException {
        log.debug("findAllUsers()");
        return userDAO.findAll();
    }

    @Override
    public StockPurchaseDTO recordPurchase(StockPurchaseDTO purchase) throws SQLException {
        log.debug("recordPurchase({})", purchase);
        return purchaseDAO.save(purchase);
    }

    @Override
    public List<StockPurchaseDTO> findPurchasesByUser(int userId) throws SQLException {
        log.debug("findPurchasesByUser({})", userId);
        return purchaseDAO.findByUserId(userId);
    }

    // src/main/java/com/example/stockapp/repository/StockRepositoryImpl.java
    @Override
    public double createUserAndCalcPL(int userId,
                                      double selling_price) throws Exception {

        // 1) create the user
        User user = new User(userId, this);
        user.portfolio();
        log.debug("Created user {}", user);

        // 2) portfolio for that (brand-new) user
        List<StockPurchaseDTO> list = user.purchases();

        // no purchases yet? gain/loss = 0
        if (list.isEmpty()) return 0.0;

        return user.port.totalGainLoss(50);

    }

}
