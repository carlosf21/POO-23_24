public class Abdominais extends Repeticoes{
    public Abdominais(Repeticoes atividade) {
        super(atividade);
        this.setNome("Abdominais");
    }

    public Abdominais() {
        super();
        this.setNome("Abdominais");
    }

    public Abdominais(int repeticoes) {
        super();
        this.setRepeticoes(repeticoes);
        this.setNome("Abdominais");
    }

    @Override
    public Atividade clone(){
        return new Abdominais(this);
    }
}