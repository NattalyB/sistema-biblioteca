/**
 * Classe abstrata que representa quem pode pegar itens emprestados.
 * O limite de itens simultâneos varia por tipo de usuário,
 * por isso é um método abstrato.
 */
public abstract class Usuario {

    private final String nome;
    private int quantidadeEmprestada;

    public Usuario(String nome) {
        this.nome = nome;
        this.quantidadeEmprestada = 0;
    }

    /** Máximo de itens que o usuário pode ter ao mesmo tempo. */
    public abstract int getLimiteItens();

    /** Nome do tipo do usuário (ex.: "Aluno"), usado nas mensagens. */
    public abstract String getTipo();

    public boolean podeEmprestar() {
        return quantidadeEmprestada < getLimiteItens();
    }

    // Só a Biblioteca altera a quantidade, como consequência de emprestar/devolver.
    void registrarEmprestimo() {
        quantidadeEmprestada++;
    }

    void registrarDevolucao() {
        if (quantidadeEmprestada > 0) {
            quantidadeEmprestada--;
        }
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeEmprestada() {
        return quantidadeEmprestada;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %d/%d itens", nome, getTipo(),
                quantidadeEmprestada, getLimiteItens());
    }
}
