import java.io.Serializable;

public class DistanciaAltimetria extends Atividade implements Serializable {
    private double distancia;
    private double altimetria;

    public DistanciaAltimetria(Atividade atividade, double distancia, double alimetria) {
        super(atividade);
        this.distancia = distancia;
        this.altimetria = alimetria;
    }

    public DistanciaAltimetria(DistanciaAltimetria atividade) {
        super(atividade);
        this.distancia = atividade.getDistancia();
        this.altimetria = atividade.getAltimetria();
    }

    public DistanciaAltimetria(double distancia, double altimetria) {
        super();
        this.distancia = distancia;
        this.altimetria = altimetria;
    }

    public DistanciaAltimetria(){
        super();
        this.distancia = 0;
        this.altimetria = 0;
    }

    public double getDistancia() {
        return this.distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getAltimetria() {
        return this.altimetria;
    }

    public void setAltimetria(double alimetria) {
        this.altimetria = alimetria;
    }

    @Override
    public Atividade clone() {
        return new DistanciaAltimetria(this, this.distancia, this.altimetria);
    }

    @Override
    public String toString() {
        return "DistanciaAltimetria{" +
                "nome='" + getNome() + '\'' +
                ", tempo=" + getTempo() +
                ", frequenciaCardiacaMedia=" + getFrequenciaCardiacaMedia() +
                ", dificuldade=" + getDificuldade() +
                ", calorias=" + getCaloriasConsumidas() +
                ", distancia=" + distancia +
                ", altimetria=" + altimetria +
                ", data=" + getData() +
                '}';
    }

    @Override
    public double calcularCaloriasConsumidas(Utilizador utilizador, double frequencia) {
        return (frequencia * utilizador.getFatorMultiplicativoBase() * this.getTempo() * ((this.getDistancia()+this.getAltimetria()/100)/10)) / 10;
    }
}
