package br.com.bancodigital.exception;

// RuntimeException (unchecked) simplifica o código, 
// pois não precisamos declarar 'throws' em todos os métodos.
public class ContaException extends RuntimeException {
    public ContaException(String message) {
        super(message);
    }
}