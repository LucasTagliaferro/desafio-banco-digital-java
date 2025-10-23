package br.com.bancodigital.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma única transação no histórico.
 * Usamos um Record para imutabilidade.
 */
public record Transacao(
    LocalDateTime dataHora,
    TipoTransacao tipo,
    double valor,
    String descricaoAdicional
) {

    // Formatador para exibição no extrato
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public String toString() {
        // Formata a transação para ser bonita no extrato
        return String.format("[%s] %-25s | R$ %10.2f | %s",
                dataHora.format(FORMATADOR),
                tipo.getDescricao(),
                valor,
                descricaoAdicional
        );
    }
}