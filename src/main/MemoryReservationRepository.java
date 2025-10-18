package main;

import java.util.*;

public class MemoryReservationRepository implements IReservationRepository {
    private final Set<Reservation> reservations = new HashSet<>();

    @Override
    public void save(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservations.remove(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations);
    }
}