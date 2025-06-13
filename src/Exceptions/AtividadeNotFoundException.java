package Exceptions;

import java.io.IOException;

public class AtividadeNotFoundException extends IOException {
    public AtividadeNotFoundException() {
        super("Atividade não encontrada.");
    }

    public AtividadeNotFoundException(String message) {
        super(message);
    }
}
