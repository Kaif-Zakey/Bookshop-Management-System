package lk.ijse.windsor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.windsor.db.DbConnection;
import lk.ijse.windsor.model.Books;
import lk.ijse.windsor.model.Customer;
import lk.ijse.windsor.model.Order;
import lk.ijse.windsor.model.OrderDetail;
import lk.ijse.windsor.model.tm.BooksTm;
import lk.ijse.windsor.model.tm.CartTm;
import lk.ijse.windsor.model.tm.CustomerTm;
import lk.ijse.windsor.model.tm.ObTm;
import lk.ijse.windsor.repository.BooksRepo;
import lk.ijse.windsor.repository.CustomerRepo;
import lk.ijse.windsor.repository.OrderDetailRepo;
import lk.ijse.windsor.repository.OrderRepo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersFromController {

    @FXML
    private TableColumn<?, ?> colB_id;

    @FXML
    private Label lblb_id;

    @FXML
    private Label lblQty;

    @FXML
    private TableColumn<?, ?> colO_id;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colU_price;

    @FXML
    private Label lblCid;

    @FXML
    private Label lblDate;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<ObTm> tblBookorder;

    @FXML
    private TextField txtid;


    public void initialize() {
        loadAllCustomers();
        setCellValueFactory();
    }

    private void setCellValueFactory(){
        colB_id.setCellValueFactory(new PropertyValueFactory<>("b_id"));
       colO_id.setCellValueFactory(new PropertyValueFactory<>("o_id"));
        colU_price.setCellValueFactory(new PropertyValueFactory<>("unitprice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qtyonhand"));
      }



    private void loadAllCustomers() {
        ObservableList<ObTm> obList = FXCollections.observableArrayList();

        try {
            List<OrderDetail> orderDetailList= OrderDetailRepo.getAll();
                for (OrderDetail orderDetail: orderDetailList) {
                    ObTm tm = new ObTm(
                            orderDetail.getO_id(),
                            orderDetail.getB_id(),
                            orderDetail.getQty(),
                           orderDetail.getUnitPrice()
                    );
                    obList.add(tm);
                }

            tblBookorder.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashboard_form.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
    String bId=lblb_id.getText();
    String id= txtid.getText();


    int oldqty= BooksRepo.getqty(bId);
    int qty= Integer.parseInt(lblQty.getText());

    OrderDetailRepo.Delete(id);
    if (BooksRepo.increaseqty(oldqty,qty,bId)){
    if (OrderRepo.Delete(id)) {
        new Alert(Alert.AlertType.CONFIRMATION, "Deleted !").show();
    }
    }else {
        new Alert(Alert.AlertType.ERROR, "Try again").show();
    }

}

    @FXML
    void btnSearchOnAction(ActionEvent event) {
    String id=txtid.getText();

        try {OrderDetail odb=OrderDetailRepo.searchById(id);
            Order order = OrderRepo.searchById(id);
            if (order != null) {
                lblCid.setText(order.getC_id());
                lblDate.setText(String.valueOf(order.getO_date()));
            }
            if (odb!=null){
                lblb_id.setText(odb.getB_id());
                lblQty.setText(String.valueOf(odb.getQty()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}