import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<Nasabah> loadNasabahData() {
        List<Nasabah> loadedNasabahs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("nasabahs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                String username = data[0];
                String password = data[1];
                int saldo = Integer.parseInt(data[2]);
                String namaBank = data[3]; 
                String nomorRekening = data[4]; 

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