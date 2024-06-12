package lk.ijse.windsor.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Inventory;
import lk.ijse.windsor.model.tm.BooksTm;
import lk.ijse.windsor.model.tm.InventoryTm;
import lk.ijse.windsor.repository.BooksRepo;
import lk.ijse.windsor.repository.InventoryRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryFormController {

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private TextField txtBookCode;

    @FXML
    private JFXComboBox<String> cmbBookCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private Label lblDescription;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<InventoryTm> tblInventory;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtQuantity;

    public  void initialize(){
        setCellValueFactory();
        loadAllInventory();
        getCurrentInventoryId();
        getItemCode();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("B_id"));
    }
    private void loadAllInventory() {
        ObservableList<InventoryTm> obList = FXCollections.observableArrayList();

        try {
            List<Inventory> inventoryList = InventoryRepo.getAll();
            List<Books>booksList = BooksRepo.getAll();

            for (Inventory inventory: inventoryList) {
                InventoryTm tm = new InventoryTm(
                        inventory.getId(),
                        inventory.getQty(),
                        inventory.getLocation(),
                        inventory.getB_id()

                );

                obList.add(tm);
            }

            tblInventory.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentInventoryId(){
        try{
            String currentId =InventoryRepo.getCurrentId();

            String nextInventoryId = generateNextInventoryId(currentId);
            txtId.setText(nextInventoryId);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    private String generateNextInventoryId(String currentId){
        if(currentId != null) {
            String[] split = currentId.split("0");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "0" + ++idNum;
        }
        return "O1";
    }
    @FXML
    void btnPrintOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Inventory_A4.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("i_id",txtId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data , DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
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
        getCurrentInventoryId();
    }
    private void clearFields() {
        txtId.setText("");
        txtQuantity.setText("");
        txtBookCode.setText("");
        txtLocation.setText("");
        lblDescription.setText("");
        lblQtyOnHand.setText("");

    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
       String bid=txtBookCode.getText();
        if(bid == ""){
            new Alert(Alert.AlertType.WARNING,"input your id press Enter key Before delete").show();
        }else {
            // String bid=txtBookCode.setText(b_id);

            try {
                boolean isDeleted = InventoryRepo.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Inventory Details deleted!").show();
                }

                String b_id =txtBookCode.getText();
                int qty = Integer.parseInt(txtQuantity.getText());
                int bookqty = BooksRepo.getqty(b_id);
                if (bookqty != 0) {
                    boolean isqtyUpdated = BooksRepo.decrease(qty, bookqty, b_id);
                    if (isqtyUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "books quantity updated").show();
                    }
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } loadAllInventory();
            getCurrentInventoryId();
        }
        }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = txtId.getText();
        int qty = Integer.parseInt(txtQuantity.getText());
        String location = txtLocation.getText();
        String description = lblDescription.getText();

        String b_id= (String) cmbBookCode.getValue();


        Inventory Inven = new Inventory(code,qty,location,b_id);

        try {
            boolean isSaved = InventoryRepo.save(Inven);
            if(isSaved) {
                txtBookCode.setText(b_id);
                String bookid=txtBookCode.getText();
                new Alert(Alert.AlertType.CONFIRMATION, "Invetory is saved!").show();

                int bookqty=BooksRepo.getqty(bookid);
                if (bookqty!=0) {
                  boolean isqtyUpdated=BooksRepo.increaseqty(bookqty,qty, bookid);
                  if (isqtyUpdated){
                      Books books = BooksRepo.searchById(b_id);
                      if(books != null) {
                          lblQtyOnHand.setText(String.valueOf(books.getQty_on_hand()));
                      }

                      new Alert(Alert.AlertType.CONFIRMATION,"Book Quantity is updated").show();
       }
    }
}
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllInventory();
clearFields();
       getCurrentInventoryId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String code = txtId.getText();
        int qty = Integer.parseInt((txtQuantity.getText()));
        String location = txtLocation.getText();
        String b_id = txtBookCode.getText();
       // int nqty= Integer.parseInt(lblQtyOnHand.getText());

        int nqty=BooksRepo.getqty(b_id);
        int qtyold =InventoryRepo.searchId(code);

        Inventory inventory = new Inventory(code,qty,location,b_id);
        try {
            boolean isUpdated = InventoryRepo.update(inventory);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Inventory is updated!").show();
                if (qty >= qtyold) {
                    int q = qty - qtyold;
                    BooksRepo.increaseqty(nqty, q, b_id);
                } else {
                    int sq = qtyold - qty;
                    BooksRepo.decrease(sq,nqty ,b_id);
                }
     }
            } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllInventory();
    }

    private void getItemCode() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = BooksRepo.getCodes();

            for (String code : codeList) {
                obList.add(code);
            }
            cmbBookCode.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbBookCodeOnAction(ActionEvent event) {
        String code = String.valueOf(cmbBookCode.getValue());

        try {
            Books books = BooksRepo.searchById(code);
            if(books != null) {
                lblDescription.setText(books.getDescription());
                lblQtyOnHand.setText(String.valueOf(books.getQty_on_hand()));
            }

            lblQtyOnHand.requestFocus();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void txtCodeOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.windsor.Util.TextField.CODE, txtId);
    }

    @FXML
    void txtLocationOnKeyReleased(KeyEvent event) {
    Regex.setTextColor(lk.ijse.windsor.Util.TextField.DESCRIPTION,txtLocation);
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
    Regex.setTextColor(lk.ijse.windsor.Util.TextField.QTYONHAND,txtQuantity);
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {
    String id = txtId.getText();

    try{

        Inventory inventory = InventoryRepo.searchById(id);
        if(inventory !=null){
            txtId.setText(inventory.getId());
            txtQuantity.setText(String.valueOf(inventory.getQty()));
            txtLocation.setText(inventory.getLocation());
            txtBookCode.setText(inventory.getB_id());
            Books books = BooksRepo.searchById(inventory.getB_id());
            lblDescription.setText(books.getDescription());
            lblQtyOnHand.setText(String.valueOf(books.getQty_on_hand()));
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Inventory not found !").show();
        }
    }catch(SQLException e){
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
}












}
