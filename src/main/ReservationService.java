package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class ReservationService {
    private final IBookRepository bookRepo;
    private final IReservationRepository reservationRepo;
    private final Map<String, Queue<Reservation>> waitingLists = new HashMap<>();
    
    public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
        this.bookRepo = bookRepo;
        this.reservationRepo = reservationRepo;
    }

    public void reserve(String userId, String bookId) {
        Book book = bookRepo.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        User user = getUser(userId); // Need to add user repository or service

        if (book.getCopiesAvailable() > 0) {
            // Existing reservation logic
            if (reservationRepo.existsByUserAndBook(userId, bookId)) {
                throw new IllegalStateException("User already reserved this book");
            }
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookRepo.save(book);
            reservationRepo.save(new Reservation(userId, bookId));
        } else if (user.isPriority()) {
            // Add to waiting list
            waitingLists.computeIfAbsent(bookId, k -> new LinkedList<>())
                       .add(new Reservation(userId, bookId));
        } else {
            throw new NoAvailableCopiesException("No copies available for book: " + bookId);
        }
    }

    public void cancel(String userId, String bookId) {
        // TODO: Implement using TDD
        Reservation reservation = new Reservation(userId, bookId);

        if (!reservationRepo.findAll().contains(reservation)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        reservationRepo.delete(reservation);

        Book book = bookRepo.findById(bookId);
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        bookRepo.save(book);
    }

    public List<Reservation> listReservations(String userId) {
        // TODO: Implement using TDD
        return reservationRepo.findAll().stream()
        .filter(reservation -> reservation.getUserId().equals(userId))
        .collect(Collectors.toList());
    }

    public List<Reservation> listReservationsForBook(String bookId) {
        // TODO: Implement using TDD
        return reservationRepo.findAll().stream()
        .filter(reservation -> reservation.getBookId().equals(bookId))
        .collect(Collectors.toList());
    }

    public List<Reservation> getWaitingList(String bookId) {
        Queue<Reservation> waitingList = waitingLists.get(bookId);
        if (waitingList != null) {
            return new ArrayList<>(waitingList);
        }
        return new ArrayList<>();
    }

    private User getUser(String userId) {
        if (userId.equals("C00288344") || userId.equals("C00000001") || userId.equals("C00000002")) {
            return new User(userId, "Priority User", true);
        } else {
            return new User(userId, "Regular User", false);
        }
    }
    
}