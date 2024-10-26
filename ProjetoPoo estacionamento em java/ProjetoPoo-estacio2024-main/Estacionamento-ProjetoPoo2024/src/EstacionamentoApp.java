import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class EstacionamentoApp extends Application {

    private Estacionamento estacionamento;

    @Override
    public void start(Stage primaryStage) {
        try {
            estacionamento = new Estacionamento(10); // Inicializa o estacionamento com 10 vagas
            estacionamento.lerDados(); // Lê os dados salvos de vagas ocupadas (se existirem)
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        primaryStage.setTitle("Estacionamento");

        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Componentes da interface
        TextField placaField = new TextField();
        placaField.setPromptText("Digite a placa");
        placaField.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-border-color: transparent;");

        TextField vagaField = new TextField();
        vagaField.setPromptText("Digite a vaga");
        vagaField.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-border-color: transparent;");

        Button btnEntrar = new Button("Entrar Veículo");
        btnEntrar.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-padding: 10px;");
        
        Button btnSair = new Button("Sair Veículo");
        btnSair.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-padding: 10px;");
        
        Button btnConsultar = new Button("Consultar Vaga");
        btnConsultar.setStyle("-fx-background-color: #9B59B6; -fx-text-fill: white; -fx-padding: 10px;");
        
        Label message = new Label();
        message.setStyle("-fx-text-fill: #ECF0F1;");

        Label placaLabel = new Label("Placa:");
        placaLabel.setStyle("-fx-text-fill: #ECF0F1;"); // Cor clara para destacar

        Label vagaLabel = new Label("Vaga:");
        vagaLabel.setStyle("-fx-text-fill: #ECF0F1;"); // Cor clara para destacar

        // Ação do botão de entrada
        btnEntrar.setOnAction(e -> {
            String placa = placaField.getText();
            try {
                int vaga = Integer.parseInt(vagaField.getText());
                estacionamento.entrar(placa, vaga);
                message.setText("Veículo entrou na vaga " + vaga);
                estacionamento.gravarDados(); // Atualiza o registro após a entrada
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        // Ação do botão de saída
        btnSair.setOnAction(e -> {
            try {
                int vaga = Integer.parseInt(vagaField.getText());
                estacionamento.sair(vaga);
                message.setText("Veículo saiu da vaga " + vaga);
                estacionamento.gravarDados(); // Atualiza o registro após a saída
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        // Ação do botão de consulta
        btnConsultar.setOnAction(e -> {
            String placa = placaField.getText();
            int vaga = estacionamento.consultarPlaca(placa);
            if (vaga != -1) {
                message.setText("O veículo está na vaga " + vaga);
            } else {
                message.setText("Veículo não encontrado no estacionamento.");
            }
        });

        root.getChildren().addAll(
            placaLabel, placaField,
            vagaLabel, vagaField,
            btnEntrar, btnSair, btnConsultar, message
        );

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Classe Estacionamento
    class Estacionamento {
        private String[] placas;

        public Estacionamento(int vagas) throws Exception {
            if (vagas <= 0) {
                throw new Exception("Número de vagas inválido."); // Deve ser de ao menos 1.
            }
            placas = new String[vagas]; // Define o número de vagas do estacionamento.
            for (int i = 0; i < placas.length; i++) {
                placas[i] = "livre";
            }
        }

        private boolean validarPlaca(String placa) {
            if (placa.length() != 7) {
                return false;
            }
            String letras = placa.substring(0, 3);
            String numeros = placa.substring(3, 4) + placa.substring(5);
            String letraOuNumero = placa.substring(4, 5);
            return letras.matches("[A-Z]*") && numeros.matches("[0-9]*") &&
                   (letraOuNumero.matches("[A-Z]*") || letraOuNumero.matches("[0-9]*"));
        }

        private boolean checarRepeticao(String placa) {
            for (String p : placas) {
                if (p.equals(placa)) return false;
            }
            return true;
        }

        public void entrar(String placa, int vaga) throws Exception {
            placa = placa.toUpperCase();
            if (!validarPlaca(placa)) {
                throw new Exception("Placa inválida, deve ter formato YYY0000 ou YYY0X00");
            }
            if (!checarRepeticao(placa)) {
                throw new Exception("Placa já registrada no estacionamento.");
            }
            if (vaga < 1 || vaga > placas.length) {
                throw new Exception("Escolha uma vaga entre 1 e " + placas.length);
            }
            if (!placas[vaga - 1].equals("livre")) {
                throw new Exception("Vaga ocupada.");
            }
            placas[vaga - 1] = placa;
            registrarMovimento(vaga, placa, "entrada");
        }

        public void sair(int vaga) throws Exception {
            if (vaga < 1 || vaga > placas.length) {
                throw new Exception("Escolha uma vaga entre 1 e " + placas.length);
            }
            if (placas[vaga - 1].equals("livre")) {
                throw new Exception("Vaga já está livre.");
            }
            String placa = placas[vaga - 1];
            placas[vaga - 1] = "livre";
            registrarMovimento(vaga, placa, "saida");
        }

        public int consultarPlaca(String placa) {
            for (int i = 0; i < placas.length; i++) {
                if (placas[i].equalsIgnoreCase(placa)) {
                    return i + 1;
                }
            }
            return -1;
        }

        public void gravarDados() {
            try (FileWriter arquivo = new FileWriter(new File("placas.csv"))) {
                for (int i = 0; i < placas.length; i++) {
                    if (!placas[i].equals("livre")) {
                        arquivo.write((i + 1) + ";" + placas[i] + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void lerDados() {
            try (Scanner arquivo = new Scanner(new File("placas.csv"))) {
                while (arquivo.hasNextLine()) {
                    String[] linha = arquivo.nextLine().split(";");
                    int vaga = Integer.parseInt(linha[0]);
                    placas[vaga - 1] = linha[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void registrarMovimento(int vaga, String placa, String tipo) throws Exception {
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter timestampFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = timestamp.format(timestampFormat);
            try (FileWriter arquivo = new FileWriter(new File("historico.csv"), true)) {
                arquivo.write(dataFormatada + ";" + vaga + ";" + placa + ";" + tipo + "\n");
            }
        }
    }
}
