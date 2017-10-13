/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class forgotPassswordController implements Initializable{

    @FXML
    private JFXTextField bfName;

    @FXML
    private JFXTextField cnic;

    @FXML
    private JFXButton click;

    @FXML
    private Label pasLabel;

     String pass="";
     @FXML
    private FontAwesomeIconView close;

    @FXML
    void closeStage(MouseEvent event) {
      Stage stage=(Stage)close.getScene().getWindow();
      stage.close();
    }
    @FXML
    void onAction(ActionEvent event) {
        if(bfName.getText().isEmpty() || cnic.getText().isEmpty())
          errorMessage("Fill The Fields Completely");
        else{
            if(nameValidation() & cnicValidation()){
               boolean flag=getPassword();
               if(flag){
                  pasLabel.setText(pass);
               }else
                   errorMessage("Enter Correct Information");
            }
        }
    }
   
    Configs con;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
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
    private boolean nameValidation() {
        Pattern p=Pattern.compile("[a-zA-Z]+");
        Matcher m=p.matcher(bfName.getText());
        if(m.find() && m.group().equals(bfName.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Friend Name");
            a1.setContentText("Please Enter Valid Friend Name");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean cnicValidation() {
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(cnic.getText());
        if(m.find() && m.group().equals(cnic.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Cnic");
            a1.setContentText("Please Enter Valid Cnic");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }

    private boolean getPassword() {
        String qu="SELECT uPass FROM user WHERE cnic="+Integer.valueOf(cnic.getText())+" AND BestFrndName='"+bfName.getText().toLowerCase()+"'";
        ResultSet rs=con.execQuery(qu);
        try {
            if(rs.next()){
               pass=rs.getString("uPass");
               return true; 
            }
               
         } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    return false;
    }
     
}
