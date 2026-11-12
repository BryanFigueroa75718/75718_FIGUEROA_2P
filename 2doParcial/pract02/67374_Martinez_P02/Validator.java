// util/Validator.java
import java.util.regex.Pattern;

public class Validator {
    // Usuario: 4-16 caracteres, empieza con letra, permite letras, números, _ y -
    public static final Pattern USER_PATTERN =
        Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]{3,15}$");

    // Contraseña: mínimo 8, al menos 1 mayúscula, 1 minúscula y 1 dígito
    public static final Pattern PASS_PATTERN =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    public static boolean isValidUser(String user) {
        return user != null && USER_PATTERN.matcher(user).matches();
    }

    public static boolean isValidPassword(String pass) {
        return pass != null && PASS_PATTERN.matcher(pass).matches();
    }
}