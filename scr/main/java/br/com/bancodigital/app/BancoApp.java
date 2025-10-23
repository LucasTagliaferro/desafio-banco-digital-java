package br.com.bancodigital.app;

import br.com.bancodigital.exception.ContaException;
import br.com.bancodigital.exception.SaldoInsuficienteException;
import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.repository.ContaRepository;
import br.com.bancodigital.repository.MemoriaContaRepository;
import br.com.bancodigital.service.ContaService;
import br.com.bancodigital.service.PixService;

import java.util.Scanner;

public class BancoApp {

    // --- Configuração das Dependências ---
    // Instanciamos as implementações concretas aqui, na "camada" principal.
    
    // 1. O Repositório (dados)
    private static final ContaRepository contaRepository = new MemoriaContaRepository();
    
    // 2. Os Serviços (regras de negócio), que *usam* o repositório
    private static final ContaService contaService = new ContaService(contaRepository);
    private static final PixService pixService = new PixService(contaRepository);
    
    // 3. O utilitário para ler a entrada do console
    private static final Scanner scanner = new Scanner(System.in);

    
    /**
     * Ponto de entrada principal da aplicação.
     */
    public static void main(String[] args) {
        System.out.println("=== BEM-VINDO AO BANCO DIGITAL ===");
        
        // Loop principal do menu
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            
            // Usamos lerString para evitar problemas do Scanner com números
            String opcao = lerString("Escolha uma opção: ");

            switch (opcao) {
                case "1":
                    criarContaCorrente();
                    break;
                case "2":
                    criarContaPoupanca();
                    break;
                case "3":
                    depositar();
                    break;
                case "4":
                    sacar();
                    break;
                case "5":
                    transferir();
                    break;
                case "6":
                    realizarPix();
                    break;
                case "7":
                    exibirExtrato();
                    break;
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            
            if (executando) {
                lerString("\nPressione ENTER para continuar...");
            }
        }

        System.out.println("\nObrigado por usar o Banco Digital. Até logo!");
        scanner.close(); // Fecha o Scanner ao sair
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar Conta Corrente");
        System.out.println("2. Criar Conta Poupança");
        System.out.println("3. Depositar");
        System.out.println("4. Sacar");
        System.out.println("5. Transferir (entre contas do banco)");
        System.out.println("6. Realizar PIX (por CPF)");
        System.out.println("7. Exibir Extrato");
        System.out.println("0. Sair");
    }

    // --- Métodos Auxiliares para cada opção do Menu ---

    private static void criarContaCorrente() {
        System.out.println("\n--- Criar Conta Corrente ---");
        try {
            String nome = lerString("Nome do Cliente: ");
            String cpf = lerString("CPF do Cliente: ");
            String agencia = lerString("Número da Agência (ex: 0001): ");
            String numero = lerString("Número da Conta (ex: 12345-6): ");
            
            Cliente cliente = new Cliente(nome, cpf);
            contaService.criarContaCorrente(cliente, agencia, numero);
            
            System.out.println("Conta Corrente criada com sucesso!");

        } catch (ContaException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void criarContaPoupanca() {
        System.out.println("\n--- Criar Conta Poupança ---");
         try {
            String nome = lerString("Nome do Cliente: ");
            String cpf = lerString("CPF do Cliente: ");
            String agencia = lerString("Número da Agência (ex: 0001): ");
            String numero = lerString("Número da Conta (ex: 54321-0): ");
            
            Cliente cliente = new Cliente(nome, cpf);
            contaService.criarContaPoupanca(cliente, agencia, numero);
            
            System.out.println("Conta Poupança criada com sucesso!");

        } catch (ContaException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void depositar() {
        System.out.println("\n--- Realizar Depósito ---");
        try {
            String numeroConta = lerString("Número da Conta: ");
            double valor = lerDouble("Valor do Depósito: ");
            
            contaService.depositar(numeroConta, valor);
            // Mensagem de sucesso já é impressa pelo model/service

        } catch (ContaException e) {
            System.out.println("Erro no depósito: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void sacar() {
        System.out.println("\n--- Realizar Saque ---");
        try {
            String numeroConta = lerString("Número da Conta: ");
            double valor = lerDouble("Valor do Saque: ");
            
            contaService.sacar(numeroConta, valor);
            // Mensagem de sucesso já é impressa pelo model/service
            
        } catch (ContaException | SaldoInsuficienteException e) {
            System.out.println("Erro no saque: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void transferir() {
        System.out.println("\n--- Realizar Transferência ---");
        try {
            String numContaOrigem = lerString("Número da Conta de Origem: ");
            String numContaDestino = lerString("Número da Conta de Destino: ");
            double valor = lerDouble("Valor da Transferência: ");

            contaService.transferir(numContaOrigem, numContaDestino, valor);
            // Mensagem de sucesso/falha já é impressa pelo model/service

        } catch (ContaException | SaldoInsuficienteException e) {
            // A exceção de saldo é pega aqui se o 'sacar' falhar
            System.out.println("Erro na transferência: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void realizarPix() {
        System.out.println("\n--- Realizar PIX (por CPF) ---");
        try {
            String numContaOrigem = lerString("Número da Conta de Origem: ");
            String cpfDestino = lerString("CPF (Chave PIX) de Destino: ");
            double valor = lerDouble("Valor do PIX: ");

            pixService.realizarPix(numContaOrigem, cpfDestino, valor);
            // Mensagem de sucesso/falha já é impressa pelo model/service

        } catch (ContaException | SaldoInsuficienteException e) {
            System.out.println("Erro no PIX: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void exibirExtrato() {
        System.out.println("\n--- Exibir Extrato ---");
         try {
            String numeroConta = lerString("Número da Conta: ");
            contaService.exibirExtrato(numeroConta);
            
        } catch (ContaException e) {
            System.out.println("Erro ao exibir extrato: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }


    // --- Métodos Utilitários de Leitura (para evitar erros do Scanner) ---

    /**
     * Lê uma linha inteira do console.
     */
    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Lê um valor double do console, tratando erros de formato.
     */
    private static double lerDouble(String prompt) {
        while (true) {
            String input = lerString(prompt);
            try {
                // Tenta converter a string lida para double
                double valor = Double.parseDouble(input);
                if (valor <= 0) {
                     System.out.println("Valor deve ser positivo. Tente novamente.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Se der erro (ex: "abc"), avisa o usuário e continua o loop
                System.out.println("Entrada inválida. Digite um número (ex: 50.75).");
            }
        }
    }
}