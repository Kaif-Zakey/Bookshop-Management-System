package lk.ijse.windsor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private String o_id;
    private String c_id;
    private Date o_date;

    public Order(String id, String cId, String date) {
        this.o_id=id;
        this.c_id=cId;
        this.o_date = Date.valueOf(date);
    }
}
