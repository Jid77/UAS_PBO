import java.util.Scanner;

class UbahPassword {
    private Nasabah nasabah;
    private Scanner scanner;

    public UbahPassword(Nasabah nasabah, Scanner scanner) {
        this.nasabah = nasabah;
        this.scanner = scanner;
    }

    public void ubahPassword() {
        System.out.print("Masukkan password baru (minimal 8 karakter, kombinasi huruf besar, huruf kecil, dan angka): ");
        String newPassword = scanner.next();

        while (!isValidPassword(newPassword)) {
            System.out.println("Password tidak memenuhi kriteria. Pastikan password mengandung minimal 8 karakter, dengan minimal satu huruf besar, huruf kecil, dan angka.");
            System.out.print("Masukkan password baru: ");
            newPassword = scanner.next();
        }

        nasabah.setPassword(newPassword);
        System.out.println("Password berhasil diubah. Selamat! Password Anda telah diperbarui.");
    }

    private boolean isValidPassword(String password) {
        // Validasi untuk minimal 8 karakter, minimal satu huruf besar, huruf kecil, dan angka
        return password.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}");
    }
}
