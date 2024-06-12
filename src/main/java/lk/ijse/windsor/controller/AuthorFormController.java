package lk.ijse.windsor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.windsor.Util.Regex;
import lk.ijse.windsor.model.Author;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.tm.AuthorTm;
import lk.ijse.windsor.model.tm.CustomerTm;
import lk.ijse.windsor.repository.AuthorRepo;
import lk.ijse.windsor.repository.CustomerRepo;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AuthorFormController {

    @FXML
    private TableColumn<?, ?> colCountry;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<AuthorTm> tblAuthor;

    @FXML
    private TextField txtAtuhorName;

    @FXML
    private TextField txtAuthorCountry;

    @FXML
    private TextField txtAuthorId;

    public void initialize(){
        setCellValueFactory();
        loadAllAuthors();
        getCurrentAuthorId();

    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));

    }

    private void loadAllAuthors() {
        ObservableList<AuthorTm> obList = FXCollections.observableArrayList();

        try {
            List<Author> authorList = AuthorRepo.getAll();
            for (Author author : authorList) {
                AuthorTm tm = new AuthorTm(
                        author.getId(),
                        author.getName(),
                        author.getCountry()
                );

                obList.add(tm);
            }

            tblAuthor.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getCurrentAuthorId() {
        try {
            String currentId = AuthorRepo.getCurrentId();

            String nextAuthorId= generateNextAuthorId(currentId);
            txtAuthorId.setText(nextAuthorId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String generateNextAuthorId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("0");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "0" + ++idNum;
        }
        return "O1";
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane= FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage=(Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        getCurrentAuthorId();

    }
    private void clearFields(){
        txtAtuhorName.setText("");
        txtAuthorId.setText("");
        txtAuthorCountry.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
    String id=txtAuthorId.getText();

    try{
        boolean isDeleted = AuthorRepo.delete(id);
   if (isDeleted) {
       new Alert(Alert.AlertType.CONFIRMATION, "Author Deleted !").show();
   }else {
       new Alert(Alert.AlertType.ERROR,"Failed Delete !").show();
   }
    }catch (SQLException e){
        new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
    }loadAllAuthors();
        clearFields();
        getCurrentAuthorId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
    String id=txtAuthorId.getText();
    String name = txtAtuhorName.getText();
    String country = txtAuthorCountry.getText();

    Author author=new Author(id,name,country);

    try{
        boolean isUpdated =AuthorRepo.update(author);
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION,"Author Detail updated !").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Author Detail Unsaved !").show();
        }
    }catch (SQLException e){
        new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
    }
    loadAllAuthors();
        clearFields();
        getCurrentAuthorId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
    String id =txtAuthorId.getText();
    String name=txtAtuhorName.getText();
    String country=txtAuthorCountry.getText();

    if (isValied()){
        boolean isSaved=AuthorRepo.save(new Author(id,name,country));
    if (isSaved){
        new Alert(Alert.AlertType.CONFIRMATION,"Saved !").show();
    } else {
        new Alert(Alert.AlertType.ERROR,"Not valid Data").show();
    }
    }else {
        new Alert(Alert.AlertType.ERROR,"failed to delete !").show();
    }
loadAllAuthors();
    clearFields();
    getCurrentAuthorId();
    }


    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.ID, txtAuthorId)) return false;
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.NAME, txtAtuhorName)) return false;
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.ADDRESS, txtAuthorCountry)) return false;

        return true;
    }

    @FXML
    void txtAuthorIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.ID, txtAuthorId);
    }



    @FXML
    void txtAuthorNameOnKeyReleased(KeyEvent event){
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.NAME, txtAtuhorName);
    }

    @FXML
    void txtAuthorCountyOnKeyReleased(KeyEvent event){
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.ADDRESS, txtAuthorCountry);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtAuthorId.getText();

        try {
            Author author = AuthorRepo.searchById(id);
            if (author != null) {
                txtAuthorId.setText(author.getId());
                txtAtuhorName.setText(author.getName());
                txtAuthorCountry.setText(author.getCountry());

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Author not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}
