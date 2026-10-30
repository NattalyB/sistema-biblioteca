# 📚 Sistema de Biblioteca — Java (POO) + PostgreSQL

Lista de exercícios da **Formação Desenvolvedor Full Stack Júnior (/codifica — +PraTi 2026)**.

**Aluna:** Nattaly Buralde Queiroz ([@NattalyB](https://github.com/NattalyB))

---

## 📁 Estrutura

```
sistema-biblioteca/
├── exercicio1-java/
│   └── src/
│       ├── ItemBiblioteca.java   # classe abstrata (codigo, titulo, disponivel)
│       ├── Livro.java            # 14 dias / R$ 0,50 por dia
│       ├── Revista.java          # 7 dias  / R$ 1,00 por dia
│       ├── Dvd.java              # categoria nova (teste de extensibilidade)
│       ├── Usuario.java          # classe abstrata (nome, quantidadeEmprestada)
│       ├── Aluno.java            # até 3 itens
│       ├── Professor.java        # até 5 itens
│       ├── Emprestimo.java       # registro de um empréstimo
│       ├── Biblioteca.java       # emprestar(), devolver(), listarAcervo()
│       └── Main.java             # cenário de teste
├── exercicio2-modelagem/
│   └── script_criacao.sql        # CREATE TABLE + dados de teste
└── exercicio3-consultas/
    └── consultas.sql             # as 4 consultas
```

---

## ☕ Exercício 1 — Sistema de Biblioteca em Java

### Como executar

```bash
cd exercicio1-java/src
javac -encoding UTF-8 *.java
java Main
```

### Decisões de projeto

- **Prazo e multa são métodos abstratos** (`getPrazoDias()`, `getMultaPorDia()`) em `ItemBiblioteca`, implementados por `Livro` e `Revista`. Assim cada tipo define seus próprios valores.
- **Limite de itens é método abstrato** (`getLimiteItens()`) em `Usuario`, implementado por `Aluno` (3) e `Professor` (5).
- **Não existe `setDisponivel()` público.** A disponibilidade só muda dentro de `emprestar()` e `devolver()`, através de métodos *package-private* (`marcarComoEmprestado()` / `marcarComoDevolvido()`).
- **Arrays de tamanho fixo** em `Biblioteca` (`ItemBiblioteca[]`, `Usuario[]`, `Emprestimo[]`) com contadores.
- **`listarAcervo()` usa um único laço e nenhum `if` de tipo:** cada item se descreve sozinho via `toString()`, e o polimorfismo resolve tipo, prazo e multa.
- **Teste de extensibilidade:** a classe `Dvd` (3 dias, R$ 2,00/dia) foi adicionada **sem alterar nenhum método já escrito**, prova de que a decisão de tipo não vazou para fora das subclasses.

### Cenário testado no `Main`

1. Cadastro de 3 livros, 2 revistas, 1 DVD, 1 aluno e 1 professor
2. ✅ Empréstimo bem-sucedido (Ana pega "Dom Casmurro")
3. ❌ Empréstimo recusado por limite (Ana, aluna, tenta o 4º item)
4. Extras: recusa por item já emprestado, devoluções com e sem multa, e novo empréstimo após liberar espaço

Trecho da saída:

```
=== 2. EMPRÉSTIMO BEM-SUCEDIDO ===
OK: "Dom Casmurro" emprestado para Ana Souza. Devolver até 2026-10-15.

=== 3. EMPRÉSTIMO RECUSADO POR LIMITE ===
OK: "Superinteressante" emprestado para Ana Souza. Devolver até 2026-10-08.
OK: "Cidade de Deus" emprestado para Ana Souza. Devolver até 2026-10-04.
RECUSADO: Ana Souza (Aluno) já atingiu o limite de 3 itens.

=== 4. DEVOLUÇÕES ===
DEVOLVIDO: "Dom Casmurro" por Ana Souza dentro do prazo. Sem multa.
DEVOLVIDO: "Superinteressante" por Ana Souza com 3 dia(s) de atraso. Multa: R$ 3,00
DEVOLVIDO: "Cidade de Deus" por Ana Souza com 2 dia(s) de atraso. Multa: R$ 4,00
```

---

## 🐘 Exercício 2 — Modelagem do Banco (PostgreSQL)

### Como executar

```bash
psql -U postgres -d <seu_banco> -f exercicio2-modelagem/script_criacao.sql
```

(ou abra o arquivo no pgAdmin / DBeaver e execute)

### Modelo

| Tabela | Colunas | Observações |
|---|---|---|
| `item` | id, codigo, titulo, tipo, autor, edicao, disponivel | `tipo IN ('livro','revista')`, `codigo` único |
| `usuario` | id, nome, tipo, limite_itens | `tipo IN ('aluno','professor')` |
| `emprestimo` | id, item_id, usuario_id, data_retirada, data_devolucao_prevista, data_devolucao, valor_multa | FKs para `item` e `usuario`; `data_devolucao` aceita NULL |

- Chaves primárias com `GENERATED ALWAYS AS IDENTITY` (sem `SERIAL`).
- `CHECK` restringindo `tipo` em `item` e `usuario`.
- Ordem de criação: `item` → `usuario` → `emprestimo` (que depende das duas).
- O script começa com `DROP TABLE IF EXISTS` para poder ser executado mais de uma vez.

**Dados de teste:** 4 itens, 2 usuários e 2 empréstimos (um **em aberto** e um **já devolvido** com multa de R$ 3,00).

---

## 🔎 Exercício 3 — Consultas SQL

```bash
psql -U postgres -d <seu_banco> -f exercicio3-consultas/consultas.sql
```

| # | Consulta | Técnica |
|---|---|---|
| 1 | Todo o acervo com código, título, tipo e disponibilidade | `SELECT` + `CASE` |
| 2 | Empréstimos em aberto com nome do usuário e título do item | `JOIN` + `WHERE data_devolucao IS NULL` |
| 3 | Total de multas por usuário | `LEFT JOIN` + `GROUP BY` + `SUM` |
| 4 | Itens que nunca foram emprestados | `LEFT JOIN` + `WHERE e.id IS NULL` |

### Resultados com os dados de teste

**1. Acervo**
```
 codigo |      titulo       |  tipo   | disponibilidade
--------+-------------------+---------+-----------------
 L001   | Dom Casmurro      | livro   | Emprestado
 L002   | O Cortiço         | livro   | Disponível
 R001   | Superinteressante | revista | Disponível
 R002   | Galileu           | revista | Disponível
```

**2. Empréstimos em aberto**
```
 emprestimo_id |  usuario  |     item     | data_retirada | data_devolucao_prevista
---------------+-----------+--------------+---------------+-------------------------
             1 | Ana Souza | Dom Casmurro | 2026-10-20    | 2026-11-03
```

**3. Multas por usuário**
```
    nome     |   tipo    | total_multas
-------------+-----------+--------------
 Carlos Lima | professor |         3.00
 Ana Souza   | aluno     |         0.00
```

**4. Itens nunca emprestados**
```
 codigo |  titulo   |  tipo
--------+-----------+---------
 L002   | O Cortiço | livro
 R002   | Galileu   | revista
```

---

## 🛠️ Tecnologias

- Java 11+
- PostgreSQL 16
