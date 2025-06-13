import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Atividade implements Serializable {
    private String id;
    private String nome;
    private String dificuldade;
    private int tempo; //tempo despendido a realizar a atividade;
    private double frequenciaCardiacaMedia;
    private double caloriasConsumidas;
    private LocalDateTime data;


    /**
     * Construtores
     */

    public Atividade(Atividade atividade) {
        this.id = atividade.getId();
        this.nome = atividade.getNome();
        this.tempo = atividade.getTempo();
        this.frequenciaCardiacaMedia = atividade.getFrequenciaCardiacaMedia();
        this.dificuldade = atividade.getDificuldade();
        this.caloriasConsumidas = atividade.getCaloriasConsumidas();
        this.data = atividade.getData();
    }

    public Atividade() {
        this.id = UUID.randomUUID().toString();
        this.nome = "";
        this.tempo = 0;
        this.frequenciaCardiacaMedia = 0;
        this.dificuldade = "";
        this.caloriasConsumidas = 0;
        this.data = null;
    }

    public Atividade(int tempo, LocalDateTime data) {
        this.id = UUID.randomUUID().toString();
        this.nome = "";
        this.tempo = tempo;
        this.frequenciaCardiacaMedia = 0;
        this.dificuldade = "";
        this.caloriasConsumidas = 0;
        this.data = data;
    }

    public Atividade(String nome, int tempo, LocalDateTime data) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.tempo = tempo;
        this.frequenciaCardiacaMedia = 0;
        this.dificuldade = "";
        this.caloriasConsumidas = 0;
        this.data = data;
    }

    /**
     * Getters e Setters
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Atividade clone() {
        return new Atividade(this);
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public double getFrequenciaCardiacaMedia() {
        return frequenciaCardiacaMedia;
    }

    public void setFrequenciaCardiacaMedia(double frequenciaCardiacaMedia) {
        this.frequenciaCardiacaMedia = frequenciaCardiacaMedia;
    }

    public double getCaloriasConsumidas() {
        return this.caloriasConsumidas;
    }

    public void setCaloriasConsumidas(double caloriasConsumidas) {
        this.caloriasConsumidas = caloriasConsumidas;
    }

    public LocalDateTime getData() {
        return this.data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String toString() {
        return "Atividade{" +
                "nome='" + nome + '\'' +
                ", tempo=" + tempo +
                ", frequenciaCardiacaMedia=" + frequenciaCardiacaMedia +
                ", dificuldade=" + dificuldade +
                ", calorias=" + caloriasConsumidas +
                ", data=" + data +
                '}';
    }

    public double calcularFrequenciaAtividade(Utilizador utilizador) {
        double frequenciaBase;
        if (Objects.equals(this.getDificuldade(), "Hard")) {
            frequenciaBase = 190;
        } else {
            frequenciaBase = 140;
        }

        return (utilizador.getFrequenciaCardiacaMedia() + frequenciaBase) / 2;
    }

    public double calcularCaloriasConsumidas(Utilizador utilizador, double frequencia) {
        return (frequencia * utilizador.getFatorMultiplicativoBase() * this.getTempo()) / 10;
    }

    public boolean isHard() {
        return this.dificuldade.equals("Hard");
    }

}
