/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Fareed
 */
public class ProductNewController implements Initializable{

    @FXML
    private JFXTextField barcode;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField uPrice;

    @FXML
    private JFXComboBox<String> suppChose;

    @FXML
    private JFXTextField stockIn;

    @FXML
    private JFXDatePicker stkInDate;

    @FXML
    private JFXComboBox<String> category;

    @FXML
    private JFXTextField desc;

    @FXML
    private JFXTextField picField;

    @FXML
    private JFXButton browse;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;

     @FXML
    private MaterialDesignIconView close;
     ObservableList categoryList=FXCollections.observableArrayList();
    
    private FileInputStream is=null;
    private ImageView imv=null;
    private Image im=null;
    private FileChooser chooseFile;
    private File file=null;  
    private Desktop desktop=Desktop.getDesktop();
    
    Configs con;

    @FXML
    void closeStage(MouseEvent event) {
        Stage st=(Stage)close.getScene().getWindow();
        st.close();
    }
    
    @FXML
    void onAction(ActionEvent event) throws FileNotFoundException {
        if(event.getSource()==save){
            saveData();            //laoding data into database
        }
        else if(event.getSource()==browse){
            chooseFile=new FileChooser();
            chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg","*.png","*.gif"));
            file=chooseFile.showOpenDialog((Stage)browse.getScene().getWindow());
            
            if(file!=null){
          //desktop.open(file);
            im=new Image(file.toURI().toString(),50,100,true,true);
            is=new FileInputStream(file);
            picField.setText(file.getAbsolutePath());
      }
        }
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
        loadCategory();
        category.setOnAction(e->System.out.println(category.getValue()));
        category.getItems().addAll(categoryList);
        
        
    }

    private void saveData() {
        if(!isFieldEmpty()){
            String bcode=barcode.getText();
            String pname=name.getText();
            String pr=uPrice.getText();
            String ps=suppChose.getValue().toString();
            String stkin=stockIn.getText();
            String stkDate=stkInDate.getValue().toString();
            String ctg=category.getValue().toString();
            String des=desc.getText();
            String picF=picField.getText();
            if(!checkDuplicate(bcode)){
                String qu="INSERT INTO products VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst=con.execQueryPrep(qu);
                try {
                pst.setString(1,bcode);
                pst.setString(2, pname);
                pst.setString(3, pr);
                pst.setString(4,ps);
                pst.setString(5, stkin);
                pst.setString(6, stkDate);
                pst.setString(7,ctg);
                pst.setString(8, des);
                pst.setBinaryStream(9, (InputStream)is, (int)file.length());
                } catch (SQLException ex) {
                    Logger.getLogger(ProductNewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
                errorMessage("Duplicate Values");
            
        }
        
    }

    private boolean checkDuplicate(String id) {
        String qu="SELECT * FROM products WHERE BarCode="+Integer.valueOf(id);
        try {
            ResultSet rs=con.execQuery(qu);
            if(rs.next())
                return true;
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    private void loadCategory() {
        String qu="SELECT CategoryName FROM category";
        ResultSet rs=con.execQuery(qu);
        try {
            while(rs.next()){
                categoryList.add(rs.getString("CategoryName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isFieldEmpty() {
        if(barcode.getText().isEmpty() || name.getText().isEmpty() || uPrice.getText().isEmpty() || suppChose.getValue().isEmpty()
          || stockIn.getText().isEmpty() || stkInDate.getValue().toString().isEmpty() || category.getValue().isEmpty() ||
          desc.getText().isEmpty() || picField.getText().isEmpty()) {
           return true;
        }
    return false;
    }
    
    
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
