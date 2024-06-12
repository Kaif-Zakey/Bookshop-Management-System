package lk.ijse.windsor.repository;

import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Author;
import lk.ijse.windsor.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepo {
    public static boolean save(Author author) throws SQLException {
        String sql = "INSERT INTO author VALUES(?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, author.getId());
        pstm.setObject(2, author.getName());
        pstm.setObject(3, author.getCountry());

        return pstm.executeUpdate() > 0;
    }

    public static Author searchById(String id) throws SQLException {
        String sql = "SELECT * FROM author WHERE a_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String a_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String country = resultSet.getString(3);


            Author author= new Author(a_id, name,country);

            return author;
        }

        return null;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT a_id FROM author ORDER BY a_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String authorId= resultSet.getString(1);
            return authorId;
        }
        return null;
    }

    public static boolean update(Author author) throws SQLException {
        String sql = "UPDATE author SET a_name = ?, a_country = ? WHERE a_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, author.getName());
        pstm.setObject(2, author.getCountry());
        pstm.setObject(3, author.getId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM author WHERE a_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<Author> getAll() throws SQLException {
        String sql = "SELECT * FROM author";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Author> authorList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String country = resultSet.getString(3);


            Author author = new Author(id, name,country);
            authorList.add(author);
        }
        return authorList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT a_id FROM author";
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
