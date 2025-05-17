package com.example.stockapp.domain;


import com.example.stockapp.dto.StockPurchaseDTO;
import com.example.stockapp.dto.UserDTO;
import com.example.stockapp.presentation.MainCLI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ControllerFacade {

    private final StockRepository repo;
    private static final Logger log = LoggerFactory.getLogger(ControllerFacade.class);

    public ControllerFacade() {
        this.repo  = new StockRepositoryImpl();
    }

    public UserDTO createUser(String name) throws SQLException {
        log.debug("createUser('{}')", name);
        return this.repo.createUser(name);
    }

    public List<UserDTO> findAllUsers() throws SQLException {
        log.debug("findAllUsers()");
        return this.repo.findAllUsers();
    }

    public Optional<UserDTO> findUser(int id) throws SQLException {
        log.debug("findUser({})", id);
        return this.repo.findUser(id);
    }

    public StockPurchaseDTO recordPurchase(StockPurchaseDTO purchase) throws SQLException {
        log.debug("recordPurchase({})", purchase);
        return this.repo.recordPurchase(purchase);
    }

    public List<StockPurchaseDTO> findPurchasesByUser(int userId) throws SQLException {
        log.debug("findPurchasesByUser({})", userId);
        return this.repo.findPurchasesByUser(userId);
    }

    public double createUserAndCalcPL(int userId,
                                      double selling_price) throws Exception {


        return this.repo.createUserAndCalcPL(userId, selling_price);

    }
}
