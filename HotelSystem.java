import javax.swing.*;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class HotelSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuFrame());
    }
    // 2 การสร้าง Keycard ที่ใช้เข้าห้องและอิงตามเวลา
    public static String generateTimeBasedKeycard() {
        try {
            long currentTime = Instant.now().truncatedTo(ChronoUnit.MINUTES).getEpochSecond();
            String data = "SuperSecureKey123" + currentTime;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec("SuperSecureKey123".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);

            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(hash);
            return encoded.substring(0, 9);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}