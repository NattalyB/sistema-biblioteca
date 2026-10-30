-- =====================================================================
-- EXERCÍCIO 2 — Modelagem do Banco de Dados (PostgreSQL)
-- Sistema de Biblioteca
-- =====================================================================
-- Sem herança: o tipo do item (livro/revista) e o tipo do usuário
-- (aluno/professor) ficam numa coluna "tipo", restrita por CHECK.
-- =====================================================================

-- Permite rodar o script mais de uma vez (apaga na ordem inversa das dependências)
DROP TABLE IF EXISTS emprestimo;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS item;

-- ---------------------------------------------------------------------
-- 1) item  (não depende de ninguém)
-- ---------------------------------------------------------------------
CREATE TABLE item (
    id          INTEGER      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo      VARCHAR(20)  NOT NULL UNIQUE,
    titulo      VARCHAR(150) NOT NULL,
    tipo        VARCHAR(10)  NOT NULL CHECK (tipo IN ('livro', 'revista')),
    autor       VARCHAR(100),          -- usado por livros
    edicao      INTEGER,               -- usado por revistas
    disponivel  BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------------------
-- 2) usuario  (não depende de ninguém)
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id            INTEGER      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    tipo          VARCHAR(10)  NOT NULL CHECK (tipo IN ('aluno', 'professor')),
    limite_itens  INTEGER      NOT NULL CHECK (limite_itens > 0)
);

-- ---------------------------------------------------------------------
-- 3) emprestimo  (depende de item e usuario, por isso é criada por último)
-- ---------------------------------------------------------------------
CREATE TABLE emprestimo (
    id                       INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id                  INTEGER       NOT NULL REFERENCES item(id),
    usuario_id               INTEGER       NOT NULL REFERENCES usuario(id),
    data_retirada            DATE          NOT NULL,
    data_devolucao_prevista  DATE          NOT NULL,
    data_devolucao           DATE,         -- SEM NOT NULL: fica vazia enquanto o item não volta
    valor_multa              NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (valor_multa >= 0),
    CHECK (data_devolucao_prevista >= data_retirada)
);

-- =====================================================================
-- DADOS DE TESTE
-- =====================================================================

-- Itens (4 livros/revistas; R002 nunca será emprestado -> aparece na consulta 4)
INSERT INTO item (codigo, titulo, tipo, autor, edicao, disponivel) VALUES
    ('L001', 'Dom Casmurro',      'livro',   'Machado de Assis', NULL, FALSE), -- emprestado (em aberto)
    ('L002', 'O Cortiço',         'livro',   'Aluísio Azevedo',  NULL, TRUE),
    ('R001', 'Superinteressante', 'revista', NULL,               450,  TRUE),  -- já foi devolvido
    ('R002', 'Galileu',           'revista', NULL,               380,  TRUE);

-- Usuários
INSERT INTO usuario (nome, tipo, limite_itens) VALUES
    ('Ana Souza',   'aluno',     3),
    ('Carlos Lima', 'professor', 5);

-- Empréstimos
--   1) EM ABERTO: Ana pegou "Dom Casmurro" (livro, 14 dias) e ainda não devolveu
--   2) DEVOLVIDO: Carlos pegou "Superinteressante" (revista, 7 dias) e devolveu
--      3 dias atrasado -> 3 x R$ 1,00 = R$ 3,00 de multa
INSERT INTO emprestimo (item_id, usuario_id, data_retirada, data_devolucao_prevista, data_devolucao, valor_multa) VALUES
    ((SELECT id FROM item WHERE codigo = 'L001'),
     (SELECT id FROM usuario WHERE nome = 'Ana Souza'),
     '2026-10-20', '2026-11-03', NULL, 0.00),

    ((SELECT id FROM item WHERE codigo = 'R001'),
     (SELECT id FROM usuario WHERE nome = 'Carlos Lima'),
     '2026-10-01', '2026-10-08', '2026-10-11', 3.00);
