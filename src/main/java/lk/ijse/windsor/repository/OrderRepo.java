package lk.ijse.windsor.repository;

import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderRepo {
    public static Order searchById(String id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE o_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);


            Order customer = new Order(cus_id, name, address);

            return customer;
        }
        return  null;
    }
    public static boolean Delete(String id) throws SQLException {
        String sql = "DELETE FROM orders WHERE o_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT o_id FROM orders ORDER BY o_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES(?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, order.getO_id());
        pstm.setString(2, order.getC_id());
        pstm.setDate(3, order.getO_date());

        return pstm.executeUpdate() > 0;
    }
    public static List<Order> getAll() throws SQLException {
        String sql = "SELECT * FROM orders";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Order> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            // = Double.parseDouble(resultSet.getString(2));
            String  c_id= resultSet.getString(2);
            String date=resultSet.getString(3);

            Order books= new Order(id,c_id,date);
            cusList.add(books);
        }
        return cusList;
    }
}
