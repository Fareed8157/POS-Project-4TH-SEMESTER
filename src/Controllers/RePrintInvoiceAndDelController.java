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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 *
 * @author Fareed
 */
public class RePrintInvoiceAndDelController implements Initializable{

    
    @FXML
    private JFXTextField invNo;
    
    @FXML
    private JFXComboBox<String> delOrPrint;

    @FXML
    private JFXButton delOrPrintButton;
    Configs con;
    ArrayList<Integer> qt=new ArrayList<Integer>();
    ArrayList<Integer> bcode=new ArrayList<Integer>();
    
    ObservableList<String> delOrPrintList=FXCollections.observableArrayList();
    @FXML
    void onAction(ActionEvent event) {
        if(delOrPrint.getValue()=="Print"){
            if(findInvoice())
                rePrintInvoice(invNo.getText().trim());    
            else
                errorMessage("Invoice Not Found");
        }else if(delOrPrint.getValue()=="Delete Invoice"){
             if(findInvoice())
                deletInvoice();
            else{
                 errorMessage("Invoice Not Found");
             }
        }
    }
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
        invNo.setDisable(true);
        delOrPrintButton.setDisable(true);
        delOrPrintList.addAll("Delete Invoice","Print");
        delOrPrint.getItems().addAll(delOrPrintList);
        delOrPrint.setOnAction(e->{
            delOrPrintButton.setDisable(false);
            invNo.setDisable(false);
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
        pr.showReport("invoice");
    }

    private void deletInvoice() {
        
        String qu="DELETE FROM invoice where InvoiceID="+Integer.valueOf(invNo.getText());
        String qu1="SELECT BarCode,Quantity from productinvoice WHERE InvoiceID="+Integer.valueOf(invNo.getText());
        ResultSet rs=con.execQuery(qu1);
        try {
            while(rs.next()){
              qt.add(rs.getInt("Quantity"));
              bcode.add(rs.getInt("BarCode"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        
        if(updateStkOutAvail(qt,bcode)){
            con.execAction(qu);
            infoMessage("Deleted");
        }
    }

    
    private boolean updateStkOutAvail(ArrayList<Integer> qt, ArrayList<Integer> bcode) {
        String qu="";
        int i=0;
        int avail=0;
        int stkOut=0;
        ResultSet rs=null;
        boolean flag=false;
        for(Integer c : bcode){
            System.out.println("Bcode="+c);
            qu="SELECT stockOut,availPro from products where BarCode="+c;
            rs=con.execQuery(qu);
            try {
                if(rs.next()){
                stkOut=rs.getInt("stockOut");
                avail=rs.getInt("availPro");
                }
                 //rs.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
            System.out.println("stck="+stkOut+",avaial="+avail);
            qu="UPDATE products set stockOut="+(stkOut-qt.get(i))+",availPro="+(avail+qt.get(i))+" WHERE BarCode="+c;  
            flag=con.execAction(qu);
            i++;
        }
           return flag; 
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

    private boolean findInvoice() {
        String qu="SELECT InvoiceID from invoice WHERE InvoiceID="+Integer.valueOf(invNo.getText());
        if(!(invNo.getText().isEmpty())){
            if(invoiceValidation()){
             ResultSet rs=con.execQuery(qu);
        try {
            if(rs.next()){
                return true;
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(RePrintInvoiceAndDelController.class.getName()).log(Level.SEVERE, null, ex);
        }   
            }
        }//end of if
        
        return false;
    }

    private boolean invoiceValidation() {
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(invNo.getText());
        if(m.find() && m.group().equals(invNo.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Invoice");
            a1.setContentText("Please Enter Valid Invoice");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }

    
}
