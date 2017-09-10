/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import DBConnection.*;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
/**
 *
 * @author Fareed
 */
public class SignUpController implements Initializable{

    Configs dbCon;
    @FXML
    private MaterialDesignIconView closeIcon;

    @FXML
    private JFXPasswordField ePass;

    @FXML
    private JFXTextField ePhone;

    @FXML
    private JFXRadioButton male;

    @FXML
    private JFXRadioButton female;

    @FXML
    private JFXTextField eFrnd;

    @FXML
    private ImageView progress;

    @FXML
    private FontAwesomeIconView checkMark;
    
    @FXML
    private JFXButton signUp;

    @FXML
    private JFXTextField eId;

    @FXML
    private JFXTextField ename;

    @FXML
    private JFXTextField eloc;

    @FXML
    private JFXTextField eCnic;

    @FXML
    private JFXButton login;

    @FXML
    void closeWin(MouseEvent event) {
        Stage stage=(Stage)closeIcon.getScene().getWindow();
        stage.close();
    }
    PauseTransition pt=new PauseTransition();
    @FXML
    void onAction(ActionEvent event) throws IOException {
       
        pt.setDuration(Duration.seconds(3));
        Stage win=new Stage();
       Parent root=FXMLLoader.load(getClass().getResource("/FXML/LoginMain.fxml"));
       Scene scene=new Scene(root);
       if(event.getSource()==signUp){
        progress.setVisible(true);
        if(checkForDuplicateId(eId.getText().trim())){
            errorMessage("Duplicate ID!!\n Please Enter Your Own ID");
        }
        else{
        if(addUser()){
            pt.setOnFinished((ev)->{                
            login.getScene().getWindow().hide();        
            win.setScene(scene);
            win.show();                
            System.out.println("Login Successfully");
            });
             
          }
        }
        pt.play();   
       }
        else if(event.getSource()==login){
         login.getScene().getWindow().hide();        
         win.setScene(scene);
         win.show();
     }   
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkMark.setVisible(false);
       progress.setVisible(false);
       eId.textProperty().addListener((observableValue,newValue,oldValue)->{
          boolean flag= checkForDuplicateId(newValue.trim());
          if(flag){
            checkMark.setGlyphName("TIMES_CIRCLE");
            checkMark.setVisible(true);
          }
          else{
              checkMark.setGlyphName("CHECK_CIRCLE");
              checkMark.setVisible(true);
          }
        });
        eId.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    boolean flag= checkForDuplicateId(eId.getText().trim());
          if(flag){
              errorMessage("Duplicate ID!!\n Please Enter Your Own ID");
              checkMark.setGlyphName("TIMES_CIRCLE");
              checkMark.setVisible(true);
          }
          else{
              checkMark.setGlyphName("CHECK_CIRCLE");
              checkMark.setVisible(true);
          }
         } 
     });
        dbCon=Configs.getInstance();
        
    }

    
    private boolean addUser() {
        String empId=eId.getText();
        String empName=ename.getText();
        String empPass=ePass.getText();
        String eLoc=eloc.getText();
        String gender="";
        if(male.isSelected())
            gender=male.getText();
        if(female.isSelected())
            gender=female.getText();
        String eph=ePhone.getText();
        String efrnd=eFrnd.getText();
        String ecnic=eCnic.getText();
        //check for empty
        if(empId.isEmpty()||empName.isEmpty()||empPass.isEmpty()||eLoc.isEmpty()||gender.isEmpty()||eph.isEmpty()||efrnd.isEmpty()||ecnic.isEmpty())
           errorMessage("Please Enter in All Fields");
        
        //query
        String qu="INSERT INTO user VALUES ("+
                empId+","+
                "'"+empName+"',"+
                "'"+empPass+"',"+
                "'"+eLoc+"',"+
                "'"+eph+"',"+
                "'"+ecnic+"',"+
                "'"+gender+"',"+
                "'"+efrnd+"'"+
                ")";
        if(dbCon.execAction(qu)){
            infoMessage("Sccuess");
            return  true;
        }
        else{
            errorMessage("Failed");
            return false;
        }
    }

    private boolean checkForDuplicateId(String newValue) {
        ResultSet rs=dbCon.execQuery("SELECT * FROM user");
        try {
            while(rs.next()){
                String temp=Integer.toString(rs.getInt("uId"));
                if(temp.equals(newValue)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
        return false;
    }
    private void errorMessage(String message){
        Alert a1= new Alert(Alert.AlertType.ERROR);
            a1.setTitle("Error");
            a1.setContentText(message);
            a1.setHeaderText(null);
            a1.showAndWait();
            pt.stop();
    }
    private void infoMessage(String message){
        Alert a1= new Alert(Alert.AlertType.INFORMATION);
            a1.setContentText(message);
            a1.setHeaderText(null);
            a1.showAndWait();
    }
}
