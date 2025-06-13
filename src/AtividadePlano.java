import java.io.Serializable;

public class AtividadePlano implements Serializable {
    protected Atividade atividadeBase;
    private int iteracoes;

    public AtividadePlano(Atividade atividadeBase, int iteracoes) {
        this.atividadeBase = atividadeBase.clone();
        this.iteracoes = iteracoes;
    }

    public AtividadePlano(AtividadePlano atividade) {
        this.atividadeBase = atividade.getAtividadeBase();
        this.iteracoes = atividade.getIteracoes();
    }

    public int getIteracoes() {
        return this.iteracoes;
    }

    public void setIteracoes(int repetir) {
        this.iteracoes = repetir;
    }

    public Atividade getAtividadeBase() {
        return atividadeBase;
    }

    public void setAtividadeBase(Atividade atividadeBase) {
        this.atividadeBase = atividadeBase;
    }

    @Override
    public String toString() {
        return "AtividadePlano{" +
                "atividade=" + atividadeBase.toString() +
                ", iteracoes=" + iteracoes +
                '}';
    }

    @Override
    public AtividadePlano clone() {
        return new AtividadePlano(this);
    }
}
