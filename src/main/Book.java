package main;

public class Book {
    private final String id;
    private final String title;
    private int copiesAvailable;

    public Book(String id, String title, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.copiesAvailable = copiesAvailable;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getCopiesAvailable() { return copiesAvailable; }
    
    // Setters for mutable fields
    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }
}
