import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private Nasabah nasabah;
    private String kritikSaran;
    private int rating;
    private LocalDateTime tanggalFeedback;
    private static final String FILE_PATH = "feedbacks.txt";

    public Feedback(Nasabah nasabah, String kritikSaran, int rating) {
        this.nasabah = nasabah;
        this.kritikSaran = kritikSaran;
        this.rating = rating;
        this.tanggalFeedback = LocalDateTime.now();
    }

    public void saveFeedbackToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            writer.write("Nasabah: " + nasabah.getUsername() + "\n");
            writer.write("Tanggal: " + tanggalFeedback.format(formatter) + "\n");
            writer.write("Rating: " + rating + "\n");
            writer.write("Kritik/Saran: " + kritikSaran + "\n");
            writer.write("-----------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}