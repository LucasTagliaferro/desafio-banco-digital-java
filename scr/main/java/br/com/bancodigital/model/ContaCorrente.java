package br.com.bancodigital.model;

import br.com.bancodigital.exception.SaldoInsuficienteException;

public class ContaCorrente extends Conta {

    private static final double LIMITE_CHEQUE_ESPECIAL = 100.0;

    public ContaCorrente(Cliente cliente, String agencia, String numero) {
        super(cliente, agencia, numero);
    }

    @Override
    protected void sacarInterno(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            throw new SaldoInsuficienteException("Valor de saque deve ser positivo.");
        }
        
        if ((this.saldo + LIMITE_CHEQUE_ESPECIAL) >= valor) {
            this.saldo -= valor;
            // System.out.println removido
        } else {
            throw new SaldoInsuficienteException("Saque não autorizado (CC). Saldo ou limite insuficiente.");
        }
    }
}