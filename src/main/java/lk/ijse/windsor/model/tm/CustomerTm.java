package lk.ijse.windsor.model.tm;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomerTm {
    private String id;
    private String name;
    private String address;
    private String email;
}
