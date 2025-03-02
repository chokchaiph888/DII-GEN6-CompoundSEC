class Booking {
    String name;
    int roomNumber;
    int floor;
    String checkInDate;
    String checkOutDate;
    boolean isConfirmed;
    String keycard;

    public Booking(String name, int roomNumber, int floor, String checkInDate, String checkOutDate) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isConfirmed = false;
        this.keycard = "Not Generated";
    }


    @Override
    public String toString() {
        return name + " | Room: " + roomNumber + " | Floor: " + floor + " | Check-in: " + checkInDate + " | Check-out: " + checkOutDate + " | " + (isConfirmed ? "Confirmed" : "Pending") + " | Keycard: " + keycard;
    }
}