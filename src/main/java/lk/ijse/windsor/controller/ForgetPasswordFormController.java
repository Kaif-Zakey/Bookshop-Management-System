package lk.ijse.windsor.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.windsor.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgetPasswordFormController {


    @FXML
    private JFXButton btnReset;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNewPassword;

    @FXML
    private TextField txtUserId;


    @FXML
    void btnResetOnAction(ActionEvent event) throws IOException {
        String userId =txtUserId.getText();
        String name=txtName.getText();

        try {
            checkuser(userId,name);

        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,"Your name or password invalid !").show();
        }
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    private   void  checkuser(String id,String name) throws SQLException {
        String sql = "SELECT user_id,name FROM user WHERE user_id=? AND name= ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);
        pstm.setObject(2, name);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            delete(id);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }
    }

   private void delete(String id) throws SQLException {
       String sql = "DELETE  FROM user WHERE user_id = ?";

       Connection connection = DbConnection.getInstance().getConnection();
       PreparedStatement pstm = connection.prepareStatement(sql);
       pstm.setObject(1,id);

        if (pstm.executeUpdate()>0){
         newPassword();
        }
   }
   private void newPassword() throws SQLException {
       String sql = "INSERT  INTO user VALUES(?,?,?)";
        String password =txtNewPassword.getText();
        String name=txtName.getText();
        String id=txtUserId.getText();

       Connection connection = DbConnection.getInstance().getConnection();
       PreparedStatement pstm = connection.prepareStatement(sql);
       pstm.setObject(1, id);
       pstm.setObject(2, name);
       pstm.setObject(3,password);


       if(pstm.executeUpdate()>0){
        new Alert(Alert.AlertType.CONFIRMATION,"Your Password changed Successfully! ").show();
       }
   }
}