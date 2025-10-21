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
    private final Map<String, User> users = new HashMap<>();
    
    public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
        this.bookRepo = bookRepo;
        this.reservationRepo = reservationRepo;
    }

    public void reserve(String userId, String bookId) {
        Book book = bookRepo.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
       User user = getUser(userId);
    
        if (reservationRepo.existsByUserAndBook(userId, bookId)) {
            throw new IllegalStateException("User already reserved this book");
        }
        
        if (book.getCopiesAvailable() > 0) {
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookRepo.save(book);
            reservationRepo.save(new Reservation(userId, bookId));
        } else if (user.isPriority()) {
            waitingLists.computeIfAbsent(bookId, k -> new LinkedList<>())
                    .add(new Reservation(userId, bookId));
        } else {
            throw new NoAvailableCopiesException("No copies available for book: " + bookId);
        }
    }

    private void addToWaitingList(String userId, String bookId) {
        // TODO Auto-generated method stub
        waitingLists.computeIfAbsent(bookId, k -> new LinkedList<>()).add(new Reservation(userId, bookId));
    }
    

    public void cancel(String userId, String bookId) {
        // TODO: Implement using TDD
        if (!reservationRepo.existsByUserAndBook(userId, bookId)) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservationRepo.delete(userId, bookId);

        Book book = bookRepo.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        Queue<Reservation> waitingList = waitingLists.get(bookId);
        if (waitingList != null && !waitingList.isEmpty()) {
            Reservation nextReservation = waitingList.poll();
            reservationRepo.save(nextReservation);
        } else {
            book.setCopiesAvailable(book.getCopiesAvailable() + 1);
            bookRepo.save(book);
        }

        if (waitingList != null && waitingList.isEmpty()) {
            waitingLists.remove(bookId);
        }
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
        Queue<Reservation> queue = waitingLists.get(bookId);
        return queue != null ? new ArrayList<>(queue) : new ArrayList<>();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    private User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            return new User(userId, "Default User", true);
        }
        return user;
    }
}