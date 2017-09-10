/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class DailySalesReportController implements Initializable{

    @FXML
    private JFXDatePicker selectedDate;

    @FXML
    private JFXRadioButton userWise;

    @FXML
    private JFXRadioButton invoiceWise;

    @FXML
    private JFXButton loadReport;

    @FXML
    private FontAwesomeIconView close;
    
    @FXML
    private JFXButton cancel;

    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void onAction(ActionEvent event) {
        
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
}
