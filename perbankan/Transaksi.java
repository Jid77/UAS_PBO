package perbankan;

public class Transaksi {
    private String jenis;
    private int jumlah;

    public Transaksi(String jenis, int jumlah) {
        this.jenis = jenis;
        this.jumlah = jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public int getJumlah() {
        return jumlah;
    }
}