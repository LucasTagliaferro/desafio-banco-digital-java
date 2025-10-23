package br.com.bancodigital.service;

import br.com.bancodigital.exception.ContaException;
import br.com.bancodigital.exception.SaldoInsuficienteException;
import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.ContaCorrente;
import br.com.bancodigital.model.ContaPoupanca;
import br.com.bancodigital.model.TipoTransacao;
import br.com.bancodigital.model.Transacao;
import br.com.bancodigital.repository.ContaRepository;

import java.time.LocalDateTime;

/**
 * Camada de Serviço para Contas (Refatorada).
 * Agora é responsável por orquestrar a lógica de saldo E o registro de transações.
 */
public class ContaService {

    private ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    private Conta buscarContaPorNumero(String numeroConta) {
        return contaRepository.buscarPorNumero(numeroConta)
                .orElseThrow(() -> new ContaException("Conta número " + numeroConta + " não encontrada."));
    }

    public Conta criarContaCorrente(Cliente cliente, String agencia, String numero) {
        if (contaRepository.buscarPorNumero(numero).isPresent()) {
            throw new ContaException("Já existe uma conta com o número " + numero);
        }
        Conta cc = new ContaCorrente(cliente, agencia, numero);
        contaRepository.salvar(cc);
        System.out.println("[Service] Conta Corrente " + numero + " criada para " + cliente.nome());
        return cc;
    }

    public Conta criarContaPoupanca(Cliente cliente, String agencia, String numero) {
        // ... (lógica idêntica ao criarContaCorrente) ...
        if (contaRepository.buscarPorNumero(numero).isPresent()) {
            throw new ContaException("Já existe uma conta com o número " + numero);
        }
        Conta cp = new ContaPoupanca(cliente, agencia, numero);
        contaRepository.salvar(cp);
        System.out.println("[Service] Conta Poupança " + numero + " criada para " + cliente.nome());
        return cp;
    }

    public void depositar(String numeroConta, double valor) {
        Conta conta = buscarContaPorNumero(numeroConta);

        // 1. Executa a lógica interna (só mexe no saldo)
        // (precisamos fazer os métodos internos da Conta serem 'protected' ou 'public')
        // *Já fizemos isso na Etapa 5.B, estão 'protected'*
        
        // (Ops, precisamos de acesso... como Service está em outro pacote,
        // vamos mudar depositarInterno e sacarInterno para 'public' em Conta.java
        // e 'public' nas filhas. Ou, melhor: mantê-los 'protected' e
        // criar métodos 'public' no service que os chamam)
        
        // **Correção Rápida**: Mude 'depositarInterno' e 'sacarInterno' para 'public'
        // em Conta, ContaCorrente e ContaPoupanca.
        
        /* * NOTA: Para este exercício, vamos expor os métodos.
         * Mude: 'protected void depositarInterno' -> 'public void depositarInterno' (em Conta.java)
         * Mude: 'protected abstract void sacarInterno' -> 'public abstract void sacarInterno' (em Conta.java)
         * Mude: 'protected void sacarInterno' -> 'public void sacarInterno' (em ContaCorrente e ContaPoupanca)
        */

        conta.depositarInterno(valor);

        // 2. Cria e registra a transação
        Transacao transacao = new Transacao(LocalDateTime.now(), TipoTransacao.DEPOSITO, valor, "Depósito em conta");
        conta.adicionarTransacao(transacao);

        // 3. Salva o estado da conta
        contaRepository.salvar(conta); 
        System.out.println("Depósito de R$" + valor + " realizado. Saldo atual: R$" + conta.getSaldo());
    }

    public void sacar(String numeroConta, double valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        
        try {
            // 1. Executa a lógica interna (mexe no saldo e pode lançar exceção)
            conta.sacarInterno(valor);

            // 2. Se o saque deu certo, cria e registra a transação
            Transacao transacao = new Transacao(LocalDateTime.now(), TipoTransacao.SAQUE, valor, "Saque em terminal/app");
            conta.adicionarTransacao(transacao);

            // 3. Salva o estado
            contaRepository.salvar(conta); 
            System.out.println("Saque de R$" + valor + " realizado. Saldo atual: R$" + conta.getSaldo());
            
        } catch (SaldoInsuficienteException e) {
            System.out.println("[Service] Falha no saque: " + e.getMessage());
            throw e; 
        }
    }

    public void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        Conta contaOrigem = buscarContaPorNumero(numeroContaOrigem);
        Conta contaDestino = buscarContaPorNumero(numeroContaDestino);
        
        if(contaOrigem.getNumero().equals(contaDestino.getNumero())){
            throw new ContaException("Transferência para a própria conta não é permitida.");
        }

        try {
            // 1. Saca da origem
            contaOrigem.sacarInterno(valor);

            // 2. Deposita no destino
            contaDestino.depositarInterno(valor);

            // 3. Registra as transações (agora com o contexto correto!)
            Transacao tOrigem = new Transacao(LocalDateTime.now(), TipoTransacao.TRANSFERENCIA_ENVIADA, valor, "Para: " + contaDestino.getCliente().nome());
            Transacao tDestino = new Transacao(LocalDateTime.now(), TipoTransacao.TRANSFERENCIA_RECEBIDA, valor, "De: " + contaOrigem.getCliente().nome());

            contaOrigem.adicionarTransacao(tOrigem);
            contaDestino.adicionarTransacao(tDestino);

            // 4. Salva o estado de AMBAS as contas
            contaRepository.salvar(contaOrigem);
            contaRepository.salvar(contaDestino);
            
            System.out.println("Transferência de R$" + valor + " realizada com sucesso.");

        } catch (SaldoInsuficienteException e) {
            System.out.println("Não foi possível realizar a transferência: " + e.getMessage());
            // Não precisamos relançar, o app.java já trata
        }
    }
    
    public void exibirExtrato(String numeroConta) {
        Conta conta = buscarContaPorNumero(numeroConta);
        // O método exibirExtrato() da Conta já foi atualizado
        conta.exibirExtrato();
    }
}