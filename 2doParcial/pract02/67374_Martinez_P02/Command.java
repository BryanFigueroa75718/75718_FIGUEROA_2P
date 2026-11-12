// Command.java
@FunctionalInterface
public interface Command {
    void execute(String[] args);
    default String name() { return ""; }
    default String description() { return ""; }
}