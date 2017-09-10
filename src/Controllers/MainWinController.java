/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class MainWinController implements Initializable{

    @FXML
    private JFXButton pos;

    @FXML
    private JFXButton category;

    @FXML
    private JFXButton product;

    @FXML
    private JFXButton dsr;

    @FXML
    private JFXButton sr;

    
    @FXML
    private JFXButton settings;

    @FXML
    private FontAwesomeIconView close;

    @FXML
    void closeStage(MouseEvent event) {
        Platform.exit();
    }
    
    
    @FXML
    void handleButton(ActionEvent event) throws IOException {
        Stage win=new Stage();
        Scene sc=null;
        if(event.getSource()==category){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/Category.fxml"));
            sc=new Scene(root);
            win.setScene(sc);
            win.show();           
        }
        else if(event.getSource()==product){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/Product.fxml"));
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==dsr){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/DailySalesReport.fxml"));
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==sr){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/StocksReport.fxml"));
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==pos){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/POS.fxml"));
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
