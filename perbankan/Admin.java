
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Admin extends User {
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
    public void bacaFeedback() {
        try (BufferedReader reader = new BufferedReader(new FileReader("feedbacks.txt"))) {
            String line;
            boolean isFeedback = false;
            StringBuilder feedbackInfo = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Nasabah: ")) {
                    isFeedback = true;
                    feedbackInfo.append(line).append("\n");
                } else if (line.startsWith("Tanggal: ") || line.startsWith("Rating: ") || line.startsWith("Kritik/Saran: ")) {
                    feedbackInfo.append(line).append("\n");
                } else if (line.equals("-----------------------------------")) {
                    if (isFeedback) {
                        isFeedback = false;
                        System.out.println(feedbackInfo.toString());
                        feedbackInfo.setLength(0); // Reset StringBuilder untuk feedback berikutnya
                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}