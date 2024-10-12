package com.example.demo.utils;

import com.example.demo.domain.GroupRecord;
import com.example.demo.domain.PasswordRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static Connection connection = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");

            // 获取当前应用的安装目录（即 .exe 所在目录）
            String installDir = System.getProperty("user.dir"); // 或 System.getProperty("java.class.path")，取决于打包工具

            // 指定数据库文件相对于安装目录的路径
            String dbFilePath = installDir + File.separator + "data" + File.separator + "passwords.db";

            // 检查数据库文件是否存在
            File dbFile = new File(dbFilePath);
            if (!dbFile.exists()) {
                // 如果数据库文件不存在，可以考虑复制一个初始数据库
                try (InputStream inputStream = DatabaseHelper.class.getResourceAsStream("/passwords.db")) {
                    if (inputStream == null) {
                        throw new RuntimeException("初始化数据库文件丢失。");
                    }

                    // 创建目标目录
                    Files.createDirectories(Paths.get(installDir, "data"));

                    // 将资源中的数据库文件复制到安装目录
                    Files.copy(inputStream, Paths.get(dbFilePath), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // 使用安装目录中的数据库文件进行连接
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            System.out.println("连接到数据库成功，数据库路径：" + dbFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    // 创建数据库表
    private static void createTables() throws SQLException {
        String createGroupsTable = "CREATE TABLE IF NOT EXISTS groups (" +
                "id INTEGER PRIMARY KEY," + // 移除 AUTOINCREMENT
                "group_name TEXT NOT NULL UNIQUE" +
                ");";

        String createPasswordsTable = "CREATE TABLE IF NOT EXISTS passwords (" +
                "id INTEGER PRIMARY KEY," + // 移除 AUTOINCREMENT
                "site TEXT NOT NULL," +
                "site_url TEXT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "notes TEXT," +
                "group_name TEXT," +
                "FOREIGN KEY (group_name) REFERENCES groups(group_name)" +
                ");";


        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createGroupsTable);
            stmt.execute(createPasswordsTable);
        }
    }

    // 获取所有分组
    public static List<GroupRecord> getAllGroups() {
        List<GroupRecord> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups";

        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groups.add(new GroupRecord(rs.getInt("id"), rs.getString("group_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public static List<String> getAllGroupNames() {
        List<String> groupNames = new ArrayList<>();
        List<GroupRecord> groups = getAllGroups(); // 获取所有组记录

        for (GroupRecord group : groups) {
            groupNames.add(group.getGroupName()); // 提取组名
        }

        return groupNames;
    }

    // 添加分组
    public static void addGroup(GroupRecord group) {
        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "INSERT INTO groups (group_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, group.getGroupName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新分组
    public static void updateGroup(GroupRecord group) {

        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "UPDATE groups SET group_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, group.getGroupName());
            pstmt.setInt(2, group.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除分组
    public static void deleteGroup(GroupRecord group) {

        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "DELETE FROM groups WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, group.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取所有密码记录
    public static List<PasswordRecord> getAllPasswordRecords() {
        List<PasswordRecord> passwordRecords = new ArrayList<>();
        String sql = "SELECT * FROM passwords";


        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                passwordRecords.add(new PasswordRecord(
                        rs.getInt("id"),
                        rs.getString("site"),
                        rs.getString("site_url"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("notes"),
                        rs.getString("group_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordRecords;
    }

    // 添加密码记录
    public static void addPasswordRecord(PasswordRecord record) {
        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "INSERT INTO passwords (site, site_url, username, password, notes, group_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, record.getSite());
            pstmt.setString(2, record.getSiteUrl());
            pstmt.setString(3, record.getUsername());
            pstmt.setString(4, record.getPassword());
            pstmt.setString(5, record.getNotes());
            pstmt.setString(6, record.getGroupName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新密码记录
    public static void updatePasswordRecord(PasswordRecord record) {
        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "UPDATE passwords SET site = ?, site_url = ?, username = ?, password = ?, notes = ?, group_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, record.getSite());
            pstmt.setString(2, record.getSiteUrl());
            pstmt.setString(3, record.getUsername());
            pstmt.setString(4, record.getPassword());
            pstmt.setString(5, record.getNotes());
            pstmt.setString(6, record.getGroupName());
            pstmt.setInt(7, record.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除密码记录
    public static void deletePasswordRecord(PasswordRecord record) {

        // 从 DatabaseHelper 获取数据库连接
        Connection connection = DatabaseHelper.getConnection();

        String sql = "DELETE FROM passwords WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, record.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
