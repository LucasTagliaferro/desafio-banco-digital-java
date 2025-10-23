package br.com.bancodigital.model;

/**
 * Representa um cliente do banco.
 * Usamos 'record' (Java 16+) para uma classe de dados imutável.
 * Ele automaticamente fornece construtor, getters, equals(), hashCode() e toString().
 */
public record Cliente(String nome, String cpf) {
}