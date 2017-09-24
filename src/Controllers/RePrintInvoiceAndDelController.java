/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Fareed
 */
public class RePrintInvoiceAndDelController implements Initializable{

    
    @FXML
    private JFXTextField invNo;

    @FXML
    private JFXTextField bcode;

    @FXML
    private JFXComboBox<String> delOrPrint;

    @FXML
    private JFXButton delOrPrintButton;
    Configs con;
    ObservableList<String> delOrPrintList=FXCollections.observableArrayList();
    @FXML
    void onAction(ActionEvent event) {
        if(delOrPrint.getValue()=="Print"){
           
        rePrintInvoice(invNo.getText().trim());
        }else if(delOrPrint.getValue()=="Delete"){
            
            deletInvoice();
        }else{
            deleteProduct();
        }
    }
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
        bcode.setDisable(true);
        delOrPrintList.addAll("Delete","Print","Delete Product");
        delOrPrint.getItems().addAll(delOrPrintList);
        delOrPrint.setOnAction(e->{
            if(delOrPrint.getValue()=="Delete Product")
                bcode.setDisable(false);
            else if(delOrPrint.getValue()=="Delete Product")
                bcode.setDisable(true);
            else
                bcode.setDisable(true);
            delOrPrintButton.setText(delOrPrint.getValue());
        });
    }
    private void rePrintInvoice(String invId) {
        String qu="";
        String un="";
        Integer rc=null;
        Integer ret=null;
        String qu1="SELECT rcvdAmount,returnedAmount FROM invoice WHERE InvoiceID="+Integer.valueOf(invId);
        ResultSet rs=con.execQuery(qu1);
        try {
            if(rs.next()){
              rc=rs.getInt(1);
              ret=rs.getInt(2);
                System.out.println("rcd="+rc+",ret="+ret);
            }
         qu1="SELECT u.uName FROM user u inner join invoice i on(u.uId=i.uId) where i.InvoiceID="+Integer.valueOf(invId);
         rs.close();
         rs=con.execQuery(qu1);
         if(rs.next()){
              un=rs.getString(1);
            }
         rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        HashMap<String,Object> hm=new HashMap<>();
        hm.put("invoiceId",Integer.valueOf(invId));
        hm.put("cashier",un);
        hm.put("rcvd",rc);
        hm.put("returned",ret);
        PrintReport pr=new PrintReport(qu,hm);
        pr.showReport();
    }

    private void deletInvoice() {
        
    }

    private void deleteProduct() {
        
    }
}
