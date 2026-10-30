/**
 * Classe abstrata que representa qualquer item do acervo da biblioteca.
 *
 * Prazo e multa variam conforme o tipo do item, por isso são métodos
 * abstratos: cada subclasse (Livro, Revista, Dvd...) define os seus.
 */
public abstract class ItemBiblioteca {

    private final String codigo;
    private final String titulo;
    private boolean disponivel;

    public ItemBiblioteca(String codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.disponivel = true; // todo item novo entra disponível
    }

    // ---- Métodos abstratos: cada tipo de item decide os seus valores ----

    /** Quantidade de dias que o item pode ficar emprestado. */
    public abstract int getPrazoDias();

    /** Valor da multa cobrada por dia de atraso. */
    public abstract double getMultaPorDia();

    /** Nome do tipo do item (ex.: "Livro"), usado na listagem. */
    public abstract String getTipo();

    // ---- Comportamento comum a todos os itens ----

    /** Calcula a multa a partir da quantidade de dias de atraso. */
    public double calcularMulta(long diasAtraso) {
        if (diasAtraso <= 0) {
            return 0.0;
        }
        return diasAtraso * getMultaPorDia();
    }

    /*
     * Não existe setDisponivel() público.
     * A disponibilidade só muda como consequência de um empréstimo ou de uma
     * devolução, e quem faz isso é a classe Biblioteca (mesmo pacote).
     * Por isso os métodos abaixo são "package-private" (sem modificador).
     */
    void marcarComoEmprestado() {
        this.disponivel = false;
    }

    void marcarComoDevolvido() {
        this.disponivel = true;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-8s | %-35s | prazo: %2d dias | multa: R$ %.2f/dia | %s",
                codigo, getTipo(), titulo, getPrazoDias(), getMultaPorDia(),
                disponivel ? "Disponível" : "Emprestado");
    }
}
