import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controla o acervo, os usuários e os empréstimos.
 * Usa arrays de tamanho fixo, como pede o enunciado.
 */
public class Biblioteca {

    private static final int MAX_ITENS = 100;
    private static final int MAX_USUARIOS = 50;
    private static final int MAX_EMPRESTIMOS = 100;

    private final ItemBiblioteca[] acervo = new ItemBiblioteca[MAX_ITENS];
    private final Usuario[] usuarios = new Usuario[MAX_USUARIOS];
    private final Emprestimo[] emprestimosAtivos = new Emprestimo[MAX_EMPRESTIMOS];

    private int totalItens = 0;
    private int totalUsuarios = 0;
    private int totalEmprestimos = 0;

    // ------------------------------------------------------------------
    // Cadastro
    // ------------------------------------------------------------------

    public boolean cadastrarItem(ItemBiblioteca item) {
        if (totalItens >= MAX_ITENS) {
            System.out.println("Acervo cheio! Não foi possível cadastrar: " + item.getTitulo());
            return false;
        }
        acervo[totalItens++] = item;
        return true;
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        if (totalUsuarios >= MAX_USUARIOS) {
            System.out.println("Limite de usuários atingido! Não foi possível cadastrar: " + usuario.getNome());
            return false;
        }
        usuarios[totalUsuarios++] = usuario;
        return true;
    }

    // ------------------------------------------------------------------
    // Empréstimo
    // ------------------------------------------------------------------

    public boolean emprestar(String codigoItem, Usuario usuario, LocalDate dataRetirada) {
        ItemBiblioteca item = buscarItem(codigoItem);

        if (item == null) {
            System.out.println("RECUSADO: item " + codigoItem + " não existe no acervo.");
            return false;
        }
        if (!item.isDisponivel()) {
            System.out.println("RECUSADO: \"" + item.getTitulo() + "\" já está emprestado.");
            return false;
        }
        if (!usuario.podeEmprestar()) {
            System.out.println("RECUSADO: " + usuario.getNome() + " (" + usuario.getTipo()
                    + ") já atingiu o limite de " + usuario.getLimiteItens() + " itens.");
            return false;
        }
        if (totalEmprestimos >= MAX_EMPRESTIMOS) {
            System.out.println("RECUSADO: limite de empréstimos simultâneos da biblioteca atingido.");
            return false;
        }

        Emprestimo emprestimo = new Emprestimo(item, usuario, dataRetirada);
        emprestimosAtivos[totalEmprestimos++] = emprestimo;

        // A disponibilidade muda como CONSEQUÊNCIA do empréstimo
        item.marcarComoEmprestado();
        usuario.registrarEmprestimo();

        System.out.println("OK: \"" + item.getTitulo() + "\" emprestado para " + usuario.getNome()
                + ". Devolver até " + emprestimo.getDataDevolucaoPrevista() + ".");
        return true;
    }

    // ------------------------------------------------------------------
    // Devolução
    // ------------------------------------------------------------------

    /**
     * Registra a devolução e retorna o valor da multa (0 se não houver atraso).
     * Retorna -1 se o item não estiver emprestado.
     */
    public double devolver(String codigoItem, LocalDate dataDevolucao) {
        int posicao = buscarPosicaoEmprestimo(codigoItem);

        if (posicao == -1) {
            System.out.println("ERRO: não há empréstimo ativo para o item " + codigoItem + ".");
            return -1;
        }

        Emprestimo emprestimo = emprestimosAtivos[posicao];
        ItemBiblioteca item = emprestimo.getItem();
        Usuario usuario = emprestimo.getUsuario();

        long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), dataDevolucao);
        double multa = item.calcularMulta(diasAtraso); // polimorfismo: cada item sabe sua multa

        // A disponibilidade muda como CONSEQUÊNCIA da devolução
        item.marcarComoDevolvido();
        usuario.registrarDevolucao();
        removerEmprestimo(posicao);

        if (multa > 0) {
            System.out.printf("DEVOLVIDO: \"%s\" por %s com %d dia(s) de atraso. Multa: R$ %.2f%n",
                    item.getTitulo(), usuario.getNome(), diasAtraso, multa);
        } else {
            System.out.printf("DEVOLVIDO: \"%s\" por %s dentro do prazo. Sem multa.%n",
                    item.getTitulo(), usuario.getNome());
        }
        return multa;
    }

    // ------------------------------------------------------------------
    // Listagem
    // ------------------------------------------------------------------

    /**
     * Percorre o acervo em um ÚNICO laço, sem nenhum if verificando o tipo.
     * Cada item sabe se descrever (toString usa getTipo/getPrazoDias/getMultaPorDia,
     * que são resolvidos pelo polimorfismo).
     */
    public void listarAcervo() {
        System.out.println("---------------------------------- ACERVO ----------------------------------");
        for (int i = 0; i < totalItens; i++) {
            System.out.println(acervo[i]);
        }
        System.out.println("----------------------------------------------------------------------------");
    }

    public void listarUsuarios() {
        System.out.println("-------- USUÁRIOS --------");
        for (int i = 0; i < totalUsuarios; i++) {
            System.out.println(usuarios[i]);
        }
        System.out.println("--------------------------");
    }

    // ------------------------------------------------------------------
    // Métodos auxiliares (privados)
    // ------------------------------------------------------------------

    private ItemBiblioteca buscarItem(String codigo) {
        for (int i = 0; i < totalItens; i++) {
            if (acervo[i].getCodigo().equals(codigo)) {
                return acervo[i];
            }
        }
        return null;
    }

    private int buscarPosicaoEmprestimo(String codigoItem) {
        for (int i = 0; i < totalEmprestimos; i++) {
            if (emprestimosAtivos[i].getItem().getCodigo().equals(codigoItem)) {
                return i;
            }
        }
        return -1;
    }

    /** Remove do array puxando os elementos seguintes uma posição para trás. */
    private void removerEmprestimo(int posicao) {
        for (int i = posicao; i < totalEmprestimos - 1; i++) {
            emprestimosAtivos[i] = emprestimosAtivos[i + 1];
        }
        emprestimosAtivos[--totalEmprestimos] = null;
    }
}
