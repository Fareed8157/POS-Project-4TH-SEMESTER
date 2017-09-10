/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import MainPack.Category;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mysql.cj.x.protobuf.Mysqlx.Error.Severity;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javax.xml.bind.Validator;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author Fareed
 */
public class CategoryNewController implements Initializable{
    
   @FXML
    private JFXTextField nid;

    @FXML
    private JFXTextField nName;

    @FXML
    private JFXTextField nDesc;

    @FXML
    private JFXTextField nImage;

    @FXML
    private JFXButton browse;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    ObservableList<Category> list=FXCollections.observableArrayList();
   
    ResultSet rs;
    private FileInputStream is=null;
    private ImageView imv=null;
    private Image im=null;
    private FileChooser chooseFile;
    private File file=null;  
    private Desktop desktop=Desktop.getDesktop();
    Configs dbCon=null;
    @FXML
    void fileHandler(ActionEvent event) {
        file=chooseFile.showOpenDialog((Stage)browse.getScene().getWindow());
      if(file!=null){
          //desktop.open(file);
          im=new Image(file.toURI().toString(),50,100,true,true);
          nImage.setText(file.getAbsolutePath());
      }
    }

     @FXML
    void onAction(ActionEvent event) throws IOException {
        
        if(event.getSource()==save){
            if(addNewCategory()){
            clearFields();
            infoMessage("Successful");
            }
        }
        else if(event.getSource()==cancel)
            cancel.getScene().getWindow().hide();
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        RequiredFieldValidator validator=new RequiredFieldValidator();
        validator.setMessage("No input Given");     
        nid.getValidators().add(validator);
        nid.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) 
                    nid.validate();
        });
        nName.getValidators().add(validator);
        nName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) 
                    nName.validate();
        });
        nDesc.getValidators().add(validator);
        nDesc.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) 
                    nDesc.validate();
        });
        nImage.getValidators().add(validator);
        nImage.focusedProperty().addListener((observable, oldValue, newValue) -> {
    if(!newValue) { // we only care about loosing focus
       nImage.validate();
    }
});
//        nImage.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) 
//                    nImage.validate();
//                else
//                    
//        });

        chooseFile=new FileChooser();
        chooseFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg","*.png","*.gif"));
        dbCon=Configs.getInstance();
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

    private boolean addNewCategory() {
        String id=nid.getText();
        String name=nName.getText();
        String desc=nDesc.getText();
        String image=nImage.getText();
        
        if(id.isEmpty() || name.isEmpty() || desc.isEmpty() || image.isEmpty())
            errorMessage("Please Complete All the Fields");
        try {
            is=new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        Integer.valueOf(nid.getText().trim());
        String qu="INSERT INTO category VALUES(?,?,?,?)";
        if(!checkDuplicate(id,1) && !checkDuplicate(name,2)){
            System.out.println("In if after check");
            PreparedStatement pst=dbCon.execQueryPrep(qu);
        try {
            pst.setInt(1, Integer.valueOf(nid.getText().trim()));
            pst.setString(2, name);
            pst.setString(3, desc);
            pst.setBinaryStream(4, (InputStream)is, (int)file.length());
            
            //pst.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        try {
            if(pst.executeUpdate()!=0){
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Category.fxml"));
                
                
                CategoryController controller=loader.getController();
                Category item=new Category(CategoryController.serialno++,Integer.valueOf(id),name,desc,im);
                controller.list.add(item);
                controller.refreshTable();
                Parent root=(Parent)loader.load();
                
//                new Thread() {
//                    @Override
//                    public void run() {
//                        controller.insertData(item);
//                        controller.
//                    };
//                }.start();
//                //controller.insertData(item);
//                
//                return true;
//                //list.add(new Category(id,name,desc,new Image()))
//            }
            
           
        } 
        }catch (SQLException | IOException ex) {
            System.out.println("In else after check");
            System.out.println(ex.toString());
        }
        }
        else{
            errorMessage("Duplicate Values");
        }
        
        
        return false;
    }

    
    private void clearFields() {
        nid.clear();
        nName.clear();
        nDesc.clear();
        nImage.clear(); 
    }

    private boolean checkDuplicate(String id,Integer type) {
        String qu;
        if(type==1)
            qu="SELECT * FROM category WHERE CategoryID="+Integer.valueOf(id);
        else{
            System.out.println("In else");
            qu="SELECT CategoryName FROM category WHERE CategoryName='"+id+"'";
        }
        
        try {
            ResultSet rs=dbCon.execQuery(qu);
            if(rs.next()){
                System.out.println("If Found"+rs.getString("CategoryName")+"===="+rs.getInt("CategoryID"));
                return true;
            }
            else
            {
            System.out.println("Else "+rs.getString("CategoryName")+"===="+rs.getInt("CategoryID"));
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    
}
