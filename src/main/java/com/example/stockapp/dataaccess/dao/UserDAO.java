package com.example.stockapp.dataaccess.dao;



import com.example.stockapp.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<UserDTO> findById(int id) throws SQLException;
    List<UserDTO> findAll() throws SQLException;
    UserDTO save(UserDTO user) throws SQLException;
}
