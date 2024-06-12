package lk.ijse.windsor.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.windsor.Util.Regex;
import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.tm.CustomerTm;
import lk.ijse.windsor.repository.CustomerRepo;
import lk.ijse.windsor.repository.OrderRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jfoenix.svg.SVGGlyphLoader.clear;


public class CustomerFormController {
    @FXML
    private TableView<CustomerTm> tblCustomer;
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private Label lblCustomerId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;


    @FXML
    private TextField txtName;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
       getCurrentCustomerId();

    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getId(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getEmail()
                );

                obList.add(tm);
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = CustomerRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } loadAllCustomers();
        clearFields();
        getCurrentCustomerId();
    }



    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();


            if (isValied()) {
                boolean isSaved = CustomerRepo.save(new Customer(id, name, address,email));
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                    clear();
            }
        }
        loadAllCustomers();
        clearFields();
        getCurrentCustomerId();
}


    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();

        Customer customer = new Customer(id, name, address, email);

        try {
            boolean isUpdated = CustomerRepo.update(customer);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } loadAllCustomers();
        clearFields();
        getCurrentCustomerId();
    }

    @FXML
    void txtCustomerIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.ID, txtId);
    }



    @FXML
    void txtAddressOnKeyReleased(KeyEvent event){
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.ADDRESS, txtAddress);
    }

    @FXML
    void txtNameOnKeyReleased(KeyEvent event){
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.NAME, txtName);
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event){
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.EMAIL, txtEmail);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.ID, txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.NAME, txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.ADDRESS, txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.windsor.Util.TextField.EMAIL, txtEmail)) return false;
        return true;
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtId.getText();

        try {
            Customer customer = CustomerRepo.searchById(id);
            if (customer != null) {
                txtId.setText(customer.getId());
                txtName.setText(customer.getName());
                txtAddress.setText(customer.getAddress());
                txtEmail.setText(customer.getEmail());
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void getCurrentCustomerId() {
        try {
            String currentId = CustomerRepo.getCurrentId();

            String nextCustomerId = generateNextCustomerId(currentId);
            txtId.setText(nextCustomerId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextCustomerId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("0");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "0" + ++idNum;
        }
        return "O1";
    }
}

