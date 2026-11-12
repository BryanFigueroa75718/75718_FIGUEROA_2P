// commands/HelpCommand.java
package commands;

import Command;
import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    private final Map<String, Command> registry;

    public HelpCommand(Map<String, Command> registry) {
        this.registry = registry;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("\n=== COMANDOS DISPONIBLES ===");
        String list = registry.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> String.format("  %-10s -> %s",
                        e.getKey(), e.getValue().description()))
                .collect(Collectors.joining("\n"));
        System.out.println(list);
        System.out.println("============================\n");
    }

    @Override
    public String name() { return "help"; }

    @Override
    public String description() { return "Muestra los comandos disponibles"; }
}