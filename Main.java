import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Nasabah> nasabahs = new ArrayList<>();
    private static Admin admin;
    public static void main(String[] args) {
        nasabahs = Database.loadNasabahData();

        admin = new Admin("admin", "admin");

        if (nasabahs.isEmpty()) {
            System.out.println("Tidak ada nasabah. Silakan tambahkan nasabah melalui admin.");
            login();
        } else {
            login();
        }

        saveNasabahData();
    }

    private static void saveNasabahData() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("nasabahs.txt"))) {
        for (Nasabah nasabah : nasabahs) {
            writer.write(nasabah.getUsername() + " " + nasabah.getPassword() + " " + nasabah.getSaldo() +
                         " " + nasabah.getInfoBank().getNamaBank() + " " + nasabah.getInfoBank().getNomorRekening());
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
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
    try (Scanner scanner = new Scanner(System.in)) {
        int choice;
        boolean logout = false;
        do {
            System.out.println("\nMenu Admin:");
            System.out.println("1. Tambah Nasabah");
            System.out.println("2. Lihat Data Nasabah");
            System.out.println("3. Hapus Nasabah");
            System.out.println("0. Logout");
            System.out.print("Masukkan pilihan: ");
            
            // Memeriksa apakah input selanjutnya adalah integer sebelum membacanya
            if(scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // untuk membersihkan newline dari buffer
                switch (choice) {
                    case 1:
                        tambahNasabah();
                        break;
                    case 2:
                        admin.lihatInfoSemuaNasabah(nasabahs);
                        break;
                    case 3:
                        hapusNasabah();
                        break;
                    case 0:
                        System.out.println("Berhasil logout.");
                        logout = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                        break;
                }
            } else {
                System.out.println("Input yang dimasukkan tidak valid.");
                scanner.nextLine(); // membersihkan buffer dari input tidak valid
            }
        } while (!logout);
    }
}




private static void tambahNasabah() {
    if (admin != null) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan username baru: ");
            String newUsername = scanner.nextLine();
            System.out.print("Masukkan password untuk nasabah baru: ");
            String newPassword = scanner.nextLine();
            System.out.print("Masukkan nama bank: ");
            String namaBank = scanner.nextLine();
            System.out.print("Masukkan nomor rekening: ");
            String nomorRekening = scanner.nextLine();

            Bank bankNasabah = new Bank(namaBank, nomorRekening);
            Nasabah newNasabah = new Nasabah(newUsername, newPassword, bankNasabah);
            nasabahs.add(newNasabah);

            // Simpan ke file setelah menambahkan nasabah baru
            saveNasabahData();
        }
        System.out.println("Nasabah baru berhasil ditambahkan.");
    } else {
        System.out.println("Hanya admin yang dapat menambah nasabah baru.");
    }
}



    private static void hapusNasabah() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan username nasabah yang akan dihapus: ");
            String username = scanner.nextLine();

            Nasabah nasabah = getNasabahByUsername(username);
            if (nasabah != null) {
                nasabahs.remove(nasabah);
                System.out.println("Nasabah dengan username " + username + " telah dihapus.");
                 saveNasabahData();

            } else {
                System.out.println("Nasabah tidak ditemukan.");
            }
        }
    }

    private static void menuNasabah(Nasabah nasabah) {
        try (Scanner scanner = new Scanner(System.in)) {
            int pilihan;
            do {
                System.out.println("\nMenu Nasabah");
                System.out.println("\n1. Deposit");
                System.out.println("2. Tarik Tunai");
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
                                int transferjumlah = scanner.nextInt();
                                if (transferjumlah > 0 && transferjumlah <= nasabah.getSaldo()) {
                                    nasabah.transfer(penerima, transferjumlah);
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
                            break;                    case 0:
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