import java.io.Serializable;

public class RepeticoesPesos extends Atividade implements Serializable {
    private int repeticoes;
    private double peso;

    public RepeticoesPesos(Atividade atividade, int repeticoes, double peso) {
        super(atividade);
        this.repeticoes = repeticoes;
        this.peso = peso;
    }

    public RepeticoesPesos(RepeticoesPesos atividade) {
        super(atividade);
        this.repeticoes = atividade.getRepeticoes();
        this.peso = atividade.getPeso();
    }

    public RepeticoesPesos(int repeticoes, double peso) {
        super();
        this.repeticoes = repeticoes;
        this.peso = peso;
    }

    public RepeticoesPesos(){
        super();
        this.repeticoes = 0;
        this.peso = 0;
    }

    public int getRepeticoes() {
        return this.repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public Atividade clone() {
        return new RepeticoesPesos(this, this.repeticoes, this.peso);
    }

    @Override
    public String toString() {
        return "RepeticoesPesos{" +
                "nome='" + getNome() + '\'' +
                ", tempo=" + getTempo() +
                ", frequenciaCardiacaMedia=" + getFrequenciaCardiacaMedia() +
                ", dificuldade=" + getDificuldade() +
                ", calorias=" + getCaloriasConsumidas() +
                ", repetições=" + repeticoes +
                ", peso=" + peso +
                ", data=" + getData() +
                '}';
    }

    public double calcularCaloriasConsumidas(Utilizador utilizador, double frequencia) {
        return (frequencia * utilizador.getFatorMultiplicativoBase() * this.getTempo() * ((double) (this.getRepeticoes()+this.getPeso())/2)/200);
    }

}