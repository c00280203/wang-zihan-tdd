package main;

public class Reservation {
    private final String userId;
    private final String bookId;

    public Reservation(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return userId.equals(that.userId) && bookId.equals(that.bookId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(userId, bookId);
    }
}
