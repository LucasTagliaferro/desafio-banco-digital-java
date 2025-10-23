package br.com.bancodigital.repository;

import br.com.bancodigital.model.Conta;
import java.util.List;
import java.util.Optional;

/**
 * Interface (Contrato) para o repositório de Contas.
 * Define os métodos que qualquer implementação de repositório de contas deve ter.
 * Isso permite trocar a implementação (de memória para banco de dados) 
 * sem alterar o resto do sistema.
 */
public interface ContaRepository {

    /**
     * Salva ou atualiza uma conta no repositório.
     * @param conta A conta a ser salva.
     */
    void salvar(Conta conta);

    /**
     * Busca uma conta pelo número.
     * @param numeroConta O número da conta a ser buscada.
     * @return um Optional contendo a conta se encontrada, ou um Optional vazio.
     */
    Optional<Conta> buscarPorNumero(String numeroConta);

    /**
     * Busca todas as contas associadas a um CPF de cliente.
     * @param cpf O CPF do cliente.
     * @return Uma lista de contas (pode estar vazia).
     */
    List<Conta> buscarPorCpfCliente(String cpf);

    /**
     * Lista todas as contas existentes no repositório.
     * @return Uma lista com todas as contas.
     */
    List<Conta> listarTodas();
    
    /**
     * (Opcional, mas útil) Remove uma conta pelo número.
     * @param numeroConta O número da conta a ser removida.
     * @return true se a conta foi removida, false caso contrário.
     */
    boolean deletar(String numeroConta);
}