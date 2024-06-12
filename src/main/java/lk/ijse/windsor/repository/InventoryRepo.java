package lk.ijse.windsor.repository;

import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Inventory;
import lk.ijse.windsor.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepo {

    public static boolean update(Inventory inventory) throws SQLException {
        String sql = "UPDATE inventory SET i_qty = ?, i_location = ?, b_id = ? WHERE i_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, inventory.getQty());
        pstm.setObject(2, inventory.getLocation());
        pstm.setObject(3, inventory.getB_id());
        pstm.setObject(4, inventory.getId());

        return pstm.executeUpdate() > 0;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE i_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static List<Inventory> getAll() throws SQLException {
        String sql = "SELECT * FROM inventory";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Inventory> invenList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            int qty = Integer.parseInt(resultSet.getString(2));
            String  location= resultSet.getString(3);
            String b_id= resultSet.getString(4);

            Inventory inventory= new Inventory(id, qty, location,b_id);
            invenList.add(inventory);
        }
        return invenList;
    }
    public static boolean save(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventory VALUES(?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1,inventory.getId());
        pstm.setObject(2,inventory.getQty());
        pstm.setObject(3,inventory.getLocation());
        pstm.setObject(4,inventory.getB_id());

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

    public static Inventory searchById(String code) throws SQLException {
        String sql = "SELECT * FROM inventory WHERE i_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Inventory(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4)

            );
        }
        return null;
    }

    public static int searchId(String code) throws SQLException {
        String sql = "SELECT * FROM inventory WHERE i_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return resultSet.getInt(2);
        }
        return 0;
    }
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT i_id FROM inventory ORDER BY i_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String invetoryId = resultSet.getString(1);
            return invetoryId;
        }
        return null;
    }

    /*public static boolean update(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isUpdateQty = updateQty(od.getB_id(), od.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }*/

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