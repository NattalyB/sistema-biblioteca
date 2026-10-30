import java.time.LocalDate;

/**
 * Cenário de teste:
 *  1. cadastro de itens e usuários
 *  2. empréstimo bem-sucedido
 *  3. empréstimo recusado por limite atingido
 *  4. (extra) devoluções com e sem multa
 */
public class Main {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();

        // ---------------- 1. Cadastro ----------------
        System.out.println("=== 1. CADASTRO ===");
        biblioteca.cadastrarItem(new Livro("L001", "Dom Casmurro", "Machado de Assis"));
        biblioteca.cadastrarItem(new Livro("L002", "O Cortiço", "Aluísio Azevedo"));
        biblioteca.cadastrarItem(new Livro("L003", "Clean Code", "Robert C. Martin"));
        biblioteca.cadastrarItem(new Revista("R001", "Superinteressante", 450));
        biblioteca.cadastrarItem(new Revista("R002", "Galileu", 380));
        biblioteca.cadastrarItem(new Dvd("D001", "Cidade de Deus")); // categoria nova, nada alterado

        Aluno ana = new Aluno("Ana Souza");
        Professor carlos = new Professor("Carlos Lima");
        biblioteca.cadastrarUsuario(ana);
        biblioteca.cadastrarUsuario(carlos);

        biblioteca.listarAcervo();
        biblioteca.listarUsuarios();

        LocalDate hoje = LocalDate.of(2026, 10, 1);

        // ---------------- 2. Empréstimo bem-sucedido ----------------
        System.out.println("\n=== 2. EMPRÉSTIMO BEM-SUCEDIDO ===");
        biblioteca.emprestar("L001", ana, hoje);

        // ---------------- 3. Empréstimo recusado por limite ----------------
        System.out.println("\n=== 3. EMPRÉSTIMO RECUSADO POR LIMITE ===");
        biblioteca.emprestar("R001", ana, hoje); // 2º item da Ana
        biblioteca.emprestar("D001", ana, hoje); // 3º item da Ana (limite do aluno)
        biblioteca.emprestar("L002", ana, hoje); // 4º -> deve ser RECUSADO

        // Outros casos de recusa
        System.out.println("\n--- Outros casos ---");
        biblioteca.emprestar("L001", carlos, hoje); // item já emprestado
        biblioteca.emprestar("L002", carlos, hoje); // professor ainda tem espaço -> OK

        biblioteca.listarAcervo();
        biblioteca.listarUsuarios();

        // ---------------- 4. Devoluções ----------------
        System.out.println("\n=== 4. DEVOLUÇÕES ===");
        biblioteca.devolver("L001", hoje.plusDays(10)); // livro: 14 dias -> no prazo
        biblioteca.devolver("R001", hoje.plusDays(10)); // revista: 7 dias -> 3 dias de atraso = R$ 3,00
        biblioteca.devolver("D001", hoje.plusDays(5));  // DVD: 3 dias -> 2 dias de atraso = R$ 4,00

        // Agora a Ana tem espaço de novo
        System.out.println("\n--- Ana tenta de novo depois de devolver ---");
        biblioteca.emprestar("L003", ana, hoje.plusDays(10));

        biblioteca.listarAcervo();
        biblioteca.listarUsuarios();
    }
}
