public class Bicicleta_montanha extends DistanciaAltimetria{
    public Bicicleta_montanha(DistanciaAltimetria atividade) {
        super(atividade);
        this.setNome("Bicicleta_montanha");
        this.setDificuldade("Hard");
    }

    public Bicicleta_montanha() {
        super();
        this.setNome("Bicicleta_montanha");
        this.setDificuldade("Hard");
    }

    public Bicicleta_montanha(double distancia, double altimetria) {
        super();
        this.setDistancia(distancia);
        this.setAltimetria(altimetria);
        this.setNome("Bicicleta_montanha");
        this.setDificuldade("Hard");
    }

    @Override
    public Atividade clone(){
        return new Bicicleta_montanha(this);
    }
}
