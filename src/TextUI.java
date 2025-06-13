import Exceptions.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TextUI {
    private Gestor gestor;

    private LocalDate dataAtual = LocalDate.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TextUI() {
        this.gestor = new Gestor();
    }

    public void run() throws UtilizadorNotFoundException, AtividadeNotFoundException {
        this.gestor = Gestor.loadState("estado_atual.dat");
        Scanner scanner = new Scanner(System.in);

        NewMenu menu = new NewMenu(new String[]{
                "Realizar atividades",
                "Visualizar Registos",
                "Visualizar Recordes",
                "Visualizar Estatísticas",
                "Gerar Plano de treino"
        });

        menu.setHandler(1, () -> {
            System.out.println("Data atual: "+dataAtual);
            System.out.print("Digite a data para até qual deseja realizar as atividades (no formato AAAA-MM-DD HH:mm): ");
            String input = scanner.nextLine();
            try {
                LocalDateTime dataEscolhida = LocalDateTime.parse(input, formatter);
                gestor.executarAtividades(dataEscolhida);
                dataAtual = LocalDate.from(dataEscolhida);

            } catch (Exception e) {
                System.out.println("Data inválida. Tente novamente.");
            }
        });

        menu.setHandler(2, () -> {
            for (Utilizador utilizador : gestor.getUtilizadores()) {
                System.out.println(utilizador.toString());
            }
        });

        menu.setHandler(3, () -> {
            gestor.printBestRecords();
        });

        menu.setHandler(4, () -> {
                    NewMenu menuStats = new NewMenu(new String[]{   "Visualizar utilizador com mais atividades",
                                                                    "Visualizar utilizador com mais calorias gastas",
                                                                    "Visualizar a atividade mais realizada",
                                                                    "Visualizar Kms percorridos por utilizador",
                                                                    "Visualizar metros de altimetria totalizados por utilizador",
                                                                    "Visualizar plano de treino mais exigente para calorias propostas",
                                                                    "Visualizar lista de atividades de um utilizador"});

                    //Mais Atividades
                    menuStats.setHandler(1, () -> {
                        System.out.print("Digite a data de início do período (no formato AAAA-MM-DD HH:mm) ou 'sempre' para desde sempre: ");
                        String inicioInput = scanner.nextLine();

                        if (inicioInput.equalsIgnoreCase("sempre")) {
                            // Get the user with the most activities since forever
                            Utilizador utilizadorMaisAtividadesSempre = gestor.utilizadorComMaisAtividadesDesdeSempre();
                            if (utilizadorMaisAtividadesSempre != null) {
                                System.out.println("Utilizador com mais atividades desde sempre:");
                                System.out.println(utilizadorMaisAtividadesSempre);
                            } else {
                                System.out.println("Não há utilizadores com atividades.");
                            }
                        } else {
                            System.out.print("Digite a data de fim do período (no formato AAAA-MM-DD HH:mm): ");
                            String fimInput = scanner.nextLine();
                            try {
                                LocalDateTime dataInicio = LocalDateTime.parse(inicioInput, formatter);
                                LocalDateTime dataFim = LocalDateTime.parse(fimInput, formatter);

                                // Encontrar o utilizador com mais atividades no período escolhido
                                Utilizador utilizadorMaisAtividades = gestor.utilizadorComMaisAtividades(dataInicio, dataFim);
                                if (utilizadorMaisAtividades != null) {
                                    System.out.println("Utilizador com mais atividades no período de " + dataInicio + " a " + dataFim + ":");
                                    System.out.println(utilizadorMaisAtividades);
                                } else {
                                    System.out.println("Não há utilizadores com atividades no período de " + dataInicio + " a " + dataFim);
                                }
                            } catch (DateTimeParseException e) {
                                System.out.println("Data inválida. Tente novamente.");
                            }
                        }
                    });
                    menuStats.setHandler(2, () -> {
                        System.out.print("Digite a data de início do período (no formato AAAA-MM-DD HH:mm) ou 'sempre' para desde sempre: ");
                        String inicioInput = scanner.nextLine();

                        if (inicioInput.equalsIgnoreCase("sempre")) {
                            // Obter o utilizador com mais calorias queimadas desde sempre
                            Utilizador utilizadorMaisCaloriasSempre = gestor.utilizadorQueGastouMaisCaloriasDesdeSempre();
                            if (utilizadorMaisCaloriasSempre != null) {
                                System.out.println("Utilizador com mais calorias queimadas desde sempre:");
                                System.out.println(utilizadorMaisCaloriasSempre);
                            } else {
                                System.out.println("Não há utilizadores com calorias queimadas.");
                            }
                        } else {
                            System.out.print("Digite a data de fim do período (no formato AAAA-MM-DD HH:mm): ");
                            String fimInput = scanner.nextLine();
                            try {
                                LocalDateTime dataInicio = LocalDateTime.parse(inicioInput, formatter);
                                LocalDateTime dataFim = LocalDateTime.parse(fimInput, formatter);

                                // Encontrar o utilizador com mais calorias queimadas no período escolhido
                                Utilizador utilizadorMaisCalorias = gestor.utilizadorQueGastouMaisCaloriasNoPeriodo(dataInicio, dataFim);
                                if (utilizadorMaisCalorias != null) {
                                    System.out.println("Utilizador com mais calorias queimadas no período de " + dataInicio + " a " + dataFim + ":");
                                    System.out.println(utilizadorMaisCalorias);
                                } else {
                                    System.out.println("Não há utilizadores com calorias queimadas no período de " + dataInicio + " a " + dataFim);
                                }
                            } catch (DateTimeParseException e) {
                                System.out.println("Data inválida. Tente novamente.");
                            }
                        }
                    });
                    menuStats.setHandler(3, () -> {
                            String tipoAtividadeMaisRealizadaSempre = gestor.tipoAtividadeMaisRealizada();
                            if (tipoAtividadeMaisRealizadaSempre != null) {
                                System.out.println("Tipo de atividade mais realizada: " + tipoAtividadeMaisRealizadaSempre);
                            } else {
                                System.out.println("Não há atividades realizadas.");
                            }
                    });
                    menuStats.setHandler(4, () -> {
                        System.out.print("Digite o código do utilizador: ");
                        int number = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Digite a data de início do período (no formato AAAA-MM-DD HH:mm) ou 'sempre' para desde sempre: ");
                        String inicioInput = scanner.nextLine();

                        if (inicioInput.equalsIgnoreCase("sempre")) {
                            double totalQuilometros = gestor.quilometrosRealizadosPorUtilizadorDesdeSempre(gestor.getUtilizadorPorCodigo(number));
                                System.out.println("Total de quilómetros: " + totalQuilometros);
                        } else {
                            System.out.print("Digite a data de fim do período (no formato AAAA-MM-DD HH:mm): ");
                            String fimInput = scanner.nextLine();
                            try {
                                LocalDateTime dataInicio = LocalDateTime.parse(inicioInput, formatter);
                                LocalDateTime dataFim = LocalDateTime.parse(fimInput, formatter);

                                double totalQuilometrosPeriodo = gestor.quilometrosRealizadosPorUtilizadorNoPeriodo(gestor.getUtilizadorPorCodigo(number), dataInicio, dataFim);
                                System.out.println("Total de quilometros: " + totalQuilometrosPeriodo);
                            } catch (DateTimeParseException e) {
                                System.out.println("Data inválida. Tente novamente.");
                            }
                        }
                    });
                    menuStats.setHandler(5, () -> {
                        System.out.print("Digite o código do utilizador: ");
                        int number = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Digite a data de início do período (no formato AAAA-MM-DD HH:mm) ou 'sempre' para desde sempre: ");
                        String inicioInput = scanner.nextLine();

                        if (inicioInput.equalsIgnoreCase("sempre")) {
                            double totalAltimetria = gestor.altimetriaTotalDesdeSempre(gestor.getUtilizadorPorCodigo(number));
                            System.out.println("Total de metros de altimetria: " + totalAltimetria);
                        } else {
                            System.out.print("Digite a data de fim do período (no formato AAAA-MM-DD HH:mm): ");
                            String fimInput = scanner.nextLine();
                            try {
                                LocalDateTime dataInicio = LocalDateTime.parse(inicioInput, formatter);
                                LocalDateTime dataFim = LocalDateTime.parse(fimInput, formatter);

                                double totalAltimetriaPeriodo = gestor.altimetriaTotalNoPeriodo(gestor.getUtilizadorPorCodigo(number),dataInicio, dataFim);
                                System.out.println("Total de metros de altimetria: " + totalAltimetriaPeriodo);
                            } catch (DateTimeParseException e) {
                                System.out.println("Data inválida. Tente novamente.");
                            }
                        }
                    });
                    menuStats.setHandler(6, () -> {
                        System.out.print("Digite o número de calorias pretendidas: ");
                        double calorias = scanner.nextDouble();
                        scanner.nextLine();
                        PlanoTreino plano = gestor.planoMaisExigente(calorias);
                        System.out.println(plano.toString());
                    });
                    menuStats.setHandler(7, () -> {
                        System.out.print("Digite o código de utilizador: ");
                        int codigo = scanner.nextInt();
                        scanner.nextLine();
                        String result = gestor.listarAtividadesDoUtilizador(gestor.getUtilizadorPorCodigo(codigo));
                        System.out.println(result);
                    });
                    menuStats.run();
        });
        menu.setHandler(5, () -> {
            System.out.println("Digite tipo de plano que pretende (Distancia, DistanciaAltimetria, Repeticoes ou RepeticoesPesos: ");
            String tipoAtividadeName = scanner.nextLine();
            Class<? extends Atividade> tipoAtividade = null;
            try {
                tipoAtividade = (Class<? extends Atividade>) Class.forName(tipoAtividadeName);
            } catch (ClassNotFoundException e) {
                throw new AtividadeNotFoundException();
            }

            System.out.println("Digite a data de realização (YYYY-MM-DD): ");
            String dataRealizacaoStr = scanner.nextLine();
            LocalDate dataRealizacao = LocalDate.parse(dataRealizacaoStr);
            int parametro1;
            do {
                System.out.println("Digite o número máximo de atividades por dia (máximo 3): ");
                parametro1 = scanner.nextInt();
                scanner.nextLine();

                if (parametro1 > 3) {
                    System.out.println("Por favor, digite um número até 3.");
                }
            } while (parametro1 > 3);
            System.out.println("Digite a recorrência semanal das atividades: ");
            int parametro2 = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Digite o consumo calórico mínimo: ");
            int parametro3 = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Digite o código do utilizador: ");
            int codigo = scanner.nextInt();
            scanner.nextLine();

            gestor.criarPlanoTreino(tipoAtividade, dataRealizacao, parametro1, parametro2, parametro3, gestor.getUtilizadorPorCodigo(codigo));
            System.out.println("Plano de treino criado com sucesso!");
        });


        menu.run();


        gestor.saveState("estado_atual.dat");
        System.out.println("Programa encerrado.");

        scanner.close();
    }

}


