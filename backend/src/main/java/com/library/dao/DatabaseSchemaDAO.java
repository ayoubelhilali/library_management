package com.library.dao;

import java.sql.Statement;

import com.library.patterns.singleton.DBConnection;

public class DatabaseSchemaDAO {



    public static void createTables(){

        createUserTable();
        createMemberTable();

        createBookTable();
        createBorrowTable();
        createNotificationTable();
        createReservationTable();



    }



    private static void createUserTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(100) UNIQUE NOT NULL,
                email VARCHAR(150) UNIQUE,
                phone VARCHAR(50),
                password VARCHAR(255) NOT NULL,
                role ENUM('ADMIN', 'MEMBER') NOT NULL
            )
        """;

        execute(sql);

    }


    private static void createMemberTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS members (
                user_id INT PRIMARY KEY,
                member_type ENUM('STUDENT', 'TEACHER') NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """;

        execute(sql);
    }
    



    
    private static void createBookTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                id INT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                category VARCHAR(100),
                isbn VARCHAR(50) UNIQUE,
                status VARCHAR(50)
            )
        """;

        execute(sql);
    }



    private static void createBorrowTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS borrows (
                id INT PRIMARY KEY AUTO_INCREMENT,
                book_id INT NOT NULL,
                member_id INT NOT NULL,
                borrow_date DATE NOT NULL,
                expected_return_date DATE,
                actual_return_date DATE ,
                FOREIGN KEY (book_id) REFERENCES books(id),
                FOREIGN KEY (member_id) REFERENCES members(user_id)
            )
        """;

        execute(sql);
    }



    // private static void createMemberTable() {

    //     String sql = """
    //         CREATE TABLE IF NOT EXISTS members (
    //             id INT PRIMARY KEY AUTO_INCREMENT,
    //             name VARCHAR(255) NOT NULL,
    //             email VARCHAR(255) UNIQUE,
    //             phone VARCHAR(50),
    //             member_type ENUM('STUDENT', 'TEACHER') NOT NULL
    //         )
    //     """;

    //     execute(sql);
    // }



    private static void createNotificationTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS notifications (
                notification_id INT PRIMARY KEY AUTO_INCREMENT,
                member_id INT NOT NULL,
                message TEXT NOT NULL,
                send_date DATE NOT NULL,
                is_read BOOLEAN DEFAULT FALSE,
                FOREIGN KEY (member_id) REFERENCES members(user_id)
            )
        """;

        execute(sql);
    }



    private static void createReservationTable() {

        String sql = """
            CREATE TABLE IF NOT EXISTS reservations (
                reservation_id INT PRIMARY KEY AUTO_INCREMENT,
                book_id INT NOT NULL,
                member_id INT NOT NULL,
                reservation_date DATE NOT NULL,
                status ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL,
                queue_position INT DEFAULT 1,
                FOREIGN KEY (book_id) REFERENCES books(id),
                FOREIGN KEY (member_id) REFERENCES members(user_id)
            )
        """;

        execute(sql);
    }



    private static void execute(String sql) {

        try (Statement stmt = DBConnection.getInstance().getConnection().createStatement()) {
            stmt.execute(sql);

        }catch (Exception e) {

            e.printStackTrace();

        }
    }
}
