package br.com.bancodigital.repository;

import br.com.bancodigital.model.Conta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação em memória do ContaRepository.
 * Simula um banco de dados usando um Map.
 */
public class MemoriaContaRepository implements ContaRepository {

    // Nosso "banco de dados" em memória.
    // A chave (String) será o número da conta, para acesso rápido.
    // O valor (Conta) será o objeto da conta.
    private Map<String, Conta> bancoDeDados = new HashMap<>();

    @Override
    public void salvar(Conta conta) {
        // O método put() do Map já lida com inserção (se não existe) 
        // e atualização (se a chave já existe).
        bancoDeDados.put(conta.getNumero(), conta);
        System.out.println("[Repo] Conta " + conta.getNumero() + " salva/atualizada.");
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numeroConta) {
        // Optional.ofNullable() cria um Optional:
        // - Se bancoDeDados.get(numeroConta) retornar uma Conta, ele a envolve (Optional[Conta]).
        // - Se retornar null, ele cria um Optional vazio (Optional.empty).
        return Optional.ofNullable(bancoDeDados.get(numeroConta));
    }

    @Override
    public List<Conta> buscarPorCpfCliente(String cpf) {
        // Usamos a API de Streams do Java 8+
        return bancoDeDados.values() // Pega todos os valores (todas as Contas) do Map
                .stream()             // Cria um fluxo (stream) para processá-los
                .filter(conta -> conta.getCliente().cpf().equals(cpf)) // Filtra apenas as contas cujo CPF do cliente bate com o buscado
                .collect(Collectors.toList()); // Coleta os resultados em uma nova Lista
    }

    @Override
    public List<Conta> listarTodas() {
        // Retorna uma nova ArrayList contendo todos os valores (Contas) do Map
        return new ArrayList<>(bancoDeDados.values());
    }

    @Override
    public boolean deletar(String numeroConta) {
        // O método remove() do Map retorna o objeto removido se ele existia,
        // ou null se a chave não existia.
        Conta contaRemovida = bancoDeDados.remove(numeroConta);
        
        // Se contaRemovida não for null, a remoção foi bem-sucedida.
        return contaRemovida != null;
    }
}