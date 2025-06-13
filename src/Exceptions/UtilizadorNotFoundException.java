package Exceptions;

import java.io.IOException;

public class UtilizadorNotFoundException extends IOException {
    public UtilizadorNotFoundException() {
        super("Utilizador não encontrado.");
    }

    public UtilizadorNotFoundException(String message) {
        super(message);
    }
}
