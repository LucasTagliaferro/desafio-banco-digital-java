package br.com.bancodigital.model;

import lombok.Getter;

@Getter
public enum TipoTransacao {
    
    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TRANSFERENCIA_ENVIADA("Transferência Enviada"),
    TRANSFERENCIA_RECEBIDA("Transferência Recebida"),
    PIX_ENVIADO("PIX Enviado"),
    PIX_RECEBIDO("PIX Recebido");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }
}