package perbankan;

import java.util.List;

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void lihatInfoSemuaNasabah(List<Nasabah> nasabahs) {
        for (Nasabah nasabah : nasabahs) {
            System.out.println("Nasabah: " + nasabah.getUsername() + ", Saldo: " + nasabah.getSaldo());
            for (Transaksi transaksi : nasabah.getRiwayatTransaksi()) {
                System.out.println("Transaksi: " + transaksi.getJenis() + ", Jumlah: " + transaksi.getJumlah());
            }
            System.out.println("");
        }
    }
}
