package com.iot.homeautomation.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.*;

@RestController
@CrossOrigin(origins = "*")   // 🔥 Allow all clients (mobile, dashboard)
public class DbDeviceController {

    private Connection con;

    // 🔥 DB CONNECTION (PostgreSQL)
    public DbDeviceController() {
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(
                "jdbc:postgresql://dpg-d7b6v98ule4c738sp9e0-a.oregon-postgres.render.com:5432/iot_db_veg6",
                "iotdb_gz1n_user",
                "iVuN78UhB0AwMGhhajDTrczGgZBB66J4"
            );

            con.setAutoCommit(true);
            System.out.println("PostgreSQL Connected ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 CONTROL API (TURN ON / OFF)
    @GetMapping("/control")
    public String control(
            @RequestParam String action,
            @RequestParam String device) {

        try {
            PreparedStatement pstmt = con.prepareStatement(
                "UPDATE iotDeviceStatus SET status=? WHERE device=?"
            );

            pstmt.setString(1, action.toUpperCase());
            pstmt.setString(2, device);

            int rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows > 0) {
                return "Device " + device + " " + action;
            } else {
                return "Device not found";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    // 🔥 STATUS API (GET ALL DEVICE STATUS)
    @GetMapping("/status")
    public String status() {

        String result = "";

        try {
            PreparedStatement pstmt = con.prepareStatement(
                "SELECT device, status FROM iotDeviceStatus ORDER BY device"
            );

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result += rs.getString("device") + ":" + rs.getString("status") + ",";
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

        return result;
    }
}