/** Revista: 7 dias de prazo, multa de R$ 1,00 por dia de atraso. */
public class Revista extends ItemBiblioteca {

    private final int edicao;

    public Revista(String codigo, String titulo, int edicao) {
        super(codigo, titulo);
        this.edicao = edicao;
    }

    @Override
    public int getPrazoDias() {
        return 7;
    }

    @Override
    public double getMultaPorDia() {
        return 1.00;
    }

    @Override
    public String getTipo() {
        return "Revista";
    }

    public int getEdicao() {
        return edicao;
    }
}
