/**
 * Teste de extensibilidade (dica 10 do enunciado):
 * esta categoria nova foi criada SEM alterar nenhum método já escrito.
 * Bastou estender ItemBiblioteca e implementar os métodos abstratos.
 *
 * DVD: 3 dias de prazo, multa de R$ 2,00 por dia de atraso.
 */
public class Dvd extends ItemBiblioteca {

    public Dvd(String codigo, String titulo) {
        super(codigo, titulo);
    }

    @Override
    public int getPrazoDias() {
        return 3;
    }

    @Override
    public double getMultaPorDia() {
        return 2.00;
    }

    @Override
    public String getTipo() {
        return "DVD";
    }
}
