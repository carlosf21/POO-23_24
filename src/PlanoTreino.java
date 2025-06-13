import java.util.UUID;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanoTreino implements Serializable {
    private String id;
    private LocalDate dataRealizacao;
    private Map<String, AtividadePlano> atividades;

    public PlanoTreino(LocalDate dataRealizacao) {
        this.id = UUID.randomUUID().toString();
        this.dataRealizacao = dataRealizacao;
        this.atividades = new HashMap<>();
    }

    public PlanoTreino(PlanoTreino p) {
        this.id = p.getId();
        this.dataRealizacao = p.getDataRealizacao();
        this.atividades = p.getAtividades().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public PlanoTreino() {
        this.id = UUID.randomUUID().toString();
        this.dataRealizacao = null;
        this.atividades = new HashMap<>();
    }


    public void adicionarAtividadePlano(Atividade atividade, int repeticoes) {
        atividades.put(atividade.getId(), new AtividadePlano(atividade.clone(), repeticoes));
    }

    public PlanoTreino clone() {
        return new PlanoTreino(this);
    }

    public String toString() {
        return "PlanoTreino{" +
                "dataRealizacao=" + dataRealizacao +
                ", atividades=" + atividades.values() +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public Map<String, AtividadePlano> getAtividades() {
        return atividades.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }
}


