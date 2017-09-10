/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import DBConnection.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Fareed
 */

public class LoginController implements Initializable{

    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextField empNo;

    @FXML
    private JFXPasswordField upass;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton forgotPass;

    @FXML
    private JFXCheckBox checkBox;

    @FXML
    private ImageView progress;

    
    @FXML
    private FontAwesomeIconView rightOrWrong;

    @FXML
    private JFXButton signIn;

    @FXML
    private MaterialDesignIconView closeIcon;

    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)closeIcon.getScene().getWindow();
        stage.close();
    }
    Configs dbCon=null;
    DateFormat dateFormat;
    Date date;
    public LoginController(){
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();  
    }
    @FXML
    void handleOnAction(ActionEvent event) throws IOException {
        
        final Parent root1=FXMLLoader.load(getClass().getResource("/FXML/MainWin.fxml"));
        
        
        if(event.getSource()==login){
            
            if(loginFun()){
            progress.setVisible(true);
            PauseTransition pt=new PauseTransition();
            pt.setDuration(Duration.seconds(3));
            pt.setOnFinished((ActionEvent ev)->{
            login.getScene().getWindow().hide();
            Scene sc1=new Scene(root1);
            Stage mainWin=new Stage();
            mainWin.setScene(sc1);
            mainWin.show();
            mainWin.setResizable(false)  ;
                System.out.println("Login Successfully");
            });
            pt.play();
            }
        }
        else if(event.getSource()==signIn){
            signIn.getScene().getWindow().hide();
            Stage signUpStage=new Stage();
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/SignUp.fxml"));
            Scene sc=new Scene(root);
            signUpStage.setScene(sc);
            signUpStage.show();
            signUpStage.setResizable(false)  ;            
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbCon=Configs.getInstance();
        login.setDisable(true);
        progress.setVisible(false);
        empNo.setStyle("-fx-text-inner-color: #ffffff;");
        upass.setStyle("-fx-text-inner-color: #ffffff;");
//    empNo.textProperty().addListener((observable,oldVal,newVal)->{
//        System.out.println(newVal.toString());
//    });
    rightOrWrong.setVisible(false);
    upass.setOnAction((actionEvent)->{
        System.out.println(upass.getText());
        
    });
    
    //login.setDefaultButton(true);
    login.defaultButtonProperty().bind(login.focusedProperty());
    upass.textProperty().addListener((observable,oldVal,newVal)->{
            if(loginFun()){
            rightOrWrong.setVisible(true);
            rightOrWrong.setGlyphName("CHECK_CIRCLE");
            login.setDisable(false);
            login.requestFocus();
            }
            else{
            rightOrWrong.setVisible(true);
            rightOrWrong.setGlyphName("TIMES_CIRCLE");              
            }
    });
    empNo.setOnAction(actionEvent->{
        System.out.println(actionEvent.getEventType().getName().toString());
    });
    
    }

    private boolean loginFun() {
        String id=empNo.getText();
        String pass=upass.getText();
        String qu="SELECT * FROM user WHERE uId="+id+" AND uPass="+"'"+pass+"'";
        ResultSet rs=dbCon.execQuery(qu);
        try {
            if(rs.next()){
                String dateTime=dateFormat.format(date);
                qu="insert into trackuser values('"+dateTime+"',"+id+")";
                dbCon.execAction(qu);
                return true; 
            }
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
        
        
    }
    
}
