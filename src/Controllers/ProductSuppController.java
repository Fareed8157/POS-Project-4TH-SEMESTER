/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Fareed
 */
public class ProductSuppController implements Initializable{

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
    private FontAwesomeIconView searcIcon;

    @FXML
    private MaterialDesignIconView closeSupplier;

    @FXML
    void closeStage(MouseEvent event) {

    }

    @FXML
    void searchIconMethod(MouseEvent event) {

    }

    @FXML
    void supplierHandler(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
}
