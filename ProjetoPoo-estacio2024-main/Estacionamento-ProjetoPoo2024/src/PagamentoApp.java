public class PagamentoApp extends Application {
    private static final String API_URL = "https://api.mercadopago.com/v1/payment_methods";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pagamento");

        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label amountLabel = new Label("Valor a pagar: R$10,00");
        amountLabel.setStyle("-fx-text-fill: #ECF0F1;");

        ToggleGroup paymentMethods = new ToggleGroup();

        RadioButton cashOption = new RadioButton("Dinheiro");
        cashOption.setToggleGroup(paymentMethods);
        cashOption.setStyle("-fx-text-fill: #ECF0F1;");

        RadioButton creditCardOption = new RadioButton("Cartão de Crédito");
        creditCardOption.setToggleGroup(paymentMethods);
        creditCardOption.setStyle("-fx-text-fill: #ECF0F1;");

        RadioButton debitCardOption = new RadioButton("Cartão de Débito");
        debitCardOption.setToggleGroup(paymentMethods);
        debitCardOption.setStyle("-fx-text-fill: #ECF0F1;");

        RadioButton pixOption = new RadioButton("Pix");
        pixOption.setToggleGroup(paymentMethods);
        pixOption.setStyle("-fx-text-fill: #ECF0F1;");

        Button btnPagar = new Button("Pagar");
        btnPagar.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-padding: 10px;");

        Label message = new Label();
        message.setStyle("-fx-text-fill: #ECF0F1;");
        btnPagar.setOnAction(e -> {
            if (paymentMethods.getSelectedToggle() != null) {
                String paymentMethod = ((RadioButton) paymentMethods.getSelectedToggle()).getText();
                processPayment(paymentMethod, message);
            } else {
                message.setText("Por favor, selecione um método de pagamento.");
            }
        });

        root.getChildren().addAll(amountLabel, cashOption, creditCardOption, debitCardOption, pixOption, btnPagar, message);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processPayment(String paymentMethod, Label message) {
        HttpClient client = HttpClient.newHttpClient();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("amount", "10.00");
        paymentData.put("method", paymentMethod);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(paymentData);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .POST(BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                message.setText("Pagamento de R$10,00 realizado com sucesso via " + paymentMethod + "!");
                saveToDatabase(paymentMethod, 10.00);
            } else {
                message.setText("Falha ao realizar o pagamento.");
            }
        } catch (Exception ex) {
            message.setText("Erro ao processar o pagamento: " + ex.getMessage());
        }
    }

    private void saveToDatabase(String paymentMethod, double amount) {
        String insertSQL = "INSERT INTO pagamentos (metodo, valor) VALUES (?, ?)";

        try (Connection conn = DatabaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, paymentMethod);
            pstmt.setDouble(2, amount);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
