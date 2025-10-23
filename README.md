# 🏦 Desafio Banco Digital (POO em Java)

Este projeto é uma simulação de um sistema bancário básico, desenvolvido como um desafio para consolidar conceitos fundamentais da programação orientada a objetos (POO) em Java.

## 🎯 Objetivos do Desafio

O objetivo principal foi aplicar na prática os pilares da POO e boas práticas de desenvolvimento:
- **Programação Orientada a Objetos:** Herança, Encapsulamento, Polimorfismo e Abstração.
- **Estrutura de Entidades:** Uso de Herança (`Conta` Abstrata) e Composição (`Conta` *tem um* `Cliente`).
- **Persistência em Memória:** Simulação de um banco de dados usando `Map` e o padrão Repository.
- **Boas Práticas:** Uso de `Lombok` (para reduzir boilerplate), `Enums` (para tipos de transação) e `Records` (para entidades imutáveis como `Cliente` e `Transacao`).
- **Tratamento de Exceções:** Criação de exceções customizadas (`SaldoInsuficienteException`).
- **Interação com Usuário:** Construção de um menu interativo via console (`Scanner`).

---

## ✨ Funcionalidades Implementadas

A aplicação é executada via console e permite ao usuário:
- Criar Contas (Corrente e Poupança) para Clientes.
- Realizar Saques (com regras de negócio específicas para CC e CP).
- Realizar Depósitos.
- Realizar Transferências entre contas do banco.
- Realizar transferências via PIX (simulado usando o CPF como chave).
- Exibir Extrato bancário detalhado, incluindo um histórico completo de transações (com data, hora, tipo e valor).

---

## 🏗️ Arquitetura do Projeto

O código foi estruturado em pacotes (camadas) para separação de responsabilidades:

- `br.com.bancodigital.app`:
  - `BancoApp.java`: Classe principal (`main`) responsável pelo menu e interação com o usuário (View).
- `br.com.bancodigital.model`:
  - `Cliente.java` (Record)
  - `Conta.java` (Abstrata)
  - `ContaCorrente.java` (Filha)
  - `ContaPoupanca.java` (Filha)
  - `Transacao.java` (Record)
  - `TipoTransacao.java` (Enum)
- `br.com.bancodigital.exception`:
  - `ContaException.java`
  - `SaldoInsuficienteException.java`
- `br.com.bancodigital.repository`:
  - `ContaRepository.java` (Interface)
  - `MemoriaContaRepository.java` (Implementação)
- `br.com.bancodigital.service`:
  - `ContaService.java` (Regras de negócio)
  - `PixService.java` (Regras de negócio)

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Build:** Apache Maven
- **Bibliotecas:**
  - Lombok (para geração de getters, etc.)

---

## 🚀 Como Executar

1. Clone este repositório:
   ```bash
   git clone [https://github.com/seu-usuario/desafio-banco-digital-java.git](https://github.com/seu-usuario/desafio-banco-digital-java.git)
