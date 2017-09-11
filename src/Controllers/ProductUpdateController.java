/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Fareed
 */
public class ProductUpdateController implements Initializable{

    @FXML
    private JFXTextField updateId;

    @FXML
    private JFXTextField updateName;

    @FXML
    private JFXTextField updatePrice;

    @FXML
    private JFXButton updateSave;

    @FXML
    private JFXButton updateCancel;

    @FXML
    private JFXTextField updateStockIn;

    @FXML
    private JFXComboBox<?> updaeCategory;

    @FXML
    private JFXTextField updateDesc;

    @FXML
    private JFXTextField picField;

    @FXML
    private JFXButton browse;

    @FXML
    private MaterialDesignIconView close;

    @FXML
    void closeStage(MouseEvent event) {

    }

    @FXML
    void onAction(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
}
