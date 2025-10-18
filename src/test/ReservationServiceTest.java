package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

import main.Book;
import main.User;
import main.IBookRepository;
import main.IReservationRepository;
import main.MemoryBookRepository;
import main.MemoryReservationRepository;
import main.NoAvailableCopiesException;
import main.ReservationService;

public class ReservationServiceTest {
    private ReservationService reservationService;
    private IBookRepository bookRepo;
    private IReservationRepository reservationRepo;
    private Book books;
    private User user;
    private Book stockBook;

    @Before
    public void setUp() {
        bookRepo = new MemoryBookRepository();
        reservationRepo = new MemoryReservationRepository();
        reservationService = new ReservationService(bookRepo, reservationRepo);

        books = new Book("Book001", "I love Java", 3);
        user = new User("C00280203", "Zihan Wang");

        bookRepo.save(books);

        stockBook = new Book("Book002", "I prefer C++", 0);
        bookRepo.save(stockBook);
    }

    @Test
    public void reserveBook() {
        setUp();
        reservationService.reserve(user.getId(), books.getId());
        Book book = bookRepo.findById(books.getId());
        assertEquals(2, book.getCopiesAvailable());
    }

    @Test
    public void reserveBook_BookNotFound_ThrowsException() {
        setUp();
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reservationService.reserve(user.getId(), "NON_EXISTENT"));
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void reserveBook_NoCopiesAvailable_ThrowsException() {
        NoAvailableCopiesException exception = assertThrows(NoAvailableCopiesException.class,
        () -> reservationService.reserve(user.getId(), stockBook.getId()));
        assertEquals("No copies available for book: " + stockBook.getId(), exception.getMessage());
    }
}