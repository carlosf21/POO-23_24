import java.io.Serializable;

public class Repeticoes extends Atividade implements Serializable {
    private int repeticoes;

    public Repeticoes(Atividade atividade, int repeticoes) {
        super(atividade);
        this.repeticoes = repeticoes;
    }

    public Repeticoes(Repeticoes atividade) {
        super(atividade);
        this.repeticoes = atividade.getRepeticoes();
    }

    public Repeticoes(int repeticoes) {
        super();
        this.repeticoes = repeticoes;
    }

    public Repeticoes() {
        super();
        this.repeticoes = 0;
    }

    public int getRepeticoes() {
        return this.repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    @Override
    public Atividade clone() {
        return new Repeticoes(this, this.repeticoes);
    }

    @Override
    public String toString() {
        return "Repeticoes{" +
                "nome='" + getNome() + '\'' +
                ", tempo=" + getTempo() +
                ", frequenciaCardiacaMedia=" + getFrequenciaCardiacaMedia() +
                ", dificuldade=" + getDificuldade() +
                ", calorias=" + getCaloriasConsumidas() +
                ", repetições=" + repeticoes +
                ", data=" + getData() +
                '}';
    }

    @Override
    public double calcularCaloriasConsumidas(Utilizador utilizador, double frequencia) {
        return (frequencia * utilizador.getFatorMultiplicativoBase() * this.getTempo() * ((double) (this.getRepeticoes())/10)/10);
    }

}