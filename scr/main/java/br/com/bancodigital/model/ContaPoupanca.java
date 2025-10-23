package br.com.bancodigital.model;

import br.com.bancodigital.exception.SaldoInsuficienteException;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(Cliente cliente, String agencia, String numero) {
        super(cliente, agencia, numero);
    }

    @Override
    protected void sacarInterno(double valor) throws SaldoInsuficienteException {
         if (valor <= 0) {
            throw new SaldoInsuficienteException("Valor de saque deve ser positivo.");
        }
        
        if (this.saldo >= valor) {
            this.saldo -= valor;
            // System.out.println removido
        } else {
            throw new SaldoInsuficienteException("Saque não autorizado (CP). Saldo insuficiente.");
        }
    }
}