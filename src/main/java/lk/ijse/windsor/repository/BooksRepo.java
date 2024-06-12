package lk.ijse.windsor.repository;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksRepo {
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM books WHERE b_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static List<Books> getAll() throws SQLException {
        String sql = "SELECT * FROM books";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Books> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            double Unitprice = Double.parseDouble(resultSet.getString(2));
            String  Description= resultSet.getString(3);
            int  qtyOnHand= Integer.parseInt(resultSet.getString(4));

            Books books= new Books(id, Unitprice, Description, qtyOnHand);
            cusList.add(books);
        }
        return cusList;
    }
    public static boolean save(Books books) throws SQLException {
        String sql = "INSERT INTO books VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, books.getB_id());
        pstm.setObject(3, books.getDescription());
        pstm.setObject(2, books.getUnit_price());
        pstm.setObject(4, books.getQty_on_hand());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getCodes() throws SQLException {
        String sql = "SELECT b_id FROM books";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static Books searchById(String code) throws SQLException {
        String sql = "SELECT * FROM books WHERE b_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Books(
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)

            );
        }
        return null;
    }
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT b_id FROM books ORDER BY b_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public static boolean update(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isUpdateQty = updateQty(od.getB_id(), od.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }
public static int getqty(String b_id) throws SQLException {
    String sql = "SELECT qty_on_hand FROM books where b_id=?";
    PreparedStatement pstm = DbConnection.getInstance().getConnection()
            .prepareStatement(sql);

    pstm.setString(1, b_id);

    ResultSet resultSet = pstm.executeQuery();
    if (resultSet.next()) {
        int qty1 = resultSet.getInt(1);
        return qty1;
    }new Alert(Alert.AlertType.ERROR,"not update to books").show();
    return 0;
    }


    public static boolean decrease(int newQty,int qty,String bId) throws SQLException {
   String sql="update books set qty_on_hand=? where b_id=?";

   PreparedStatement pstm=DbConnection.getInstance().getConnection().
           prepareStatement(sql);

        int total=qty-newQty;

        pstm.setInt(1,total);
        pstm.setString(2,bId);

        return pstm.executeUpdate()>0;
    }


        public static boolean increaseqty(int qty,int qtynew,String bid) throws SQLException {
        int toatl= qty+qtynew;

        String sql1 = "UPDATE books SET qty_on_hand= ? WHERE b_id = ?";
        PreparedStatement pstm1 = DbConnection.getInstance().getConnection()
                .prepareStatement(sql1);


        pstm1.setInt(1, toatl);
        pstm1.setString(2,bid);

       return pstm1.executeUpdate()>0;

    }
        //return qty;


    private static boolean updateQty(String itemCode, int qty) throws SQLException {
        String sql = "UPDATE books SET qty_on_hand = qty_on_hand - ? WHERE b_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, itemCode);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT b_id FROM books";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

}
