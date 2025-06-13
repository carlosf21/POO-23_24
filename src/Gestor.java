import Exceptions.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class Gestor implements Serializable {
    private Map<Integer, Utilizador> utilizadores;
    private Map<String, Recorde> recordes;
    private Map<String, Atividade> atividades;
    private Map<String, PlanoTreino> planosTreino;


    public Gestor() {
        this.utilizadores = new HashMap<>();
        this.recordes = new HashMap<>();
        this.atividades = new HashMap<>();
        this.planosTreino = new HashMap<>();
    }

    public void saveState(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            System.out.println("Estado guardado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Gestor loadState(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Gestor gestor = (Gestor) inputStream.readObject();
            System.out.println("Estado carregado com sucesso.");
            return gestor;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<Utilizador> getUtilizadores() {
        return this.utilizadores.values().stream().map(Utilizador::clone).collect(Collectors.toList());
    }

    public void adicionarUtilizador(Utilizador utilizador) {
        this.utilizadores.put(utilizador.getCodigo(), utilizador);

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            this.atividades.put(atividade.getId(), atividade.clone());
        }

        for (PlanoTreino plano : utilizador.getPlanosTreino().values()) {
            this.planosTreino.put(plano.getId(), plano.clone());
        }
    }


    public void removerUtilizador(int codigo) throws UtilizadorNotFoundException {
        if (!utilizadores.containsKey(codigo)) {
            throw new UtilizadorNotFoundException("Utilizador " + codigo + " não encontrado.");
        }
        this.utilizadores.remove(codigo);
    }

    public void printBestRecords() {
        System.out.println("Recordes:");

        for (Map.Entry<String, Recorde> entry : recordes.entrySet()) {
            String activityName = entry.getKey();
            Recorde record = entry.getValue();
            Utilizador user = record.getUser();
            Atividade activity = record.getActivity();

            System.out.println("Atividade: " + activityName);
            System.out.println("Utilizador: " + user.getNome());
            System.out.println("Calorias consumidas: " + activity.getCaloriasConsumidas());
            System.out.println("-------------");
        }
    }

    public List<Atividade> getAtividadesPrevistas(LocalDateTime dataLimite, Map<String, Atividade> atividadesIsoladas, Map<String, PlanoTreino> planosTreino) {
        List<Atividade> atividadesPrevistas = new ArrayList<>();

        // Iterate over atividadesIsoladas values
        for (Atividade atividade : atividadesIsoladas.values()) {
            if (atividade.getData().isBefore(dataLimite)) {
                atividadesPrevistas.add(atividade.clone());
            }
        }

        // Iterate over planosTreino values
        for (PlanoTreino plano : planosTreino.values()) {
            for (AtividadePlano atividadePlano : plano.getAtividades().values()) {
                if (atividadePlano.getAtividadeBase().getData().isBefore(dataLimite)) {
                    atividadesPrevistas.add(atividadePlano.getAtividadeBase().clone());
                }
            }
        }

        return atividadesPrevistas;
    }


    public void registarAtividade(Utilizador utilizador, Atividade atividade) {
        this.atividades.put(atividade.getId(), atividade.clone());
        utilizador.registarAtividade(atividade);
    }

    public void executarAtividades(LocalDateTime dataLimite) {
        for (Utilizador utilizador : utilizadores.values()) {
            List<Atividade> atividadesPrevistas = getAtividadesPrevistas(dataLimite, utilizador.getAtividadesIsoladas(), utilizador.getPlanosTreino());
            for (Atividade atividade : atividadesPrevistas) {
                    executarAtividade(utilizador, atividade);
                    utilizador.removeAtividadeIsolada(atividade.getId());
            }
        }
    }

    public void executarAtividade(Utilizador utilizador, Atividade atividade) {

        double frequencia = atividade.calcularFrequenciaAtividade(utilizador);
        double caloriasConsumidas = atividade.calcularCaloriasConsumidas(utilizador, frequencia);

        Atividade atividadeFinal = atividade.clone();
        atividadeFinal.setFrequenciaCardiacaMedia(frequencia);
        atividadeFinal.setCaloriasConsumidas(caloriasConsumidas);
        this.registarAtividade(utilizador,atividadeFinal);
        updateBestActivity(atividadeFinal, utilizador);
    }

    private void updateBestActivity(Atividade activity, Utilizador user) {
        String activityName = activity.getNome();
        if (!recordes.containsKey(activityName)) {
            recordes.put(activityName, new Recorde(user, activity));
        } else {
            Recorde currentRecord = recordes.get(activityName);
            double currentCalories = currentRecord.getActivity().getCaloriasConsumidas();
            double newCalories = activity.getCaloriasConsumidas();
            if (newCalories > currentCalories) {
                recordes.put(activityName, new Recorde(user, activity));
            }
        }
    }

    public Utilizador utilizadorComMaisAtividades(LocalDateTime inicio, LocalDateTime fim) {
        Utilizador utilizadorComMaisAtividades = null;
        int maxAtividades = 0;

        for (Utilizador utilizador : utilizadores.values()) {
            int numAtividades = contarAtividadesNoPeriodo(utilizador, inicio, fim);
            if (numAtividades > maxAtividades) {
                maxAtividades = numAtividades;
                utilizadorComMaisAtividades = utilizador;
            }
        }

        return utilizadorComMaisAtividades;
    }

    private int contarAtividadesNoPeriodo(Utilizador utilizador, LocalDateTime inicio, LocalDateTime fim) {
        int numAtividades = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            LocalDateTime dataAtividade = atividade.getData();
            if (dataAtividade.isAfter(inicio) && dataAtividade.isBefore(fim)) {
                numAtividades++;
            }
        }

        return numAtividades;
    }


    public Utilizador utilizadorComMaisAtividadesDesdeSempre() {
        return utilizadores.values()
                .stream()
                .max(Comparator.comparingInt(u -> u.getAtividadesRealizadas().size()))
                .orElse(null);
    }

    public Utilizador utilizadorQueGastouMaisCaloriasNoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Utilizador utilizadorComMaisCalorias = null;
        double maxCalorias = 0;

        for (Utilizador utilizador : utilizadores.values()) {
            double caloriasUtilizador = calcularCaloriasGastasNoPeriodo(utilizador, inicio, fim);
            if (caloriasUtilizador > maxCalorias) {
                maxCalorias = caloriasUtilizador;
                utilizadorComMaisCalorias = utilizador;
            }
        }

        return utilizadorComMaisCalorias;
    }

    private double calcularCaloriasGastasNoPeriodo(Utilizador utilizador, LocalDateTime inicio, LocalDateTime fim) {
        double totalCalorias = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            LocalDateTime dataAtividade = atividade.getData();
            if (dataAtividade.isAfter(inicio) && dataAtividade.isBefore(fim)) {
                totalCalorias += atividade.getCaloriasConsumidas();
            }
        }

        return totalCalorias;
    }


    public Utilizador utilizadorQueGastouMaisCaloriasDesdeSempre() {
        Utilizador utilizadorComMaisCalorias = null;
        double maxCalorias = 0;

        for (Utilizador utilizador : utilizadores.values()) {
            double caloriasUtilizador = calcularTotalCaloriasGastas(utilizador);
            if (caloriasUtilizador > maxCalorias) {
                maxCalorias = caloriasUtilizador;
                utilizadorComMaisCalorias = utilizador;
            }
        }

        return utilizadorComMaisCalorias;
    }

    private double calcularTotalCaloriasGastas(Utilizador utilizador) {
        double totalCalorias = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            totalCalorias += atividade.getCaloriasConsumidas();
        }

        return totalCalorias;
    }


    public int getUltimoCodigoUtilizador() {
        if (utilizadores.isEmpty()) {
            return 0;
        } else {
            Utilizador ultimoUtilizador = utilizadores.values().iterator().next();
            for (Utilizador utilizador : utilizadores.values()) {
                if (utilizador.getCodigo() > ultimoUtilizador.getCodigo()) {
                    ultimoUtilizador = utilizador;
                }
            }
            return ultimoUtilizador.getCodigo();
        }
    }

    public Utilizador getUtilizadorPorCodigo(int codigo) {
        return utilizadores.get(codigo);
    }

    public String tipoAtividadeMaisRealizada() {
        Map<String, Integer> contadorAtividades = new HashMap<>();

        // Contar a frequência de cada nome de atividade
        for (Atividade atividade : this.atividades.values()) {
            String nomeAtividade = atividade.getNome();
            contadorAtividades.put(nomeAtividade, contadorAtividades.getOrDefault(nomeAtividade, 0) + 1);
        }

        // Encontrar o nome de atividade com maior frequência
        String atividadeMaisRealizada = null;
        int maxFrequencia = 0;
        for (Map.Entry<String, Integer> entry : contadorAtividades.entrySet()) {
            String nomeAtividade = entry.getKey();
            int frequencia = entry.getValue();
            if (frequencia > maxFrequencia) {
                atividadeMaisRealizada = nomeAtividade;
                maxFrequencia = frequencia;
            }
        }

        return atividadeMaisRealizada;
    }

    public double quilometrosRealizadosPorUtilizadorNoPeriodo(Utilizador utilizador, LocalDateTime inicio, LocalDateTime fim) {
        double totalQuilometros = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            LocalDateTime dataAtividade = atividade.getData();
            if (dataAtividade.isAfter(inicio) && dataAtividade.isBefore(fim) && atividade instanceof Distancia) {
                totalQuilometros += ((Distancia) atividade).getDistancia();
            }
            if (atividade instanceof DistanciaAltimetria) {
                totalQuilometros += ((DistanciaAltimetria) atividade).getDistancia();
            }
        }

        return totalQuilometros;
    }

    public double quilometrosRealizadosPorUtilizadorDesdeSempre(Utilizador utilizador) {
        double totalQuilometros = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            if (atividade instanceof Distancia) {
                totalQuilometros += ((Distancia) atividade).getDistancia();
            }
            if (atividade instanceof DistanciaAltimetria) {
                totalQuilometros += ((DistanciaAltimetria) atividade).getDistancia();
            }
        }

        return totalQuilometros;
    }

    public double altimetriaTotalNoPeriodo(Utilizador utilizador, LocalDateTime inicio, LocalDateTime fim) {
        double altimetriaTotal = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            LocalDateTime dataAtividade = atividade.getData();
            if (dataAtividade.isAfter(inicio) && dataAtividade.isBefore(fim) && atividade instanceof DistanciaAltimetria) {
                altimetriaTotal += ((DistanciaAltimetria) atividade).getAltimetria();
            }
        }

        return altimetriaTotal;
    }

    public double altimetriaTotalDesdeSempre(Utilizador utilizador) {
        double altimetriaTotal = 0;

        for (Atividade atividade : utilizador.getAtividadesRealizadas().values()) {
            if (atividade instanceof DistanciaAltimetria) {
                altimetriaTotal += ((DistanciaAltimetria) atividade).getAltimetria();
            }
        }

        return altimetriaTotal;
    }

    public PlanoTreino planoMaisExigente(double caloriasPropostas) {
        PlanoTreino planoMaisExigente = null;
        double menorDiferenca = Double.MAX_VALUE;

        for (Utilizador utilizador : utilizadores.values()) {
            for (PlanoTreino plano : utilizador.getPlanosTreino().values()) {
                double caloriasPlano = calcularCaloriasPlano(plano,utilizador);
                double diferenca = Math.abs(caloriasPlano - caloriasPropostas);
                if (diferenca < menorDiferenca) {
                    menorDiferenca = diferenca;
                    planoMaisExigente = plano;
                }
            }
        }

        return planoMaisExigente;
    }

    private double calcularCaloriasPlano(PlanoTreino plano, Utilizador utilizador) {
        double totalCalorias = 0;
        double frequencia = 0;

        for (AtividadePlano atividadePlano : plano.getAtividades().values()) {
            frequencia = atividadePlano.getAtividadeBase().calcularFrequenciaAtividade(utilizador);
            totalCalorias += atividadePlano.getAtividadeBase().calcularCaloriasConsumidas(utilizador, frequencia);
        }

        return totalCalorias;
    }

    public String listarAtividadesDoUtilizador(Utilizador utilizador) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Atividades realizadas pelo usuário:\n");
        stringBuilder.append("------------------------------------\n");
        stringBuilder.append(listarAtividades(utilizador.getAtividadesRealizadas()));

        stringBuilder.append("\nAtividades isoladas do usuário:\n");
        stringBuilder.append("---------------------------------\n");
        stringBuilder.append(listarAtividades(utilizador.getAtividadesIsoladas()));

        stringBuilder.append("\nAtividades dos planos de treino do usuário:\n");
        stringBuilder.append("--------------------------------------------\n");
        for (Map.Entry<String, PlanoTreino> entry : utilizador.getPlanosTreino().entrySet()) {
            String planoNome = entry.getKey();
            PlanoTreino plano = entry.getValue();
            stringBuilder.append("Plano: ").append(planoNome).append("\n");
            for (AtividadePlano atividadePlano : plano.getAtividades().values()) {
                stringBuilder.append("Nome: ").append(atividadePlano.getAtividadeBase().getNome()).append("\n");
                stringBuilder.append("Data: ").append(atividadePlano.getAtividadeBase().getData()).append("\n");
                stringBuilder.append("Calorias Consumidas: ").append(atividadePlano.getAtividadeBase().getCaloriasConsumidas()).append("\n");
                stringBuilder.append("------------------------------------\n");
            }
        }

        return stringBuilder.toString();
    }




    private String listarAtividades(Map<String, Atividade> atividades) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Atividade> entry : atividades.entrySet()) {
            String atividadeNome = entry.getKey();
            Atividade atividade = entry.getValue();
            stringBuilder.append("Nome: ").append(atividade.getNome()).append("\n");
            stringBuilder.append("Data: ").append(atividade.getData()).append("\n");
            stringBuilder.append("Calorias Consumidas: ").append(atividade.getCaloriasConsumidas()).append("\n");
            stringBuilder.append("------------------------------------\n");
        }
        return stringBuilder.toString();
    }


    public void addPlanoTreino(int codigoUtilizador, PlanoTreino plano) throws UtilizadorNotFoundException {
        Utilizador utilizador = this.utilizadores.get(codigoUtilizador);

        if (utilizador != null) {
            utilizador.addPlanoTreino(plano);
            planosTreino.put(plano.getId(), plano);
        } else {
            throw new UtilizadorNotFoundException();
        }
    }

    public void removePlanoTreino(String id) throws PlanoTreinoNotFoundException{
        if(!planosTreino.containsKey(id)){
            throw new PlanoTreinoNotFoundException("Plano "+id+" não encontrado.");
        }
        this.planosTreino.remove(id);
    }

    public Map<String, Atividade> getAtividadesRealizadas() {
        return this.atividades;
    }

    public Map<String, PlanoTreino> getPlanosTreino() {
        return this.planosTreino;
    }

    public void removeAtividade(String id) throws AtividadeNotFoundException{
        if(!atividades.containsKey(id)){
            throw new AtividadeNotFoundException();
        }
        this.atividades.remove(id);
    }

    public void adicionarAtividadeUtilizador(Utilizador utilizador, Atividade atividade) throws UtilizadorNotFoundException {
        this.atividades.put(atividade.getId(), atividade.clone());

        int userCode = utilizador.getCodigo();

        Utilizador userToUpdate = this.utilizadores.get(userCode);
        if (userToUpdate != null) {
            userToUpdate.addAtivadeIsolada(atividade);
            this.utilizadores.put(userCode, userToUpdate);
        } else {
            throw new UtilizadorNotFoundException();
        }
    }

    public void criarPlanoTreino(Class<? extends Atividade> tipoAtividadeEscolhido,
                                 LocalDate dataRealizacao, int numeroMaximoAtividadesPorDia, int recorrenciaSemanal,
                                 double consumoCaloricoMinimo, Utilizador utilizador) throws UtilizadorNotFoundException {
        PlanoTreino planoTreino;
        while(true){
            planoTreino = criarPlanoTreinoSimples(tipoAtividadeEscolhido, dataRealizacao, numeroMaximoAtividadesPorDia, recorrenciaSemanal, consumoCaloricoMinimo, utilizador);
            if(planoTreino != null){
                addPlanoTreino(utilizador.getCodigo(),planoTreino);
                return;
            }
        }

    }


    private PlanoTreino criarPlanoTreinoSimples(Class<? extends Atividade> tipoAtividadeEscolhido,
                                                LocalDate dataRealizacao, int numeroMaximoAtividadesPorDia, int recorrenciaSemanal,
                                                double consumoCaloricoMinimo,Utilizador utilizador) {
        List<Class<? extends Atividade>> subclasses = getSubclasses(tipoAtividadeEscolhido);

        PlanoTreino planoTreino = new PlanoTreino(dataRealizacao);

        int diasSemana = DayOfWeek.values().length;
        int[] contadorDiasSemana = new int[diasSemana];

        Map<Class<? extends Atividade>, Integer> atividadesAdicionadasPorTipo = new HashMap<>();

        for (Class<? extends Atividade> subclass : subclasses) {
            atividadesAdicionadasPorTipo.put(subclass, 0);
        }

        double totalCaloriesConsumed = 0;

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            for (Class<? extends Atividade> subclass : subclasses) {
                if (atividadesAdicionadasPorTipo.get(subclass) >= recorrenciaSemanal) {
                    continue;
                }


                Atividade atividade = criarAtividade(subclass);
                atividade.setTempo(new Random().nextInt(120) + 5);


                LocalTime availableTime = getNextAvailableTime(dayOfWeek, contadorDiasSemana[dayOfWeek.getValue() - 1]);
                atividade.setData(dataRealizacao.with(TemporalAdjusters.nextOrSame(dayOfWeek)).atTime(availableTime));


                planoTreino.adicionarAtividadePlano(atividade, 1);

                // Update ao contador do tipo de atividade (Recorrencia semanal)
                int activityCountForWeek = atividadesAdicionadasPorTipo.get(subclass);
                atividadesAdicionadasPorTipo.put(subclass, activityCountForWeek + 1);
                contadorDiasSemana[dayOfWeek.getValue() - 1]++;

                // Calcular as calorias consumidas
                double caloriesConsumed = atividade.calcularCaloriasConsumidas(utilizador, atividade.calcularFrequenciaAtividade(utilizador));
                totalCaloriesConsumed += caloriesConsumed;

                if (contadorDiasSemana[dayOfWeek.getValue() - 1] >= numeroMaximoAtividadesPorDia) {
                    break;
                }
            }
        }

        // Se o total de calorias pretendidas for menor  que o pretendido retorna null
        if (totalCaloriesConsumed < consumoCaloricoMinimo) {
            return null;
        }

        return planoTreino;
    }




    private LocalTime getNextAvailableTime(DayOfWeek dayOfWeek, int activityCount) {
        return LocalTime.of(6, 0).plusHours(activityCount * 2L);
    }


    public Atividade criarAtividade(Class<? extends Atividade> subclass) {
        try {
            double distancia = 0.0;
            double altimetria = 0.0;
            double valorPeso = 0.0;
            int repeticoes = 0;

            // Verificar o tipo da subclasse e atribuir valores aos atributos conforme necessário
            if (Distancia.class.isAssignableFrom(subclass)) {
                distancia = getValorDistancia();
            } else if (RepeticoesPesos.class.isAssignableFrom(subclass)) {
                repeticoes = getValorRepeticoes();
                valorPeso = getValorPeso();
            } else if(Repeticoes.class.isAssignableFrom(subclass)){
                repeticoes = getValorRepeticoes();
            }
            else {
                distancia = getValorDistancia();
                altimetria = getValorAltimetria();
            }

            // Obter o construtor adequado
            Constructor<? extends Atividade> constructor;
            if (Distancia.class.isAssignableFrom(subclass)) {
                constructor = subclass.getDeclaredConstructor(double.class);
                return constructor.newInstance(distancia);
            } else if (RepeticoesPesos.class.isAssignableFrom(subclass)) {
                constructor = subclass.getDeclaredConstructor(int.class, double.class);
                return constructor.newInstance(repeticoes, valorPeso);
            } else if (DistanciaAltimetria.class.isAssignableFrom(subclass)) {
                constructor = subclass.getDeclaredConstructor(double.class, double.class);
                return constructor.newInstance(distancia,altimetria);
            }else{
                constructor = subclass.getDeclaredConstructor(int.class);
                return constructor.newInstance(repeticoes);
            }

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    public double getValorDistancia() {
            return new Random().nextDouble(10) + 1;
    }

    public double getValorPeso() {
        return new Random().nextDouble(150) +5;
    }

    public int getValorRepeticoes() {
        return new Random().nextInt(50) + 5;
    }

    public int getValorAltimetria() {
        return new Random().nextInt(800) + 200;
    }


    public List<Class<? extends Atividade>> getSubclasses(Class<? extends Atividade> parentClass) {
        List<Class<? extends Atividade>> subclasses = new ArrayList<>();
        try {
            String classpath = System.getProperty("java.class.path");
            String[] classpathEntries = classpath.split(File.pathSeparator);
            for (String classpathEntry : classpathEntries) {
                File file = new File(classpathEntry);
                if (file.isDirectory()) {
                    findClassesInDirectory(file, "", parentClass, subclasses);
                } else if (file.getName().endsWith(".jar")) {
                    // You can implement searching in JAR files if needed
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subclasses;
    }

    private void findClassesInDirectory(File directory, String packageName, Class<? extends Atividade> parentClass, List<Class<? extends Atividade>> subclasses) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClassesInDirectory(file, packageName + file.getName() + ".", parentClass, subclasses);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (parentClass.isAssignableFrom(clazz) && !clazz.equals(parentClass)) {
                            subclasses.add(clazz.asSubclass(Atividade.class));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
