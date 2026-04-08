package com.iot.homeautomation.controller;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@RestController
public class DbDeviceController {

    private Connection con;

    // 🔥 Constructor (like init())
    public DbDeviceController() {
        try {
        	Class.forName("org.postgresql.Driver");

        	con = DriverManager.getConnection(
        	    "jdbc:postgresql://dpg-d7b6v98ule4c738sp9e0-a.oregon-postgres.render.com:5432/iot_db_veg6",
        	    "iotdb_gz1n_user",
        	    "iVuN78UhB0AwMGhhajDTrczGgZBB66J4"
        	);
            con.setAutoCommit(true);

            System.out.println("DB Connected");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 API
    @GetMapping("/control")
    public String controlDevice(
            @RequestParam String action,
            @RequestParam String device) {

        try {

            PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE iotDeviceStatus SET status=? WHERE TRIM(device)=?"
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
            return "Server Error";
        }
    }
    @GetMapping("/status")
    public String getStatus() {

        String result = "";

        try {

            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT device, status FROM iotDeviceStatus ORDER BY device"
            );

            java.sql.ResultSet rs = pstmt.executeQuery();

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