package perbankan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static void saveNasabahData(List<Nasabah> nasabahs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("nasabahs.txt"))) {
            for (Nasabah nasabah : nasabahs) {
                writer.write(nasabah.getUsername() + " " + nasabah.getPassword() + " " + nasabah.getSaldo());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Nasabah> loadNasabahData() {
        List<Nasabah> loadedNasabahs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("nasabahs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                String username = data[0];
                String password = data[1];
                int saldo = Integer.parseInt(data[2]);

                Nasabah nasabah = new Nasabah(username, password);
                nasabah.deposit(saldo);

                loadedNasabahs.add(nasabah);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedNasabahs;
    }
}