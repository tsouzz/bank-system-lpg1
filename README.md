# Terminal Bank System

Sistema bancário executado no terminal, desenvolvido em Java como trabalho de recuperação da disciplina de Linguagem de Programação 1 do curso de Bacharelado em Sistemas de Informação do IFSP.

## Funcionalidades

- Cadastro de clientes (Pessoa Física e Jurídica)
- Login e logout por sessão
- Abertura de conta corrente e conta poupança
- Depósito, saque e transferência entre contas
- Simulação de empréstimo pela Tabela SAC
- Histórico de operações da sessão
- Quantidade de contas do cliente
- Promoção automática de clientes ao status VIP

## Como executar

**Pré-requisitos:** Java 17 ou superior e Maven instalados.

```bash
# Clonar o repositório
git clone https://github.com/tsouzz/bank-system.git
cd bank-system

# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass="org.ifsp.bank_system.Main"
```

## Estrutura do projeto

```
src/main/java/org/ifsp/terminal_bank_system/
├── Main.java
├── controller/
│   └── MenuController.java
├── model/
│   ├── Account.java
│   ├── CurrentAccount.java
│   ├── SavingsAccount.java
│   ├── Client.java
│   ├── NaturalPersonClient.java
│   ├── LegalEntityClient.java
│   └── Address.java
├── service/
│   ├── AccountService.java
│   ├── ClientService.java
│   ├── LoanService.java
│   └── SessionManager.java
├── repository/
│   ├── AccountRepository.java
│   └── ClientRepository.java
├── interfaces/
│   └── FinancialSimulator.java
└── exception/
    ├── InsufficientFundsException.java
    └── InvalidOperationException.java
```

## Conceitos de POO aplicados

- **Herança:** `CurrentAccount` e `SavingsAccount` estendem `Account`; `NaturalPersonClient` e `LegalEntityClient` estendem `Client`
- **Polimorfismo:** `withdrawMoney()` é resolvido em tempo de execução conforme o tipo da conta
- **Encapsulamento:** atributos `private` com getters e setters em todas as classes
- **Classes abstratas:** `Account` e `Client`
- **Interface:** `FinancialSimulator` implementada por `LoanService`
- **Sobrecarga:** `simulateSAC` com 2 e 3 parâmetros
- **Sobreposição:** `withdrawMoney()` e `getDocument()` com `@Override`
- **Composição:** `Account` contém `Client`, `Client` contém `Address`
- **Tratamento de exceções:** `InsufficientFundsException` (checked) e `InvalidOperationException` com `try/catch`, `throw` e `throws`
- **Coleções genéricas:** `ArrayList<String>` para histórico de sessão
- **Matrizes:** `double[][]` para a tabela SAC no `LoanService`
- **`static` e `final`:** constantes de taxa de juros, agência padrão e contadores de conta

## Tecnologias

- Java 17
- Maven
