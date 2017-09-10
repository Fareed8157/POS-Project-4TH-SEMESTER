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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class ProductController implements Initializable{

    @FXML
    private TableView<?> suppTable;

    @FXML
    private TableColumn<?, ?> sFname;

    @FXML
    private TableColumn<?, ?> sLname;

    @FXML
    private TableColumn<?, ?> sEmail;

    @FXML
    private TableColumn<?, ?> sPhno;

    @FXML
    private JFXTextField sFnamField;

    @FXML
    private JFXTextField sLnamField;

    @FXML
    private JFXTextField sEmailField;

    @FXML
    private JFXTextField sPhnoFiled;

    @FXML
    private JFXButton sAdd;

    @FXML
    private JFXButton sDel;

    @FXML
    private JFXButton sUpdate;

    @FXML
    private JFXTextField suppSearch;

    @FXML
    private MaterialDesignIconView closeSupplier;

    @FXML
    private JFXButton supplier;
    
    @FXML
    private TableView<?> proTable;
     
    @FXML
    private JFXTextField nId;

    @FXML
    private JFXTextField nName;

    @FXML
    private JFXTextField nPrice;

    @FXML
    private JFXButton nSave;

    @FXML
    private JFXButton nCancel;

    @FXML
    private JFXTextField nStockIn;

    @FXML
    private JFXComboBox<?> nCategory;

    @FXML
    private JFXTextField nDesc;
    
    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> desc;

    @FXML
    private TableColumn<?, ?> unitPrice;

    @FXML
    private TableColumn<?, ?> stockIn;

    @FXML
    private JFXButton refresh;
    
    @FXML
    private JFXComboBox<?> suppChose;
    
    @FXML
    private JFXTextField spId;

    @FXML
    private JFXDatePicker upDate;
    
    @FXML
    private TableColumn<?, ?> stockOut;

    @FXML
    private TableColumn<?, ?> pCategory;

    @FXML
    private JFXButton newItem;

    @FXML
    private JFXButton updateItem;

    @FXML
    private JFXButton deleteItem;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXTextField pSearch;

    @FXML
    private MaterialDesignIconView close;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void newItemHandler(ActionEvent event) {

    }
    
    @FXML
    void supplierHandler(ActionEvent event) {

    }
    
    @FXML
    void onAction(ActionEvent event) throws IOException {
        Stage win=new Stage();
        Scene sc;
        if(event.getSource()==newItem){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/productNew.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==updateItem){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/productUpdate.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==closeButton){
            Stage stage=(Stage)closeButton.getScene().getWindow();
            stage.close();
        }
         else if(event.getSource()==supplier){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/productSupplier.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        win.setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
