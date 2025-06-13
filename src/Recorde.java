import java.io.Serializable;

public class Recorde implements Serializable {
    private Utilizador user;
    private Atividade activity;

    public Recorde(Utilizador user, Atividade activity) {
        this.user = user;
        this.activity = activity;
    }

    public Utilizador getUser() {
        return user;
    }

    public Atividade getActivity() {
        return activity;
    }
}
