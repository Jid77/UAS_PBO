package perbankan;

import java.util.ArrayList;
import java.util.List;

public class Nasabah extends User {
    private int saldo;
    private List<Transaksi> riwayatTransaksi;

    public Nasabah(String username, String password) {
        super(username, password);
        this.saldo = 0;
        this.riwayatTransaksi = new ArrayList<>();
    }

    public int getSaldo() {
        return saldo;
    }

    public List<Transaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public void deposit(int jumlah) {
        saldo += jumlah;
        Transaksi transaksi = new Transaksi("Deposit", jumlah);
        riwayatTransaksi.add(transaksi);
    }

    public boolean withdraw(int jumlah) {
        if (jumlah <= saldo) {
            saldo -= jumlah;
            Transaksi transaksi = new Transaksi("Penarikan", -jumlah);
            riwayatTransaksi.add(transaksi);
            return true;
        }
        return false;
    }

    public void transfer(Nasabah penerima, int jumlah) {
        if (withdraw(jumlah)) {
            penerima.deposit(jumlah);
            Transaksi transaksi = new Transaksi("Transfer", -jumlah);
            riwayatTransaksi.add(transaksi);
        }
    }
}