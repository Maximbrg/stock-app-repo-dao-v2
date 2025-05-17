package com.example.stockapp.service;


import com.example.stockapp.domain.ControllerFacade;
import com.example.stockapp.domain.StockRepository;
import com.example.stockapp.domain.StockRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.stockapp.dto.*;

import java.net.http.*;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class StockService {
    private static final Logger log = LoggerFactory.getLogger(StockService.class);
    private final ControllerFacade cntr;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    public StockService() { this.cntr = new ControllerFacade(); }

    public UserDTO createUser(String name) throws Exception {
        log.info("createUser '{}'", name);
        return cntr.createUser(name);
    }

    public List<UserDTO> getAllUsers() throws Exception {
        log.info("getAllUsers()");
        return cntr.findAllUsers();
    }

    public StockPurchaseDTO buyStock(int userId, String symbol, int qty) throws Exception {
        log.info("buyStock user={} symbol={} qty={}", userId, symbol, qty);
        Optional<UserDTO> userOpt = cntr.findUser(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User %d does not exist".formatted(userId));
        }

//        double price = fetchLatestPrice(symbol);
//        StockPurchase purchase = new StockPurchase(
//                null, userId, symbol.toUpperCase(), qty, price, Instant.now());

                double price = 100;
        StockPurchaseDTO purchase = new StockPurchaseDTO(
                null, userId, symbol.toUpperCase(), qty, price, Instant.now());

        return cntr.recordPurchase(purchase);
    }

    public List<StockPurchaseDTO> getPurchasesForUser(int userId) throws Exception {
        log.info("getPurchasesForUser {}", userId);
        return cntr.findPurchasesByUser(userId);
    }

    public double totalGains(int userId) throws Exception {
        log.info("calculateTotalGains {}", userId);
        return cntr.createUserAndCalcPL(userId, 100);
    }

    private double fetchLatestPrice(String symbol) throws Exception {
        String url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=" + symbol;
        log.debug("Fetching price from {}", url);
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("User-Agent", "Java HttpClient")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new RuntimeException("HTTP " + res.statusCode());
        }

        JsonNode root = json.readTree(res.body());
        JsonNode priceNode = root.at("/quoteResponse/result/0/regularMarketPrice");

        if (priceNode.isMissingNode() || priceNode.isNull()) {
            throw new RuntimeException("Price not found");
        }
//        double price = priceNode.asDouble();
        double price = 100;
        log.debug("Price for {} is {}", symbol, price);
        return price;
    }
}
