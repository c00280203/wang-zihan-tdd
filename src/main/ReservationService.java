package main;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private final IBookRepository bookRepo;
    private final IReservationRepository reservationRepo;

    public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
        this.bookRepo = bookRepo;
        this.reservationRepo = reservationRepo;
    }

    public void reserve(String userId, String bookId) {
        Book book = bookRepo.findById(bookId);
        
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        // Check if copies available
        if (book.getCopiesAvailable() <= 0) {
            throw new NoAvailableCopiesException("No copies available for book: " + bookId);
        }

        // Check if user already reserved this book
        List<Reservation> allReservations = reservationRepo.findAll();
        for (Reservation reservation : allReservations) {
            if (reservation.getUserId().equals(userId) && reservation.getBookId().equals(bookId)) {
                throw new IllegalStateException("User already reserved this book");
            }
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepo.save(book);

        Reservation reservation = new Reservation(userId, bookId);
        reservationRepo.save(reservation);
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
}