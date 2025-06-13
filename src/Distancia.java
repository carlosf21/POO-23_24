import java.io.Serializable;

public class Distancia extends Atividade implements Serializable {
    private double distancia;

    public Distancia(Atividade atividade, double distancia) {
        super(atividade);
        this.distancia = distancia;
    }

    public Distancia(Distancia atividade) {
        super(atividade);
        this.distancia = atividade.getDistancia();
    }

    public Distancia(double distancia) {
        super();
        this.distancia = distancia;
    }

    public Distancia() {
        super();
        this.distancia = 0;
    }

    public double getDistancia() {
        return this.distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    @Override
    public Atividade clone() {
        return new Distancia(this, this.distancia);
    }

    @Override
    public String toString() {
        return "Distancia{" +
                "nome='" + getNome() + '\'' +
                ", tempo=" + getTempo() +
                ", frequenciaCardiacaMedia=" + getFrequenciaCardiacaMedia() +
                ", dificuldade=" + getDificuldade() +
                ", calorias=" + getCaloriasConsumidas() +
                ", distancia=" + distancia +
                ", data=" + getData() +
                '}';
    }

    @Override
    public double calcularCaloriasConsumidas(Utilizador utilizador, double frequencia) {
        return (frequencia * utilizador.getFatorMultiplicativoBase() * this.getTempo() * (this.getDistancia()/5)) / 10;
    }

}
