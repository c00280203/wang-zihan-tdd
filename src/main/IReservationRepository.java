package main;

import java.util.List;

public interface IReservationRepository {
    void save(Reservation reservation);
    void delete(Reservation reservation);
    List<Reservation> findAll();
}
