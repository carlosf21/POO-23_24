public class PraticanteOcasional extends Utilizador{
    public PraticanteOcasional(Utilizador utilizador) {
        super(utilizador);
        double fatorPraticante = 0.2;
        this.setFatorMultiplicativoBase(fatorPraticante);
    }
}