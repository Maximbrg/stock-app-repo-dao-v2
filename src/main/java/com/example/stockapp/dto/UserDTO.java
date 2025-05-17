package com.example.stockapp.dto;

/**
 * Data Transfer Object representing an application user.
 * Immutable and serializable; contains no behaviour.
 */
public record UserDTO(Integer id, String name) {}