/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
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
    private ToggleGroup saleByUserOrInvoice;
    
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
        if(event.getSource()==loadReport){
            if(!(((JFXTextField)selectedDate.getEditor()).getText().isEmpty()))
                loadReport();
            else
                errorMessage("Choose the Date");
        }else if(event.getSource()==cancel){
            Stage win=(Stage)cancel.getScene().getWindow();
            win.close();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceWise.setSelected(true);
        invoiceWise.setToggleGroup(saleByUserOrInvoice);
        userWise.setToggleGroup(saleByUserOrInvoice);
        selectedDate.setStyle("-fx-font-size:20");
    }
    
    private void loadReport() {
            LocalDate dailyDate=selectedDate.getValue();
            System.out.println("date="+dailyDate.toString());
        if(invoiceWise.isSelected()){
            String qu="";
            HashMap<String,Object> hm=new HashMap<>();
            hm.put("dateOfInvoice",dailyDate.toString());
            PrintReport pr=new PrintReport(qu,hm);
            pr.showReport("dailySale");
        }else{
            String qu="";
            HashMap<String,Object> hm=new HashMap<>();
            hm.put("dateOfSaleByUser",dailyDate.toString());
            PrintReport pr=new PrintReport(qu,hm);
            pr.showReport("dailySaleByUser");
        }
    }
    //dialog boxes
    private void errorMessage(String message){
        Alert a1= new Alert(Alert.AlertType.ERROR);
            a1.setTitle("Error");
            a1.setContentText(message);
            a1.setHeaderText(null);
            a1.showAndWait();
    }
    private void infoMessage(String message){
        Alert a1= new Alert(Alert.AlertType.INFORMATION);
            a1.setContentText(message);
            a1.setHeaderText(null);
            a1.showAndWait();
    }
    
}
