import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Bank {
    private String namaBank;
    private String nomorRekening;

    public Bank(String namaBank, String nomorRekening) {
        this.namaBank = namaBank;
        this.nomorRekening = nomorRekening;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }
}

class Bunga {
    private static final double TINGKAT_BUNGA = 0.05; // Anggap tingkat bunga 5%

    public static double hitungBunga(int jumlah) {
        return jumlah * TINGKAT_BUNGA;
    }
}

class Database {
    public static List<Nasabah> loadNasabahData() {
        List<Nasabah> loadedNasabahs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("nasabahs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                String username = data[0];
                String password = data[1];
                int saldo = Integer.parseInt(data[2]);
                String namaBank = data[3]; // Baca nama bank dari data

                // Jika informasi bank ada di posisi tetap, dan nomor rekening juga tersimpan dalam file
                String nomorRekening = data[4]; // Misalnya nomor rekening ada di posisi kelima

                Bank bankNasabah = new Bank(namaBank, nomorRekening);
                Nasabah nasabah = new Nasabah(username, password, bankNasabah);
                nasabah.deposit(saldo);

                loadedNasabahs.add(nasabah);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedNasabahs;
    }
}



class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Transaksi {
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

public class Full {
    private static List<Nasabah> nasabahs = new ArrayList<>();
    private static Admin admin;
    private static Scanner scanner = new Scanner(System.in); // Tambahkan objek Scanner
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

    private static void loadNasabahData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("nasabahs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                String username = data[0];
                String password = data[1];
                int saldo = Integer.parseInt(data[2]);
                String namaBank = data[3]; // Baca nama bank dari data

                // Jika informasi bank ada di posisi tetap, dan nomor rekening juga tersimpan dalam file
                String nomorRekening = data[4]; // Misalnya nomor rekening ada di posisi kelima
                Bank bankNasabah = new Bank(namaBank, nomorRekening);
                Nasabah nasabah = new Nasabah(username, password, bankNasabah);
                nasabah.deposit(saldo);

                nasabahs.add(nasabah);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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


class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void lihatInfoSemuaNasabah(List<Nasabah> nasabahs) {
        for (Nasabah nasabah : nasabahs) {
            System.out.println("Nasabah: " + nasabah.getUsername() + ", Saldo: " + nasabah.getSaldo());
            System.out.println("Bank: " + nasabah.getInfoBank().getNamaBank()); // Tampilkan nama bank
            System.out.println("Nomor Rekening: " + nasabah.getInfoBank().getNomorRekening()); // Tampilkan nomor rekening

            for (Transaksi transaksi : nasabah.getRiwayatTransaksi()) {
                System.out.println("Transaksi: " + transaksi.getJenis() + ", Jumlah: " + transaksi.getJumlah());
            }
            System.out.println();
        }
    }
}

