package lk.ijse.windsor.repository;

import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    public static OrderDetail searchById(String code) throws SQLException {
        String sql = "SELECT * FROM orderbookdetail WHERE o_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new OrderDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)

            );
        }
        return null;
    }
    public static boolean Delete(String id) throws SQLException {
        String sql = "DELETE FROM orderbookdetail WHERE o_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static boolean save1(String id, String bid, int qty, double up) throws SQLException {
        String sql = "INSERT INTO orderbookdetail VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, id);
        pstm.setString(2, bid);
        pstm.setInt(3, qty);
        pstm.setDouble(4, up);

        return pstm.executeUpdate() > 0;    //false ->  |
    }

    private static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO orderbookdetail VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getO_id());
        pstm.setString(2, od.getB_id());
        pstm.setInt(3, od.getQty());
        pstm.setDouble(4, od.getUnitPrice());

        return pstm.executeUpdate() > 0;    //false ->  |
    }
        public static List<OrderDetail> getAll() throws SQLException {
            String sql1 = "SELECT * FROM orderbookdetail";

            PreparedStatement pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement(sql1);

            ResultSet resultSet = pstm.executeQuery();

            List<OrderDetail> OdList = new ArrayList<>();

            while (resultSet.next()) {
                String o_id = resultSet.getString(1);
                String b_id= resultSet.getString(2);
                int qty = Integer.parseInt(resultSet.getString(3));
                double unitprice = Double.parseDouble(resultSet.getString(4));

                OrderDetail orderDetail = new OrderDetail(o_id,b_id ,qty,unitprice);
                OdList.add(orderDetail);
            }
            return OdList;
        }


}
