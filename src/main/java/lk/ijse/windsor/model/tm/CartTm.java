package lk.ijse.windsor.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CartTm {
    private String code;
    private String Description;
    private int qty;
    private double unitPrice;
    private double total;
    private JFXButton btnRemove;
}
