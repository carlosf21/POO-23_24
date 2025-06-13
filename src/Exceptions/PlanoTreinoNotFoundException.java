package Exceptions;

import java.io.IOException;

public class PlanoTreinoNotFoundException extends IOException {
    public PlanoTreinoNotFoundException() {
        super("PlanoTreino não encontrado.");
    }

    public PlanoTreinoNotFoundException(String message) {
        super(message);
    }
}
