package lk.ijse.windsor.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ObTm {
    private String o_id;
    private String B_id;
    private int qtyonhand;
    private double unitprice;


}
