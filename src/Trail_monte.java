public class Trail_monte extends DistanciaAltimetria{
    public Trail_monte(DistanciaAltimetria atividade) {
        super(atividade);
        this.setNome("Trail_monte");
        this.setDificuldade("Hard");
    }

    public Trail_monte() {
        super();
        this.setNome("Trail_monte");
        this.setDificuldade("Hard");
    }

    public Trail_monte(double distancia, double altimetria) {
        super();
        this.setDistancia(distancia);
        this.setAltimetria(altimetria);
        this.setNome("Trail_monte");
        this.setDificuldade("Hard");
    }

    @Override
    public Atividade clone(){
        return new Trail_monte(this);
    }
}
