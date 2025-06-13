public class Amador extends Utilizador{
    public Amador(Utilizador utilizador) {
        super(utilizador);
        double fatorAmador = 0.4;
        this.setFatorMultiplicativoBase(fatorAmador);
    }
}
