package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.Book;
import main.User;
import main.IBookRepository;
import main.IReservationRepository;
import main.MemoryBookRepository;
import main.MemoryReservationRepository;
import main.NoAvailableCopiesException;
import main.Reservation;
import main.ReservationService;

public class ReservationServiceTest {
    private ReservationService reservationService;
    private IBookRepository bookRepo;
    private IReservationRepository reservationRepo;
    private Book books;
    private User user;
    private Book stockBook;
    private User user02;

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

        user02 = new User("C00280204", "Handsome Man");
    }

    @Test
    public void reserveBook() {
        // setUp();
        reservationService.reserve(user.getId(), books.getId());
        Book book = bookRepo.findById(books.getId());
        assertEquals(2, book.getCopiesAvailable());
    }

    @Test
    public void reserveBook_BookNotFound_ThrowsException() {
        // setUp();
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

    @Test
    public void reserveBook_UserAlreadyReserved_ThrowsException() {
        // Given
        reservationService.reserve(user.getId(), books.getId());
        
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> reservationService.reserve(user.getId(), books.getId()));
        assertEquals("User already reserved this book", exception.getMessage());
    }

    @Test
    public void cancelReservation_SuccessfulCancellation() {
        // Given
        reservationService.reserve(user.getId(), books.getId());
        Book bookBeforeCancel = bookRepo.findById(books.getId());
        assertEquals(2, bookBeforeCancel.getCopiesAvailable());
        
        // When
        reservationService.cancel(user.getId(), books.getId());
        
        // Then - This will fail initially as cancel method is not implemented
        Book bookAfterCancel = bookRepo.findById(books.getId());
        assertEquals(3, bookAfterCancel.getCopiesAvailable());
    }

    @Test
    public void cancelReservation_NonExistentReservation_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> reservationService.cancel(user.getId(), books.getId()));
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    public void listReservations_ReturnsUserReservations() {
        // Given
        reservationService.reserve(user.getId(), books.getId());
        
        // When
        List<Reservation> reservations = reservationService.listReservations(user.getId());
        
        // Then - This will fail initially as listReservations method is not implemented
        assertEquals(1, reservations.size());
        assertEquals(books.getId(), reservations.get(0).getBookId());
        assertEquals(user.getId(), reservations.get(0).getUserId());
    }

    @Test
    public void listReservationsForBook_ReturnsBookReservations() {
        // Given
        reservationService.reserve(user.getId(), books.getId());
        reservationService.reserve(user02.getId(), books.getId());
        
        // When
        List<Reservation> reservations = reservationService.listReservationsForBook(books.getId());
        
        // Then - This will fail initially as listReservationsForBook method is not implemented
        assertEquals(2, reservations.size());
    }

    @Test
    public void reserveLastCopy_Successful() {
        // Given
        Book lastCopyBook = new Book("B003", "Last Copy Book", 1);
        bookRepo.save(lastCopyBook);
        
        // When
        reservationService.reserve(user.getId(), lastCopyBook.getId());
        
        // Then
        Book book = bookRepo.findById(lastCopyBook.getId());
        assertEquals(0, book.getCopiesAvailable());
        
        // Verify no more copies available
        NoAvailableCopiesException exception = assertThrows(NoAvailableCopiesException.class,
            () -> reservationService.reserve(user02.getId(), lastCopyBook.getId()));
    }

    @Test
    public void reserveBook_PriorityUser_CanReserve() {
        User prioUser = new User("C00288344", "Handsome Melka");
        Book outOfStockBook = new Book("Book003", "How to get rich women's heart", 1);
        bookRepo.save(outOfStockBook);

        reservationService.reserve(prioUser.getId(), outOfStockBook.getId());

        // List<Reservation> waitingList = reservationService.getWaitingList(outOfStockBook.getId());
        // assertEquals(1, waitingList.size());
        // assertEquals(prioUser.getId(), waitingList.get(0).getUserId());
    }

   @Test
    public void reserveBook_RegularUser_CannotReserve() {
        User regUser = new User("C00290305", "Robot", false);
        Book outBook = new Book("Book004", "How could be handsome", 0);
        bookRepo.save(outBook);

        assertThrows(NoAvailableCopiesException.class, () -> reservationService.reserve(regUser.getId(), outBook.getId()));
    }

    @Test
    public void getWaitingList_PrioUserOrder() {
        User prioUser01 = new User("C00000001", "Filip Handsome", true);
        User prioUser02 = new User("C00000002", "I am handsome", true);
        Book outBook = new Book("Book005", "Penguin City Survival Guide", 0);

        bookRepo.save(outBook);
        
        reservationService.reserve(prioUser01.getId(), outBook.getId());
        reservationService.reserve(prioUser02.getId(), outBook.getId());

        List<Reservation> waitingList = reservationService.getWaitingList(outBook.getId());
        assertEquals(2, waitingList.size());
        assertEquals(prioUser01.getId(), waitingList.get(0).getUserId());
        assertEquals(prioUser02.getId(), waitingList.get(1).getUserId());
    }
}