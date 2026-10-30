/** Aluno pode ter até 3 itens emprestados ao mesmo tempo. */
public class Aluno extends Usuario {

    public Aluno(String nome) {
        super(nome);
    }

    @Override
    public int getLimiteItens() {
        return 3;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }
}
