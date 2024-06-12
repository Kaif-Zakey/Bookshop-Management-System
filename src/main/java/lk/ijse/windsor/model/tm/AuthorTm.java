package lk.ijse.windsor.model.tm;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class AuthorTm {
    private String id;
    private  String name;
    private String country;
}
