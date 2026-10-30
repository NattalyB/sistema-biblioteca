/** Professor pode ter até 5 itens emprestados ao mesmo tempo. */
public class Professor extends Usuario {

    public Professor(String nome) {
        super(nome);
    }

    @Override
    public int getLimiteItens() {
        return 5;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }
}
