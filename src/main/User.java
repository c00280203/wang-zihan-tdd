package main;

public class User {
    private final String id;
    private final String name;
    private final boolean priority;

    public User(String id, String name) {
        this(id, name, false);
    }

    public User(String id, String name, boolean priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isPriority() { return priority; }
}
