public class Profissional extends Utilizador{
    public Profissional(Utilizador utilizador) {
        super(utilizador);
        double fatorProfissional = 0.6;
        this.setFatorMultiplicativoBase(fatorProfissional);
    }
}
