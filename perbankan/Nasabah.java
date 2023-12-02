package perbankan;

import java.util.ArrayList;
import java.util.List;

class Nasabah extends User {
    private int saldo;
    private List<Transaksi> riwayatTransaksi;
    private Bank infoBank;

    public Nasabah(String username, String password, Bank infoBank) {
        super(username, password);
        this.saldo = 0;
        this.riwayatTransaksi = new ArrayList<>();
        this.infoBank = infoBank;
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
            Transaksi transaksi = new Transaksi("Penarikan", jumlah);
            riwayatTransaksi.add(transaksi);
            return true;
        }
        return false;
    }

public void transfer(Nasabah penerima, int jumlah) {
        if (jumlah <= saldo) {
            saldo -= jumlah;

            if (penerima.getInfoBank().getNamaBank().equals(this.infoBank.getNamaBank())) {
                // Transfer dalam satu bank
                penerima.deposit(jumlah);
                Transaksi transaksi = new Transaksi("Transfer Keluar", jumlah);
                riwayatTransaksi.add(transaksi);
                penerima.getRiwayatTransaksi().add(new Transaksi("Transfer Masuk", jumlah));
            } else {
                // Transfer antar bank
                double jumlahTransfer = jumlah + Bunga.hitungBunga(jumlah);
                if (saldo >= jumlahTransfer) {
                    saldo -= jumlahTransfer;
                    penerima.deposit(jumlah);
                    Transaksi transaksi = new Transaksi("Transfer Keluar", jumlah);
                    riwayatTransaksi.add(transaksi);
                    penerima.getRiwayatTransaksi().add(new Transaksi("Transfer Masuk", jumlah));
                    System.out.println("Transfer antar bank telah berhasil dengan penambahan bunga.");
                } else {
                    System.out.println("Saldo tidak mencukupi untuk transfer dengan tambahan bunga.");
                }
            }
        }
    }

    public Bank getInfoBank() {
        return infoBank;
    }

    public String getPassword() {
        return password;
    }
}
