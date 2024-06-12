package lk.ijse.windsor.model.tm;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BooksTm {
    private String b_id;
    private double unit_price;
    private String Description;
    private int qty_on_hand;
}
