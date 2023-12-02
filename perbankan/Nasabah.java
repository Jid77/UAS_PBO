package perbankan;

import java.util.ArrayList;
import java.util.List;

class Nasabah extends User {
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

    public void deposit(int amount) {
        saldo += amount;
        Transaksi transaksi = new Transaksi("Deposit", amount);
        riwayatTransaksi.add(transaksi);
    }

    public boolean withdraw(int amount) {
        if (amount <= saldo) {
            saldo -= amount;
            Transaksi transaksi = new Transaksi("Penarikan", amount);
            riwayatTransaksi.add(transaksi);
            return true;
        }
        return false;
    }

    public void transfer(Nasabah penerima, int amount) {
        if (amount <= saldo) {
            saldo -= amount;
            penerima.deposit(amount);
            Transaksi transaksi = new Transaksi("Transfer Keluar", amount);
            riwayatTransaksi.add(transaksi);
            penerima.getRiwayatTransaksi().add(new Transaksi("Transfer Masuk", amount));
        }
    }

    public String getPassword() {
        return password;
    }
}