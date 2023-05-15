package com.example.signup;

import java.sql.*;

public class RoleUtils {
    public static String getUserRole(String username) {
        String role = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vvdata", "vvapp", "vvapp123");
             PreparedStatement statement = connection.prepareStatement("SELECT role FROM roles WHERE username = ?")) {

            // Paraméter beállítása
            statement.setString(1, username);

            // Lekérdezés végrehajtása
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hiba kezelése
        }

        return role;
    }
}
