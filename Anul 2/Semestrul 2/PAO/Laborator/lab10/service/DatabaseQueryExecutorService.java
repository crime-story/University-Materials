package service;

import mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryExecutorService {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/laborator";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "sa";

    /**
     * Used for SELECT queries
     */
    public static List<Object> executeReadQuery(String sql, RowMapper<?> rowMapper) {
        Connection conn = null;
        Statement stmt = null;
        List<Object> result = new ArrayList<>();
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 4: Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                result.add(rowMapper.mapRow(rs));

            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try

        return result;
    }

    /**
     * Used for INSERT, UPDATE, DELETE sql queries
     */
    public static void executeUpdateQuery(String sql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connected database successfully...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
