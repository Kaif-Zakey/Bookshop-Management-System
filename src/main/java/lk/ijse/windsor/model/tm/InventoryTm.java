package lk.ijse.windsor.model.tm;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Setter
public class InventoryTm {
    private String id;
    private int qty;
    private String location;
    private String b_id;

}
