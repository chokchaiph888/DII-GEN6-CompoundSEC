// UserFrame.java - UI สำหรับการจองห้องพักของผู้ใช้
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class UserFrame extends JFrame {
    private JTextField nameField, roomField, floorField, checkInField, checkOutField;
    private JButton bookButton, backButton, logoutButton, checkRoomButton, generateKeycardButton, viewKeycardButton;
    static ArrayList<Booking> bookings = new ArrayList<>();
    static ArrayList<String> history = new ArrayList<>();

    public UserFrame() {
        if (UserManager.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first!", "Error", JOptionPane.ERROR_MESSAGE);
            new UserPanelFrame();
            dispose();
            return;
        }

        setTitle("Room Booking - Logged in as " + UserManager.currentUser.username);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margin รอบๆ ช่องกรอกข้อมูล
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(15);
        roomField = new JTextField(15);
        floorField = new JTextField(15);
        checkInField = new JTextField(15);
        checkOutField = new JTextField(15);


        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1; formPanel.add(roomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Floor:"), gbc);
        gbc.gridx = 1; formPanel.add(floorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Check-in Date:"), gbc);
        gbc.gridx = 1; formPanel.add(checkInField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Check-out Date:"), gbc);
        gbc.gridx = 1; formPanel.add(checkOutField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bookButton = new JButton("Book Room");
        checkRoomButton = new JButton("Check Room Status");
        generateKeycardButton = new JButton("Generate Keycard");
        viewKeycardButton = new JButton("View Keycard");
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");

        buttonPanel.add(bookButton);
        buttonPanel.add(checkRoomButton);
        buttonPanel.add(generateKeycardButton);
        buttonPanel.add(viewKeycardButton);
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);

        bookButton.addActionListener(e -> bookRoom());
        checkRoomButton.addActionListener(e -> checkRoomStatus());
        generateKeycardButton.addActionListener(e -> generateKeycard());
        viewKeycardButton.addActionListener(e -> viewKeycard());
        backButton.addActionListener(e -> {
            dispose();
            new MainMenuFrame();
        });
        logoutButton.addActionListener(e -> {
            UserManager.logout();
            JOptionPane.showMessageDialog(this, "You have logged out.");
            dispose();
            new UserPanelFrame();
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // กำหนด Padding ขอบหน้าต่าง

        add(mainPanel);
        setVisible(true);
    }
    public void generateKeycard() {
        if (UserManager.currentUser != null) {
            for (Booking booking : bookings) {
                if (booking.name.equals(UserManager.currentUser.username) && booking.isConfirmed) {
                    String keycard = HotelSystem.generateTimeBasedKeycard();
                    booking.keycard = keycard;
                    JOptionPane.showMessageDialog(this, "Your Digital Keycard: " + booking.keycard, "Keycard Generated", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "You must have a confirmed booking to generate a keycard!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You must log in first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookRoom() {
        String name = nameField.getText().trim();
        int roomNumber;
        int floor;
        try {
            roomNumber = Integer.parseInt(roomField.getText().trim());
            floor = Integer.parseInt(floorField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid room number and floor!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String checkIn = checkInField.getText().trim();
        String checkOut = checkOutField.getText().trim();


        for (Booking booking : bookings) {
            if (booking.roomNumber == roomNumber) {
                JOptionPane.showMessageDialog(this, "Room " + roomNumber + " is already booked!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        Booking newBooking = new Booking(name, roomNumber, floor, checkIn, checkOut);
        bookings.add(newBooking);
        history.add("User booked room: " + newBooking.toString());
        JOptionPane.showMessageDialog(this, "Room booked successfully! Waiting for admin confirmation.");
    }

    private void checkRoomStatus() {
        String roomNumberStr = roomField.getText().trim();
        String floorStr = floorField.getText().trim();

        if (roomNumberStr.isEmpty() || floorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both room number and floor to check!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int roomNumber = Integer.parseInt(roomNumberStr);
        int floor = Integer.parseInt(floorStr);

        for (Booking booking : bookings) {
            if (booking.roomNumber == roomNumber && booking.floor == floor) {
                String status = booking.isConfirmed ? "Confirmed" : "Pending";
                history.add(UserManager.currentUser.username + " checked room: " + roomNumber + " on floor " + floor + " - Status: " + status);
                JOptionPane.showMessageDialog(this, "Room " + roomNumber + " on floor " + floor + " is " + status);
                return;
            }
        }
        history.add(UserManager.currentUser.username + " checked room: " + roomNumber + " on floor " + floor + " - Not booked");
        JOptionPane.showMessageDialog(this, "Room " + roomNumber + " on floor " + floor + " is not booked yet.");
    }

    private void viewKeycard() {
        if (UserManager.currentUser != null) {
            for (Booking booking : bookings) {
                if (booking.name.equals(UserManager.currentUser.username) && booking.isConfirmed) {
                    JOptionPane.showMessageDialog(this, "Your Keycard: " + booking.keycard, "Keycard Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "You must have a confirmed booking to view your keycard!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You must log in first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}