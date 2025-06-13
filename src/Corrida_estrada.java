public class Corrida_estrada extends Distancia {
    public Corrida_estrada(Distancia atividade) {
        super(atividade);
        this.setNome("Corrida_estrada");
    }

    public Corrida_estrada() {
        super();
        this.setNome("Corrida_estrada");
    }

    public Corrida_estrada(double distancia) {
        super();
        this.setDistancia(distancia);
        this.setNome("Corrida_estrada");
    }

    @Override
    public Atividade clone(){
        return new Corrida_estrada(this);
    }
}
