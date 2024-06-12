package lk.ijse.windsor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderDetail {
    private String o_id;
    private String b_id;
    private int qty;
    private double unitPrice;
}
