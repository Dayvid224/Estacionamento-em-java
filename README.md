# Estacionamento-em-java
Projeto Estacionamento em Java
Descrição
Este projeto é uma aplicação de gerenciamento de estacionamento desenvolvida em Java. Ele permite que usuários registrem a entrada e saída de veículos, bem como consultem vagas e vejam o histórico de movimentações.

Funcionalidades
Registrar a entrada de veículos.

Registrar a saída de veículos.

Consultar vagas ocupadas.

Gravar dados de movimentação em um arquivo CSV.

Tela de login e registro de usuários.

Requisitos
Java Development Kit (JDK) 1.8 ou superior.

Uma IDE de sua preferência (Eclipse, IntelliJ, NetBeans, etc.).

Instruções de Instalação
Clone o repositório

bash

Copiar
git clone https://github.com/seu-usuario/estacionamento-java.git
Abra o projeto na IDE de sua preferência

Navegue até a pasta onde você clonou o projeto e abra-a na sua IDE.

Navegue até a pasta src

No explorador de arquivos da IDE, vá para a pasta src.

Encontre e execute o arquivo LoginApp.java

No diretório src, localize o arquivo LoginApp.java.

Execute LoginApp.java como uma aplicação Java.

Estrutura do Projeto

Copiar
estacionamento-java/
├── src/
│   ├── EstacionamentoApp.java
│   ├── LoginApp.java
│   └── Estacionamento.java
├── users.txt
├── placas.csv
└── historico.csv
LoginApp.java: Gerencia a interface de login e registro de usuários.

EstacionamentoApp.java: Interface principal para o gerenciamento do estacionamento.

Estacionamento.java: Contém a lógica de negócios do estacionamento.

users.txt: Armazena os dados dos usuários registrados.

placas.csv: Armazena os dados de vagas ocupadas.

historico.csv: Registra o histórico de movimentações.

Como Contribuir
Faça um fork deste repositório.

Crie um branch com sua nova feature (git checkout -b feature/nova-feature).

Commit suas mudanças (git commit -m 'Adicionei nova-feature').

Push para o branch (git push origin feature/nova-feature).

Abra um Pull Request.

Licença
Este projeto está licenciado sob a licença MIT. Consulte o arquivo LICENSE para mais informações.
