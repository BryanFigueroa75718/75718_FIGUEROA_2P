// commands/LoginCommand.java
package commands;

import Command;
import util.Validator;

import java.util.Optional;

public class LoginCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: login <usuario> <contraseña>");
            return;
        }

        String user = args[0];
        String pass = args[1];

        Optional<String> userError = validateUser(user);
        Optional<String> passError = validatePassword(pass);

        if (userError.isPresent() || passError.isPresent()) {
            userError.ifPresent(System.out::println);
            passError.ifPresent(System.out::println);
            System.out.println("❌ Login fallido");
            return;
        }

        System.out.println("✅ Login exitoso. Bienvenido, " + user + "!");
    }

    private Optional<String> validateUser(String user) {
        return Validator.isValidUser(user)
                ? Optional.empty()
                : Optional.of("❌ Usuario inválido: debe tener 4-16 caracteres, " +
                              "empezar con letra y solo usar [a-zA-Z0-9_-]");
    }

    private Optional<String> validatePassword(String pass) {
        return Validator.isValidPassword(pass)
                ? Optional.empty()
                : Optional.of("❌ Contraseña inválida: mínimo 8 caracteres, " +
                              "con mayúscula, minúscula y número");
    }

    @Override
    public String name() { return "login"; }

    @Override
    public String description() { return "login <user> <pass> - Autentica un usuario"; }
}