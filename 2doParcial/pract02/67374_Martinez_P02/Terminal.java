// Terminal.java
import commands.HelpCommand;
import commands.LoginCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Terminal {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private String prompt = "user@java-terminal:~$ ";
    private String banner = "=== JAVA STREAM TERMINAL v1.0 ===";

    public Terminal() {
        register(new LoginCommand());
        register(new HelpCommand(commands));
    }

    public void register(Command cmd) {
        commands.put(cmd.name(), cmd);
    }

    public Terminal withPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public Terminal withBanner(String banner) {
        this.banner = banner;
        return this;
    }

    public void run() {
        System.out.println(banner);
        System.out.println("Escribe 'help' para ver los comandos disponibles.\n");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Saliendo...");
                    break;
                }
                process(line);
                System.out.print(prompt);
            }
        } catch (IOException e) {
            System.err.println("Error de entrada: " + e.getMessage());
        }
    }

    private void process(String line) {
        // Parsing con streams: divide respetando espacios
        String[] tokens = Arrays.stream(line.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        if (tokens.length == 0) return;

        String cmdName = tokens[0].toLowerCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        Optional.ofNullable(commands.get(cmdName))
                .ifPresentOrElse(
                        cmd -> cmd.execute(args),
                        () -> System.out.println("Comando no reconocido: " + cmdName +
                                                 " (usa 'help')")
                );
    }
}