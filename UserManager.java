// UserManager.java - คลาสสำหรับจัดการผู้ใช้และบันทึกกิจกรรม
import java.util.ArrayList;

class UserManager {
    static ArrayList<User> users = new ArrayList<>();
    static User currentUser = null;
    static ArrayList<String> activityLog = new ArrayList<>(); // บันทึกกิจกรรม

    // ฟังก์ชันบันทึก Log ทุกครั้งที่เกิดกิจกรรม
    public static void logActivity(String action) {
        activityLog.add(action);
    }

    public static boolean register(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username)) {
                logActivity("Registration failed: Username already exists for " + username);
                return false; // มีชื่อผู้ใช้อยู่แล้ว
            }
        }
        users.add(new User(username, password));
        logActivity("User registered: " + username);
        return true;
    }

    public static boolean login(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user;
                logActivity("User logged in: " + username);
                return true;
            }
        }
        logActivity("Login failed: Invalid credentials for " + username);
        return false;
    }

    public static void logout() {
        logActivity("User logged out: " + (currentUser != null ? currentUser.username : "Unknown"));
        currentUser = null;
    }
}