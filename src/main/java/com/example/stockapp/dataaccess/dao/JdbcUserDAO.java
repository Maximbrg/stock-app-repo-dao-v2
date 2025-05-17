package com.example.stockapp.dataaccess.dao;

import com.example.stockapp.dto.*;
import com.example.stockapp.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserDAO implements UserDAO {

    @Override
    public Optional<UserDTO> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM users WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(new UserDTO(rs.getInt("id"), rs.getString("name")))
                        : Optional.empty();
            }
        }
    }

    @Override
    public List<UserDTO> findAll() throws SQLException {
        String sql = "SELECT id, name FROM users ORDER BY id";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<UserDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new UserDTO(rs.getInt("id"), rs.getString("name")));
            }
            return list;
        }
    }

    @Override
    public UserDTO save(UserDTO user) throws SQLException {
        if (user.id() == null) {
            String sql = "INSERT INTO users(name) VALUES(?)";
            try (PreparedStatement ps = Database.getConnection()
                                               .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.name());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return new UserDTO(keys.getInt(1), user.name());
                }
            }
        } else {
            String sql = "UPDATE users SET name = ? WHERE id = ?";
            try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
                ps.setString(1, user.name());
                ps.setInt(2, user.id());
                ps.executeUpdate();
                return user;
            }
        }
    }
}
