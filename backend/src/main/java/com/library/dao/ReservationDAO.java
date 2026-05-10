package com.library.dao ;

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

    
    public boolean addReservation(Reservation reservation) {

        String query = "INSERT INTO reservations (book_id, member_id, reservation_date, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reservation.getBookId());
            stmt.setInt(2, reservation.getMemberId());
            stmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(4, reservation.getStatus().name());

            int rows = stmt.executeUpdate();

            return rows > 0 ;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false ;
    }



    public List<Reservation> getAllReservations() {

        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM reservations";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Reservation reservation = new Reservation();

                reservation.setReservationId(rs.getInt("id"));

                
                reservation.setBookId(rs.getInt("book_id"));

                reservation.setMemberId(rs.getInt("member_id"));

                reservation.setReservationDate(
                        rs.getDate("reservation_date").toLocalDate()
                );

                reservation.setStatus(
                        ReservationStatus.valueOf(rs.getString("status"))
                );

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }



    public Reservation getReservationById(int id) {

        String query = "SELECT * FROM reservations WHERE reservation_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Reservation reservation = new Reservation();

                reservation.setReservationId(rs.getInt("reservation_id"));

                reservation.setBookId(rs.getInt("book_id"));

                reservation.setMemberId(rs.getInt("member_id"));

                reservation.setReservationDate(
                        rs.getDate("reservation_date").toLocalDate()
                );

                reservation.setStatus(
                        ReservationStatus.valueOf(rs.getString("status"))
                );

                return reservation;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public boolean updateReservation(Reservation reservation) {

        String query = "UPDATE reservations SET book_id = ?, member_id = ?, reservation_date = ?, status = ? WHERE reservation_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reservation.getBookId());
            stmt.setInt(2, reservation.getMemberId());
            stmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(4, reservation.getStatus().name());
            stmt.setInt(5, reservation.getReservationId());

            int rows = stmt.executeUpdate();

            return rows > 0 ;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false ;
    }




    public boolean deleteReservation(int id) {

        String query = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            return rows > 0 ;

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false ;
    }



    public List<Reservation> getReservationsByMemberId(int memberId) {

        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM reservations WHERE member_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Reservation reservation = new Reservation();

                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setMemberId(rs.getInt("member_id"));
                    reservation.setReservationDate(
                            rs.getDate("reservation_date").toLocalDate()
                    );
                    reservation.setStatus(
                            ReservationStatus.valueOf(rs.getString("status"))
                    );

                    reservations.add(reservation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }



    public List<Reservation> getReservationsByBookId(int bookId) {

        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM reservations WHERE book_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Reservation reservation = new Reservation();

                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setMemberId(rs.getInt("member_id"));
                    reservation.setReservationDate(
                            rs.getDate("reservation_date").toLocalDate()
                    );
                    reservation.setStatus(
                            ReservationStatus.valueOf(rs.getString("status"))
                    );

                    reservations.add(reservation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }



    public List<Reservation> getReservationsByStatus(ReservationStatus status) {

        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM reservations WHERE status = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Reservation reservation = new Reservation();

                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setMemberId(rs.getInt("member_id"));
                    reservation.setReservationDate(
                            rs.getDate("reservation_date").toLocalDate()
                    );
                    reservation.setStatus(
                            ReservationStatus.valueOf(rs.getString("status"))
                    );

                    reservations.add(reservation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }



}