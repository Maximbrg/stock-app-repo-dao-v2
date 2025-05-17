package com.example.stockapp.presentation;

import com.example.stockapp.dto.*;
import com.example.stockapp.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class MainCLI {
    private static final Logger log = LoggerFactory.getLogger(MainCLI.class);

    private static final String BANNER = """
        ==========================================
           Stock Buyer (repository + DAO demo)
        ==========================================
        Commands:
          new   - create user
          buy   - buy a stock
          ls    - list users with their purchases
          q     - quit
        ==========================================
        """;

    public static void main(String[] args) {
//        var repo = new StockRepositoryImpl(new JdbcUserDAO(), new JdbcPurchaseDAO());
        var service = new StockService();
        Scanner in = new Scanner(System.in);

        System.out.println(BANNER);
        while (true) {
            System.out.print("Command [new | buy | ls | pf | q] : ");
            String cmd = in.nextLine().trim().toLowerCase();
            log.info("Received command '{}'", cmd);

            try {
                switch (cmd) {
                    case "new" -> {
                        System.out.print("User name              : ");
                        String name = in.nextLine().trim();
                        var user = service.createUser(name);
                        System.out.printf("✅  Created user #%d (%s)%n%n", user.id(), user.name());
                    }
                    case "buy" -> {
                        System.out.print("User ID                : ");
                        int uid = Integer.parseInt(in.nextLine().trim());
                        System.out.print("Stock symbol (e.g. AAPL): ");
                        String sym = in.nextLine().trim().toUpperCase();
                        System.out.print("Quantity               : ");
                        int qty = Integer.parseInt(in.nextLine().trim());

                        var p = service.buyStock(uid, sym, qty);
                        System.out.printf(
                                "💰  Recorded purchase #%d – %d × %s @ %.2f for user %d%n%n",
                                p.id(), p.quantity(), p.symbol(), p.price(), p.userId());
                    }
                    case "ls" -> {
                        listUsersAndPurchases(service);
                    }
                    case "pf" -> {
                        System.out.print("User ID : ");
                        int uid = Integer.parseInt(in.nextLine().trim());


                        System.out.printf("Total Gain/Loss = %.2f%n%n",
                                service.totalGains(uid));

                    }
                    case "q" -> {
                        log.info("Exiting CLI");
                        return;
                    }
                    default -> System.out.println("Unknown command.\n");
                }
            } catch (Exception e) {
                log.error("Error handling command '" + cmd + "'", e);
                System.out.println("⚠️  " + e.getMessage() + "\n");
            }
        }
    }

    private static void listUsersAndPurchases(StockService service) throws Exception {
        List<UserDTO> users = service.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.\n");
            return;
        }
        for (UserDTO u : users) {
            System.out.printf("User #%d (%s)%n", u.id(), u.name());
            List<StockPurchaseDTO> purchases = service.getPurchasesForUser(u.id());
            if (purchases.isEmpty()) {
                System.out.println("  (no purchases)");
            } else {
                for (StockPurchaseDTO p : purchases) {
                    System.out.printf("  • %d × %s  @ %.2f  on %s%n",
                            p.quantity(), p.symbol(), p.price(), p.purchasedAt());
                }
            }
            System.out.println();
        }
    }
}
