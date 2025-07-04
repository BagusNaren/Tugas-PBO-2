package Model;

public class Review {
    private int bookingId;
    private int customerId;
    private int star;
    private String title;
    private String content;

    public Review() {}

    public Review(int bookingId, int star, String title, String content) {
        this.bookingId = bookingId;
        this.star = star;
        this.title = title;
        this.content = content;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
