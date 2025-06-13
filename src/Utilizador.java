import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Utilizador implements Serializable {

    private int codigo;
    private String nome;
    private String morada;
    private String email;
    private double frequenciaCardiacaMedia;
    private double fatorMultiplicativoBase;
    private Map<String, Atividade> atividadesIsoladas;
    private Map<String, Atividade> atividadesRealizadas;
    private Map<String, PlanoTreino> planosTreino;

    public Utilizador() {
        this.codigo = -1;
        this.nome = "";
        this.morada = "";
        this.email = "";
        this.frequenciaCardiacaMedia = 0;
        this.atividadesRealizadas = new HashMap<>();
        this.planosTreino = new HashMap<>();
        this.atividadesIsoladas = new HashMap<>();
    }

    public Utilizador(int codigo, String nome, String morada, String email, double frequenciaCardiacaMedia) {
        this.codigo = codigo;
        this.nome = nome;
        this.morada = morada;
        this.email = email;
        this.frequenciaCardiacaMedia = frequenciaCardiacaMedia;
        this.atividadesRealizadas = new HashMap<>();
        this.planosTreino = new HashMap<>();
        this.atividadesIsoladas = new HashMap<>();
    }

    public Utilizador(Utilizador utilizador) {
        this.codigo = utilizador.getCodigo();
        this.nome = utilizador.getNome();
        this.morada = utilizador.getMorada();
        this.email = utilizador.getEmail();
        this.frequenciaCardiacaMedia = utilizador.getFrequenciaCardiacaMedia();
        this.fatorMultiplicativoBase = utilizador.getFatorMultiplicativoBase();
        this.atividadesRealizadas = new HashMap<>(utilizador.getAtividadesRealizadas());
        this.planosTreino = utilizador.getPlanosTreino().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
        this.atividadesIsoladas = new HashMap<>(utilizador.getAtividadesIsoladas());
    }

    /**
     * Getters
     */
    public int getCodigo() {
        return this.codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public String getMorada() {
        return this.morada;
    }

    public String getEmail() {
        return this.email;
    }

    public double getFrequenciaCardiacaMedia() {
        return this.frequenciaCardiacaMedia;
    }

    public Map<String, Atividade> getAtividadesRealizadas() {
        return new HashMap<>(this.atividadesRealizadas);
    }

    public Map<String, PlanoTreino> getPlanosTreino() {
        return new HashMap<>(this.planosTreino);
    }

    public Map<String, Atividade> getAtividadesIsoladas() {
        return new HashMap<>(this.atividadesIsoladas);
    }

    public double getFatorMultiplicativoBase() {
        return this.fatorMultiplicativoBase;
    }

    /**
     * Setters
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFrequenciaCardiacaMedia(double frequenciaCardiacaMedia) {
        this.frequenciaCardiacaMedia = frequenciaCardiacaMedia;
    }

    public void setAtividadesRealizadas(Map<String, Atividade> atividadesRealizadas) {
        this.atividadesRealizadas = new HashMap<>(atividadesRealizadas);
    }

    public void setPlanosTreino(Map<String, PlanoTreino> planosTreino) {
        this.planosTreino = planosTreino.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setAtividadesIsoladas(Map<String, Atividade> atividadesIsoladas) {
        this.atividadesIsoladas = new HashMap<>(atividadesIsoladas);
    }

    public void setFatorMultiplicativoBase(double fatorMultiplicativoBase) {
        this.fatorMultiplicativoBase = fatorMultiplicativoBase;
    }

    public String toString() {
        return "Utilizador{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", morada='" + morada + '\'' +
                ", email='" + email + '\'' +
                ", frequenciaCardiacaMedia=" + frequenciaCardiacaMedia +
                ", atividadesRealizadas=" + atividadesRealizadas.values() +
                ", planosTreino=" + planosTreino.values()+
                ", atividadesIsoladas=" + atividadesIsoladas.values() +
                '}';
    }

    public Utilizador clone() {
        return new Utilizador(this);
    }

    /**
     * Métodos
     */

    public void registarAtividade(Atividade atividade) {
        atividadesRealizadas.put(atividade.getId(), atividade.clone());
    }

    public void addPlanoTreino(PlanoTreino plano) {
        planosTreino.put(plano.getId(), plano.clone());
    }

    public void addAtivadeIsolada(Atividade atividade) {
        atividadesIsoladas.put(atividade.getId(), atividade.clone());
    }

    public void removeAtividadeIsolada(String id) {
        atividadesIsoladas.remove(id);
    }
}


