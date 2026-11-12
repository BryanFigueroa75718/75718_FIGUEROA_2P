// Main.java
public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal()
                .withBanner("""
                ┌──────────────────────────────────────┐
                │      JAVA STREAM TERMINAL v1.0       │
                │   Comandos: help, login, exit        │
                └──────────────────────────────────────┘""")
                .withPrompt("streams@terminal:~$ ");

        terminal.run();
    }
}