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
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.tm.BooksTm;
import lk.ijse.windsor.repository.BooksRepo;
import lk.ijse.windsor.repository.CustomerRepo;
import lk.ijse.windsor.repository.OrderRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BooksFormController {

    @FXML
    private TableColumn<?, ?> colDecription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;
    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<BooksTm> tblItem;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
       getCurrentBookId();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("b_id"));
        colDecription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qty_on_hand"));
    }

    private void loadAllCustomers() {
        ObservableList<BooksTm> obList = FXCollections.observableArrayList();

        try {
            List<Books> customerList = BooksRepo.getAll();
            for (Books customer : customerList) {
                BooksTm tm = new BooksTm(
                        customer.getB_id(),
                        customer.getUnit_price(),
                        customer.getDescription(),
                        customer.getQty_on_hand()
                );

                obList.add(tm);
            }

            tblItem.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtCode.getText();

        try {
            boolean isDeleted = BooksRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "BookDetail deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } loadAllCustomers();
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

    private void clearFields() {
        txtCode.setText("");
        txtDescription.setText("");
        txtQtyOnHand.setText("");
        txtUnitPrice.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String description = txtDescription.getText();
        double unitPride = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        Books books = new Books(code,  unitPride,description, qtyOnHand);

        try {
            boolean isSaved = BooksRepo.save(books);
            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomers();
        getCurrentBookId();
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        double UnitPrice = Double.parseDouble(txtUnitPrice.getText());
        String Description = txtDescription.getText();
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        Books books = new Books(code, UnitPrice, Description, qtyOnHand);

        try {
            boolean isUpdated = books.update(books);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Books updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomers();
    }

    @FXML
    void txtCodeSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtCode.getText();

        try {
            Books books = BooksRepo.searchById(id);
            if (books != null) {
                txtCode.setText(books.getB_id());
                txtDescription.setText(books.getDescription());
                txtUnitPrice.setText(String.valueOf(books.getUnit_price()));
                txtQtyOnHand.setText(String.valueOf(books.getQty_on_hand()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Book not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtCodeOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.CODE, txtCode);
    }

    @FXML
    void txtDescriptionOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.DESCRIPTION, txtDescription);
    }

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.UNITPRICE, txtUnitPrice);
    }

    @FXML
    void txtQtyOnHandOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.QTYONHAND, txtQtyOnHand);
    }







    private void getCurrentBookId() {
        try {
            String currentId = BooksRepo.getCurrentId();

            String nextBookId = generateNextBookId(currentId);
            txtCode.setText(nextBookId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextBookId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("0");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "0" + ++idNum;
        }
        return "O1";
    }
}
