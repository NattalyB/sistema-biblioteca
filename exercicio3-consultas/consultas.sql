-- =====================================================================
-- EXERCÍCIO 3 — Consultas SQL
-- Rode antes o script do Exercício 2 (script_criacao.sql).
-- =====================================================================


-- ---------------------------------------------------------------------
-- Consulta 1: listar todo o acervo, com código, título, tipo e disponibilidade
-- ---------------------------------------------------------------------
SELECT
    codigo,
    titulo,
    tipo,
    CASE WHEN disponivel THEN 'Disponível' ELSE 'Emprestado' END AS disponibilidade
FROM item
ORDER BY codigo;


-- ---------------------------------------------------------------------
-- Consulta 2: empréstimos em aberto, com o nome do usuário e o título do item
-- Em aberto = data_devolucao IS NULL (nunca usar "= NULL")
-- ---------------------------------------------------------------------
SELECT
    e.id                      AS emprestimo_id,
    u.nome                    AS usuario,
    i.titulo                  AS item,
    e.data_retirada,
    e.data_devolucao_prevista
FROM emprestimo e
JOIN usuario u ON u.id = e.usuario_id
JOIN item    i ON i.id = e.item_id
WHERE e.data_devolucao IS NULL
ORDER BY e.data_devolucao_prevista;


-- ---------------------------------------------------------------------
-- Consulta 3: total de multas acumuladas por usuário
-- LEFT JOIN para que usuários sem empréstimo também apareçam (com 0,00)
-- ---------------------------------------------------------------------
SELECT
    u.nome,
    u.tipo,
    COALESCE(SUM(e.valor_multa), 0) AS total_multas
FROM usuario u
LEFT JOIN emprestimo e ON e.usuario_id = u.id
GROUP BY u.id, u.nome, u.tipo
ORDER BY total_multas DESC, u.nome;


-- ---------------------------------------------------------------------
-- Consulta 4: itens que nunca foram emprestados
-- LEFT JOIN traz todos os itens; quem não tem par em emprestimo
-- fica com as colunas de emprestimo NULL -> filtramos por e.id IS NULL
-- ---------------------------------------------------------------------
SELECT
    i.codigo,
    i.titulo,
    i.tipo
FROM item i
LEFT JOIN emprestimo e ON e.item_id = i.id
WHERE e.id IS NULL
ORDER BY i.codigo;
