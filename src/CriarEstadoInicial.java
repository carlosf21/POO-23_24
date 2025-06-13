import Exceptions.*;

import java.io.*;
import java.time.*;

public class CriarEstadoInicial {
    public static void main(String[] args) throws UtilizadorNotFoundException {
        Atividade corrida_estrada = new Corrida_estrada(new Distancia(new Atividade(30, LocalDateTime.of(2024, Month.APRIL, 19, 10, 10, 0)), 5.0));
        Atividade trail_monte = new Trail_monte(new DistanciaAltimetria(new Atividade(60, LocalDateTime.of(2024, Month.APRIL, 19, 15, 10, 0)), 10.0, 200));
        Atividade abdominais = new Abdominais(new Repeticoes(new Atividade(10, LocalDateTime.of(2024, Month.APRIL, 20, 10, 10, 0)), 20));
        Atividade bicicletaMontanha = new Bicicleta_montanha(new DistanciaAltimetria(new Atividade(40, LocalDateTime.of(2024, Month.APRIL, 20, 20, 10, 0)), 15.0, 200));
        Atividade bicilceta_estrada = new Bicicleta_estrada(new Distancia(new Atividade(30, LocalDateTime.of(2024, Month.APRIL, 19, 10, 10, 0)), 7));
        Atividade levantamento_pesos = new Levantamento_pesos(new RepeticoesPesos(new Atividade(40, LocalDateTime.of(2024, Month.APRIL, 20, 20, 10, 0)),15,20));
        Atividade levantamento_pesos2 = new Levantamento_pesos(new RepeticoesPesos(new Atividade(40, LocalDateTime.of(2024, Month.APRIL, 20, 15, 10, 0)),30,30));
        Gestor gestor = new Gestor();

        Utilizador utilizador1 = new Amador(new Utilizador(0,"João","Rua da Feira","joao@mail.com", 60));
        PlanoTreino plano1 = new PlanoTreino(LocalDate.of(2024,4,19));
        plano1.adicionarAtividadePlano(corrida_estrada,2);
        plano1.adicionarAtividadePlano(bicicletaMontanha,1);
        plano1.adicionarAtividadePlano(abdominais,5);
        plano1.adicionarAtividadePlano(levantamento_pesos2,2);
        utilizador1.addPlanoTreino(plano1);
        gestor.adicionarUtilizador(utilizador1);

        Utilizador utilizador2 = new Profissional(new Utilizador(1,"António","Rua 25 Abril","antonio@mail.com", 70));
        PlanoTreino plano2 = new PlanoTreino(LocalDate.of(2024,4,18));
        plano2.adicionarAtividadePlano(bicilceta_estrada,1);
        plano2.adicionarAtividadePlano(levantamento_pesos,2);
        utilizador2.addPlanoTreino(plano2);
        utilizador2.addAtivadeIsolada(trail_monte);
        gestor.adicionarUtilizador(utilizador2);

        try (FileOutputStream arquivoSaida = new FileOutputStream("estado_atual.dat");
             ObjectOutputStream saida = new ObjectOutputStream(arquivoSaida)) {
            saida.writeObject(gestor);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}