public class Levantamento_pesos extends RepeticoesPesos{
    public Levantamento_pesos(RepeticoesPesos atividade) {
        super(atividade);
        this.setNome("Levantamento_pesos");
    }

    public Levantamento_pesos() {
        super();
        this.setNome("Levantamento_pesos");
    }

    public Levantamento_pesos(int repeticoes, double peso) {
        super();
        this.setRepeticoes(repeticoes);
        this.setPeso(peso);
        this.setNome("Levantamento_pesos");
    }

    @Override
    public Atividade clone(){
        return new Levantamento_pesos(this);
    }
}
