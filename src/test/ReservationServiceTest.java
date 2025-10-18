package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.Book;
import main.User;
import main.IBookRepository;
import main.IReservationRepository;
import main.MemoryBookRepository;
import main.MemoryReservationRepository;
import main.ReservationService;


public class ReservationServiceTest {
    private ReservationService reservationService;
    private IBookRepository iBookRepository;
    private IReservationRepository iReservationRepository;
    private Book books;
    private User user;

    void setUp() {
        bookRepo = new MemoryBookRepository();
        reservationRepo = new MemoryBookRepository();
        reservationService = new ReservationService(bookRepo, reservationRepo);

        books = new Book("Book001", "I love Java", 3);
        user = new User("C00280203", "Zihan Wang");

        bookRepo.save(books);
    }

    @Test
    void reserveBook() {
        reservationService.reserve(user.getId(), book.getId());
        Book book = bookRepo.findById(books.getId().orElseThrow());
        assertEquals(2, book.getCopiesAvailable());
    }
}
