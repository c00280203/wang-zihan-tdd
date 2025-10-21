package main;

import java.util.*;

public class MemoryReservationRepository implements IReservationRepository {
    private final Set<Reservation> reservations = new HashSet<>();

    @Override
    public void save(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public boolean existsByUserAndBook(String userId, String bookId) {
        return reservations.stream().anyMatch(r -> r.getUserId().equals(userId) && r.getBookId().equals(bookId));
    }

    @Override
    public List<Reservation> findByUser(String userId) {
        return reservations.stream().filter(r -> r.getUserId().equals(userId)).toList();
    }


    @Override
    public void delete(Reservation reservation) {
        reservations.remove(reservations);
    }

    @Override
    public List<Reservation> findAll() {
        // TODO Auto-generated method stub
        return new ArrayList<>(reservations);
    }

    @Override
    public void delete(String bookId, String userId) {
        // TODO Auto-generated method stub
        reservations.removeIf(r -> r.getUserId().equals(userId) && r.getBookId().equals(bookId));
    }
}