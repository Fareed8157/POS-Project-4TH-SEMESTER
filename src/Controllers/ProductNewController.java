/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Fareed
 */
public class ProductNewController implements Initializable{

    @FXML
    private JFXTextField nId;

    @FXML
    private JFXTextField nName;

    @FXML
    private JFXTextField nPrice;

    @FXML
    private JFXComboBox<?> suppChose;

    @FXML
    private JFXTextField nStockIn;

    @FXML
    private JFXDatePicker stkInDate;

    @FXML
    private JFXComboBox<?> nCategory;

    @FXML
    private JFXTextField nDesc;

    @FXML
    private JFXTextField picField;

    @FXML
    private JFXButton browse;

    @FXML
    private JFXButton nSave;

    @FXML
    private JFXButton nCancel;

    @FXML
    void newItemHandler(ActionEvent event) {

    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
}
