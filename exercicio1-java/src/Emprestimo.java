import java.time.LocalDate;

/**
 * Registro de um empréstimo: qual item, para quem e quando.
 * A data prevista de devolução vem do prazo do próprio item (polimorfismo).
 */
public class Emprestimo {

    private final ItemBiblioteca item;
    private final Usuario usuario;
    private final LocalDate dataRetirada;
    private final LocalDate dataDevolucaoPrevista;

    public Emprestimo(ItemBiblioteca item, Usuario usuario, LocalDate dataRetirada) {
        this.item = item;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataDevolucaoPrevista = dataRetirada.plusDays(item.getPrazoDias());
    }

    public ItemBiblioteca getItem() {
        return item;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }
}
