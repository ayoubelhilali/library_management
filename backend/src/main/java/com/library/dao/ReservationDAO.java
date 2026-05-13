package com.library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.model.Reservation;
import com.library.model.enums.ReservationStatus;
import com.library.patterns.singleton.DBConnection;

public class ReservationDAO {

    private final Connection connection;

    public ReservationDAO() {
        connection = DBConnection.getInstance().getConnection();
    }

    public boolean addReservation(Reservation reservation) {

        String query = """
            INSERT INTO reservations
            (book_id, member_id, reservation_date, status, queue_position)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, reservation.getBookId());
            stmt.setInt(2, reservation.getMemberId());
            stmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(4, reservation.getStatus().name());
            stmt.setInt(5, reservation.getQueuePosition());

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Reservation> getAllReservations() {

        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM reservations";

        try (
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Reservation reservation = mapReservation(rs);

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public Reservation getReservationById(int id) {

        String query =
                "SELECT * FROM reservations WHERE reservation_id = ?";

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapReservation(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Reservation getFirstPendingReservationByBookId(int bookId) {

        String query = """
        SELECT *
        FROM reservations
        WHERE book_id = ?
        AND status = 'PENDING'
        ORDER BY queue_position ASC
        LIMIT 1
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reservation reservation = new Reservation();

                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setBookId(rs.getInt("book_id"));
                reservation.setMemberId(rs.getInt("member_id"));
                reservation.setReservationDate(rs.getDate("reservation_date").toLocalDate());
                reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setQueuePosition(rs.getInt("queue_position"));

                return reservation;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean decrementQueuePositions(int bookId, int removedPosition) {

        String query = """
        UPDATE reservations
        SET queue_position = queue_position - 1
        WHERE book_id = ?
        AND status = 'PENDING'
        AND queue_position > ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookId);
            stmt.setInt(2, removedPosition);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateReservation(Reservation reservation) {

        String query = """
            UPDATE reservations
            SET book_id = ?,
                member_id = ?,
                reservation_date = ?,
                status = ?,
                queue_position = ?
            WHERE reservation_id = ?
        """;

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, reservation.getBookId());
            stmt.setInt(2, reservation.getMemberId());
            stmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(4, reservation.getStatus().name());
            stmt.setInt(5, reservation.getQueuePosition());
            stmt.setInt(6, reservation.getReservationId());

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteReservation(int id) {

        String query =
                "DELETE FROM reservations WHERE reservation_id = ?";

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Reservation> getReservationsByMemberId(int memberId) {

        List<Reservation> reservations = new ArrayList<>();

        String query =
                "SELECT * FROM reservations WHERE member_id = ?";

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, memberId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Reservation reservation = mapReservation(rs);

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public Reservation getFirstPendingReservationForBook(int bookId) {
        String query =
                "SELECT * FROM reservations WHERE book_id = ? AND status = 'PENDING' ORDER BY queue_position ASC LIMIT 1";
        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean decrementQueuePositionsForBook(int bookId) {
        String query = "UPDATE reservations SET queue_position = queue_position - 1 WHERE book_id = ? AND status = 'PENDING'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Reservation> getReservationsByBookId(int bookId) {

        List<Reservation> reservations = new ArrayList<>();

        String query =
                "SELECT * FROM reservations WHERE book_id = ?";

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, bookId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Reservation reservation = mapReservation(rs);

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public List<Reservation> getReservationsByStatus(
            ReservationStatus status
    ) {

        List<Reservation> reservations = new ArrayList<>();

        String query =
                "SELECT * FROM reservations WHERE status = ?";

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setString(1, status.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Reservation reservation = mapReservation(rs);

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public int getNextQueuePosition(int bookId) {

        String query = """
            SELECT COUNT(*) + 1 AS next_position
            FROM reservations
            WHERE book_id = ?
            AND status = 'PENDING'
        """;

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, bookId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("next_position");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public Reservation getPendingReservation(
            int bookId,
            int memberId
    ) {

        String query = """
            SELECT *
            FROM reservations
            WHERE book_id = ?
            AND member_id = ?
            AND status = 'PENDING'
        """;

        try (
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {

            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapReservation(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Reservation mapReservation(ResultSet rs)
            throws SQLException {

        Reservation reservation = new Reservation();

        reservation.setReservationId(
                rs.getInt("reservation_id")
        );

        reservation.setBookId(
                rs.getInt("book_id")
        );

        reservation.setMemberId(
                rs.getInt("member_id")
        );

        reservation.setReservationDate(
                rs.getDate("reservation_date").toLocalDate()
        );

        reservation.setStatus(
                ReservationStatus.valueOf(
                        rs.getString("status")
                )
        );

        reservation.setQueuePosition(
                rs.getInt("queue_position")
        );

        return reservation;
    }
}