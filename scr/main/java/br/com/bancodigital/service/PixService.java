package br.com.bancodigital.service;

import br.com.bancodigital.exception.ContaException;
import br.com.bancodigital.exception.SaldoInsuficienteException;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.TipoTransacao;
import br.com.bancodigital.model.Transacao;
import br.com.bancodigital.repository.ContaRepository;

import java.time.LocalDateTime;
import java.util.List;

public class PixService {

    private ContaRepository contaRepository;

    public PixService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public void realizarPix(String numeroContaOrigem, String cpfChaveDestino, double valor) {
        
        Conta contaOrigem = contaRepository.buscarPorNumero(numeroContaOrigem)
                .orElseThrow(() -> new ContaException("Conta de origem PIX não encontrada."));

        List<Conta> contasDestino = contaRepository.buscarPorCpfCliente(cpfChaveDestino);

        if (contasDestino.isEmpty()) {
            throw new ContaException("Nenhuma conta encontrada para a chave PIX (CPF) " + cpfChaveDestino);
        }
        
        Conta contaDestino = contasDestino.get(0);
        
        if (contaOrigem.getNumero().equals(contaDestino.getNumero())) {
             throw new ContaException("PIX para a própria conta não é permitido.");
        }

        System.out.println("[PIX] Iniciando PIX de " + contaOrigem.getCliente().nome() + 
                           " para " + contaDestino.getCliente().nome());
        
        try {
            // 1. Saca da origem (usando a lógica interna)
            contaOrigem.sacarInterno(valor);

            // 2. Deposita no destino (usando a lógica interna)
            contaDestino.depositarInterno(valor);

            // 3. Registra as transações de PIX
            Transacao tOrigem = new Transacao(LocalDateTime.now(), TipoTransacao.PIX_ENVIADO, valor, "PIX para: " + contaDestino.getCliente().nome());
            Transacao tDestino = new Transacao(LocalDateTime.now(), TipoTransacao.PIX_RECEBIDO, valor, "PIX de: " + contaOrigem.getCliente().nome());

            contaOrigem.adicionarTransacao(tOrigem);
            contaDestino.adicionarTransacao(tDestino);

            // 4. Salva ambas
            contaRepository.salvar(contaOrigem);
            contaRepository.salvar(contaDestino);

            System.out.println("PIX de R$" + valor + " realizado com sucesso.");

        } catch (SaldoInsuficienteException e) {
            System.out.println("Não foi possível realizar o PIX: " + e.getMessage());
        }
    }
}