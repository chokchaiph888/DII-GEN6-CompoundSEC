// AdminFrame.java - UI สำหรับผู้ดูแลระบบ
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class AdminFrame extends JFrame {
    private DefaultListModel<Booking> bookingListModel;
    private JList<Booking> bookingList;
    private JTextArea historyTextArea;
    private JButton backButton;
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminFrame() {
        // ขอให้ Admin ใส่รหัสผ่านก่อนเข้าใช้งาน
        String password = JOptionPane.showInputDialog(this, "Enter Admin Password:", "Admin Login", JOptionPane.INFORMATION_MESSAGE);

        if (password == null || !password.equals(ADMIN_PASSWORD)) {
            JOptionPane.showMessageDialog(this, "Access Denied! Incorrect Password.", "Error", JOptionPane.ERROR_MESSAGE);
            new MainMenuFrame(); // กลับไปหน้าเมนูหลัก
            return;
        }

        setTitle("Admin Panel - Manage Bookings");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bookingListModel = new DefaultListModel<>();
        bookingList = new JList<>(bookingListModel);

        for (Booking booking : UserFrame.bookings) {
            bookingListModel.addElement(booking);
        }

        JButton confirmButton = new JButton("Confirm Booking");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton editButton = new JButton("Edit Booking");
        JButton createCardButton = new JButton("Create Card");
        JButton showHistoryButton = new JButton("Show History");
        JButton showKeycardButton = new JButton("Show Keycard");


        confirmButton.addActionListener(e -> confirmBooking());
        cancelButton.addActionListener(e -> cancelBooking());
        editButton.addActionListener(e -> editBooking());
        createCardButton.addActionListener(e -> createCard());
        showHistoryButton.addActionListener(e -> showHistory());
        showKeycardButton.addActionListener(e -> showKeycard());

        historyTextArea = new JTextArea(10, 50);
        historyTextArea.setEditable(false);
        JPanel historyPanel = new JPanel();
        historyPanel.add(new JScrollPane(historyTextArea));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(bookingList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(editButton);
        buttonPanel.add(createCardButton);
        buttonPanel.add(showHistoryButton);
        buttonPanel.add(showKeycardButton);

        addBackButton(buttonPanel);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(historyPanel, BorderLayout.NORTH);

        add(panel);
        setVisible(true);
    }

    private void addBackButton(JPanel buttonPanel) {
        backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            dispose();
            new MainMenuFrame();
        });
        buttonPanel.add(backButton);
    }

    private void showHistory() {
        historyTextArea.setText(String.join("\n", UserFrame.history));
    }

    private void confirmBooking() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = UserFrame.bookings.get(selectedIndex);
            selectedBooking.isConfirmed = true;


            if (selectedBooking.keycard.equals("Not Generated")) {
                selectedBooking.keycard = HotelSystem.generateTimeBasedKeycard();
            }

            bookingListModel.set(selectedIndex, selectedBooking);
            UserFrame.history.add("Admin confirmed booking: " + selectedBooking.toString());
            JOptionPane.showMessageDialog(this, "Booking confirmed for " + selectedBooking.name + "\nKeycard: " + selectedBooking.keycard);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to confirm.");
        }
    }


    private void cancelBooking() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = UserFrame.bookings.get(selectedIndex);
            UserFrame.bookings.remove(selectedIndex);
            bookingListModel.remove(selectedIndex);
            UserFrame.history.add("Admin cancelled booking: " + selectedBooking.toString());
            JOptionPane.showMessageDialog(this, "Booking cancelled.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
        }
    }

    private void editBooking() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = UserFrame.bookings.get(selectedIndex);

            JTextField nameField = new JTextField(selectedBooking.name);
            JTextField roomField = new JTextField(String.valueOf(selectedBooking.roomNumber));
            JTextField floorField = new JTextField(String.valueOf(selectedBooking.floor));
            JTextField checkInField = new JTextField(selectedBooking.checkInDate);
            JTextField checkOutField = new JTextField(selectedBooking.checkOutDate);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Room Number:"));
            panel.add(roomField);
            panel.add(new JLabel("Floor:"));
            panel.add(floorField);
            panel.add(new JLabel("Check-in Date:"));
            panel.add(checkInField);
            panel.add(new JLabel("Check-out Date:"));
            panel.add(checkOutField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Booking", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                selectedBooking.name = nameField.getText();
                selectedBooking.roomNumber = Integer.parseInt(roomField.getText());
                selectedBooking.floor = Integer.parseInt(floorField.getText());
                selectedBooking.checkInDate = checkInField.getText();
                selectedBooking.checkOutDate = checkOutField.getText();
                bookingListModel.set(selectedIndex, selectedBooking);
                UserFrame.history.add("Admin edited booking: " + selectedBooking.toString());
                JOptionPane.showMessageDialog(this, "Booking updated.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to edit.");
        }
    }

    private void createCard() {
        JTextField nameField = new JTextField();
        JTextField roomField = new JTextField();
        JTextField floorField = new JTextField();
        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomField);
        panel.add(new JLabel("Floor:"));
        panel.add(floorField);
        panel.add(new JLabel("Check-in Date:"));
        panel.add(checkInField);
        panel.add(new JLabel("Check-out Date:"));
        panel.add(checkOutField);


        int result = JOptionPane.showConfirmDialog(this, panel, "Create Guest Card", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Booking newCard = new Booking(
                    nameField.getText(),
                    Integer.parseInt(roomField.getText()),
                    Integer.parseInt(floorField.getText()),
                    checkInField.getText(),
                    checkOutField.getText()
            );
            UserFrame.bookings.add(newCard);
            bookingListModel.addElement(newCard);
            UserFrame.history.add("Admin created a guest card: " + newCard.toString());
            JOptionPane.showMessageDialog(this, "Guest card created successfully.");
        }
    }

    private void showKeycard() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = UserFrame.bookings.get(selectedIndex);
            JOptionPane.showMessageDialog(this, "Keycard for " + selectedBooking.name + ":\n" + selectedBooking.keycard, "Keycard Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking first.");
        }
    }
}