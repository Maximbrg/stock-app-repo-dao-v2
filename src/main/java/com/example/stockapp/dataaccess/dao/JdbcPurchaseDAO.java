package com.example.stockapp.dataaccess.dao;


import com.example.stockapp.dto.StockPurchaseDTO;
import com.example.stockapp.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.stockapp.dto.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class JdbcPurchaseDAO implements PurchaseDAO {
    private static final Logger log = LoggerFactory.getLogger(JdbcPurchaseDAO.class);

    @Override
    public StockPurchaseDTO save(StockPurchaseDTO p) throws SQLException {
        if (p.id() == null) {
            log.info("Recording new purchase {}", p);
            String sql = """
                INSERT INTO purchases(user_id, symbol, quantity, price, purchased_at)
                VALUES(?,?,?,?,?)
                """;
            try (PreparedStatement ps = Database.getConnection()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.userId());
                ps.setString(2, p.symbol());
                ps.setInt(3, p.quantity());
                ps.setDouble(4, p.price());
                ps.setString(5, p.purchasedAt().toString());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    StockPurchaseDTO saved = new StockPurchaseDTO(
                            keys.getInt(1),
                            p.userId(),
                            p.symbol(),
                            p.quantity(),
                            p.price(),
                            p.purchasedAt());
                    log.debug("Inserted purchase {}", saved);
                    return saved;
                }
            }
        } else {
            return p;
        }
    }

    @Override
    public List<StockPurchaseDTO> findByUserId(int userId) throws SQLException {
        log.debug("findByUserId({})", userId);
        String sql = "SELECT * FROM purchases WHERE user_id = ? ORDER BY id";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<StockPurchaseDTO> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new StockPurchaseDTO(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("symbol"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            Instant.parse(rs.getString("purchased_at"))
                    ));
                }
                log.trace("findByUserId({}) -> {} purchases", userId, list.size());
                return list;
            }
        }
    }
}
