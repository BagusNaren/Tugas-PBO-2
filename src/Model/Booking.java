package Model;

public class Booking {
    private int id;
    private int customerId;
    private int roomTypeId;
    private String checkinDate;
    private String checkoutDate;
    private int price;
    private int voucherId;
    private int finalPrice;
    private String paymentStatus;
    private boolean checkedIn;
    private boolean checkedOut;

    // Constructor kosong
    public Booking() {}

    // Constructor lengkap
    public Booking(
            int id, int customerId, int roomTypeId, String checkinDate, String checkoutDate,
            int price, int voucherId, int finalPrice, String paymentStatus,
            boolean checkedIn, boolean checkedOut
    ) {
        this.id = id;
        this.customerId = customerId;
        this.roomTypeId = roomTypeId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.price = price;
        this.voucherId = voucherId;
        this.finalPrice = finalPrice;
        this.paymentStatus = paymentStatus;
        this.checkedIn = checkedIn;
        this.checkedOut = checkedOut;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public int getPrice() {
        return price;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }
}
