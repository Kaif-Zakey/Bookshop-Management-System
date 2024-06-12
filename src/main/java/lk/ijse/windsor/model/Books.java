package lk.ijse.windsor.model;


import lk.ijse.windsor.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Books {
    private String b_id;
    private double unit_price;
    private String Description;
    private int qty_on_hand;


    public static boolean update(Books books) throws SQLException {
        String sql = "UPDATE books SET unit_price = ?,Description = ?, qty_on_hand = ? WHERE b_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, books.getUnit_price());
        pstm.setObject(2, books.getDescription());
        pstm.setObject(3, books.getQty_on_hand());
        pstm.setObject(4, books.getB_id());

        return pstm.executeUpdate() > 0;
    }
}
