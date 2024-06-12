package lk.ijse.windsor.model;

public class Inventory {
    private String id;
    private int qty;
    private String location;
    private String b_id;

    @Override
    public String toString() {
        return "Inventory{" +
                "id='" + id + '\'' +
                ", qty=" + qty +
                ", location='" + location + '\'' +
                ", b_id='" + b_id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public Inventory(String id, int qty, String location, String b_id) {
        this.id = id;
        this.qty = qty;
        this.location = location;
        this.b_id = b_id;
    }

    public Inventory() {
    }
}
