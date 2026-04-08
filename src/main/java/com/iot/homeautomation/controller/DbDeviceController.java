package com.iot.homeautomation.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.*;

@RestController
@CrossOrigin(origins = "*")   // 🔥 ADD THIS
public class DbDeviceController {

    private Connection con;

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