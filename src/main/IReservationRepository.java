package main;

import java.util.List;

public interface IReservationRepository {
    void save(Reservation reservation);
    void delete(Reservation reservation);
    List<Reservation> findByUser(String userId);
    void delete(String userId, String bookId);
    boolean existsByUserAndBook(String userId, String bookId);
    List<Reservation> findAll();
}
