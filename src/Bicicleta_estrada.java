public class Bicicleta_estrada extends Distancia {
    public Bicicleta_estrada(Distancia atividade) {
        super(atividade);
        this.setNome("Bicicleta_estrada");
    }

    public Bicicleta_estrada() {
        super();
        this.setNome("Bicicleta_estrada");
    }

    public Bicicleta_estrada(double distancia) {
        super();
        this.setDistancia(distancia);
        this.setNome("Bicicleta_estrada");
    }

    @Override
    public Atividade clone(){
        return new Bicicleta_estrada(this);
    }
}
