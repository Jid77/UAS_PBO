import perbankan.Admin;
import perbankan.Nasabah;
import perbankan.Transaksi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SistemPerbankan {
    private static List<Nasabah> nasabahs = new ArrayList<>();
    private static Admin admin;

    public static void main(String[] args) {
        Nasabah nasabah1 = new Nasabah("User1", "sandi1");
        Nasabah nasabah2 = new Nasabah("User2", "sandi2");
        nasabahs.add(nasabah1);
        nasabahs.add(nasabah2);

        admin = new Admin("admin", "adminsandi");

        login();
    }

    private static void login() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            if (admin.authenticate(username, password)) {
                menuAdmin();
            } else {
                Nasabah nasabah = getNasabahByUsername(username);
                if (nasabah != null && nasabah.authenticate(username, password)) {
                    menuNasabah(nasabah);
                } else {
                    System.out.println("Username atau password salah. Silakan coba lagi.");
                    login();
                }
            }
        }
    }

    private static void menuAdmin() {
        admin.lihatInfoSemuaNasabah(nasabahs);
    }
    private static void tambahNasabah() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan username baru: ");
            String newUsername = scanner.nextLine();
            System.out.print("Masukkan password untuk nasabah baru: ");
            String newPassword = scanner.nextLine();

            Nasabah newNasabah = new Nasabah(newUsername, newPassword);
            nasabahs.add(newNasabah);
        }
        System.out.println("Nasabah baru berhasil ditambahkan.");
    }

    private static void menuNasabah(Nasabah nasabah) {
        try (Scanner scanner = new Scanner(System.in)) {
            int pilihan;
            do {
                System.out.println("\n1. Deposit");
                System.out.println("2. Penarikan");
                System.out.println("3. Transfer");
                System.out.println("4. Lihat Riwayat Transaksi");
                System.out.println("5. Tambah Nasabah");
                System.out.println("6. Cek Saldo");

                System.out.println("0. Logout");
                System.out.print("Masukkan pilihan: ");
                pilihan = scanner.nextInt();
                switch (pilihan) {
                    case 1:
                        System.out.print("Masukkan jumlah untuk deposit: ");
                        int depositJumlah = scanner.nextInt();
                        if (depositJumlah > 0) {
                            System.out.println("Saldo Sekarang: " + nasabah.getSaldo());
                            System.out.println("Melakukan deposit sebesar " + depositJumlah + " ke akun Anda.");
                            nasabah.deposit(depositJumlah);
                        } else {
                            System.out.println("Jumlah deposit tidak valid.");
                        }
                        break;
                    case 2:
                        System.out.print("Masukkan jumlah untuk penarikan: ");
                        int penarikanJumlah = scanner.nextInt();
                        if (penarikanJumlah > 0 && penarikanJumlah <= nasabah.getSaldo()) {
                            System.out.println("Saldo Sekarang: " + nasabah.getSaldo());
                            System.out.println("Melakukan penarikan sebesar " + penarikanJumlah + " dari akun Anda.");
                            if (nasabah.withdraw(penarikanJumlah)) {
                                System.out.println("Penarikan berhasil.");
                            } else {
                                System.out.println("Penarikan gagal.");
                            }
                        } else {
                            System.out.println("Jumlah tidak valid atau saldo tidak mencukupi untuk penarikan.");
                        }
                        break;
                    case 3:
                        System.out.print("Masukkan username penerima transfer: ");
                        String recipientUsername = scanner.next();
                        Nasabah penerima = getNasabahByUsername(recipientUsername);
                        if (penerima != null) {
                            System.out.print("Masukkan jumlah yang akan ditransfer: ");
                            int transferAmount = scanner.nextInt();
                            if (transferAmount > 0 && transferAmount <= nasabah.getSaldo()) {
                                nasabah.transfer(penerima, transferAmount);
                                System.out.println("Transfer berhasil dilakukan.");
                            } else {
                                System.out.println("Jumlah tidak valid atau saldo tidak mencukupi untuk transfer.");
                            }
                        } else {
                            System.out.println("Penerima tidak ditemukan.");
                        }
                        break;

                    case 4:
                        List<Transaksi> riwayatTransaksi = nasabah.getRiwayatTransaksi();
                        if (riwayatTransaksi.isEmpty()) {
                            System.out.println("Belum ada transaksi yang dilakukan.");
                        } else {
                            System.out.println("Riwayat Transaksi:");
                            for (Transaksi transaksi : riwayatTransaksi) {
                                System.out.println("Jenis: " + transaksi.getJenis() + ", Jumlah: " + transaksi.getJumlah());
                            }
                        }
                        break;
                    case 5:
                        tambahNasabah();
                        break;
                    case 6:
                        System.out.println("Saldo Anda saat ini: " + nasabah.getSaldo());
                        break;
                    case 0:
                        System.out.println("Berhasil logout.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                        break;
                }
            } while (pilihan != 0);
        }
    }

    private static Nasabah getNasabahByUsername(String username) {
        for (Nasabah nasabah : nasabahs) {
            if (nasabah.getUsername().equals(username)) {
                return nasabah;
            }
        }
        return null;
    }
}