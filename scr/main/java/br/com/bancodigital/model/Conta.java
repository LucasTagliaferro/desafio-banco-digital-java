package br.com.bancodigital.model;

import br.com.bancodigital.exception.SaldoInsuficienteException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class Conta {

    protected String agencia;
    protected String numero;
    protected double saldo;
    protected Cliente cliente; 

    // NOVO: Lista para guardar o histórico
    protected List<Transacao> historicoTransacoes;

    public Conta(Cliente cliente, String agencia, String numero) {
        this.cliente = cliente;
        this.agencia = agencia;
        this.numero = numero;
        this.saldo = 0.0;
        // NOVO: Inicializa a lista
        this.historicoTransacoes = new ArrayList<>(); 
    }

    // --- Métodos Internos (agora são 'protected') ---
    // A camada de Serviço irá chamá-los

    /**
     * Lógica interna de depósito. Apenas soma ao saldo.
     * (Removemos o System.out)
     */
    protected void depositarInterno(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    /**
     * Lógica interna de saque. Apenas subtrai do saldo e lança exceção.
     * (Mudou de 'sacar' para 'sacarInterno' e ficou 'protected')
     */
    protected abstract void sacarInterno(double valor) throws SaldoInsuficienteException; 

    /**
     * NOVO: Método para o serviço adicionar transações ao histórico.
     */
    public void adicionarTransacao(Transacao transacao) {
        this.historicoTransacoes.add(transacao);
    }
    
    /**
     * NOVO: Getter para o histórico (retorna uma lista não modificável).
     */
    public List<Transacao> getHistoricoTransacoes() {
        return Collections.unmodifiableList(historicoTransacoes);
    }

    /**
     * Método 'transferir' foi REMOVIDO.
     * A lógica de transferência agora é de responsabilidade
     * exclusiva do ContaService, que entende o contexto.
     */
    
    /**
     * ATUALIZADO: ExibirExtrato agora mostra o histórico.
     */
    public void exibirExtrato() {
        System.out.println("--- Extrato da Conta ---");
        System.out.println("Cliente: " + this.cliente.nome());
        System.out.println("Agência: " + this.agencia);
        System.out.println("Número: " + this.numero);
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Histórico de Transações:");

        if (historicoTransacoes.isEmpty()) {
            System.out.println("(Nenhuma transação registrada)");
        } else {
            for (Transacao t : historicoTransacoes) {
                // Usa o método toString() customizado do Record 'Transacao'
                System.out.println(t); 
            }
        }
        
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("SALDO ATUAL: R$ %.2f%n", this.saldo);
        System.out.println("--------------------------------------------------------------------------");
    }
}