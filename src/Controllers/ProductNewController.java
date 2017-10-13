/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Category;
import MainPack.Product;
import MainPack.ProductTracking;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
     ObservableList<Product> plist=FXCollections.observableArrayList();
     ObservableList categoryList=FXCollections.observableArrayList();
     ObservableList<ProductTracking> inventory=FXCollections.observableArrayList();
     ObservableList supplierList=FXCollections.observableArrayList();
    private FileInputStream is=null;
    private ImageView imv=null;
    private Image im=null;
    private FileChooser chooseFile;
    private File file=null;  
    private Desktop desktop=Desktop.getDesktop();
    
    Configs con;
    static boolean flag;
    public boolean getStatus(){
       return flag; 
    }
    @FXML
    void closeStage(MouseEvent event) {
        Stage st=(Stage)close.getScene().getWindow();
        st.close();
    }
    
    @FXML
    void onAction(ActionEvent event) throws FileNotFoundException {
        if(event.getSource()==save){
               flag=saveData();            //laoding data into database
               System.out.println("flag=="+flag);
         if(flag){
             infoMessage("Saved Successfully");
             clearFields();
            } 
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
        else if(event.getSource()==cancel){
            Stage win=(Stage)cancel.getScene().getWindow();
            win.close();
        }
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
        loadCategory();
        loadSupplier();
        stkInDate.setStyle("-fx-font-size:20");
        category.getItems().addAll(categoryList);
        suppChose.getItems().addAll(supplierList);
   }

    private boolean saveData() {
        boolean flag1=false;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Product.fxml"));
        try {
            Parent root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProductNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ProductController puc=(ProductController)loader.getController();
        
      //  boolean flag1;
        if(!isFieldEmpty()){
            if(barcodeValidation() & nameValidation() & descValidation() & uPriceValidation() & stockInValidation()){
               String bcode=barcode.getText().toLowerCase();
            String pname=name.getText().toLowerCase();
            String des=desc.getText().toLowerCase();
            String upr=uPrice.getText().toLowerCase();
            String stkOut="";
            String picF=picField.getText().toLowerCase();
            Integer cno=findId(category.getValue().toLowerCase());
            Category ctg=new Category(cno);
            String date=((JFXTextField)stkInDate.getEditor()).getText();
            Date today = new Date(date);
            SimpleDateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd");
            try{
                System.out.println(tsdf.format(today));
            }
            catch (Exception e){
                System.out.println("Error occurred" + e.getMessage());
            }
            String sc=suppChose.getValue().toString().toLowerCase();
            String stkin=stockIn.getText().toLowerCase();                     //product tracking data
            String stkDate=stkInDate.getValue().toString().toLowerCase();    //product tracking data
            System.out.println("before  execute");
            if(!checkDuplicate(bcode)){
                System.out.println("Inside if execute");
                //String qu="INSERT INTO products VALUES(?,?,?,?,?,?,?,?)";
                String qu="INSERT INTO products VALUES(?,?,?,?,?,?,"+"("+"Select CategoryID From category where CategoryName='"+category.getValue().toString()+"')"+",?,?)";
                PreparedStatement pst=con.execQueryPrep(qu);
                System.out.println("After prepared");
                try {
                pst.setString(1,bcode);
                pst.setString(2, pname);
                pst.setString(3, des);
                pst.setString(4,upr);
                //pst.setNull(5, 0);
                pst.setString(5, "0");
                pst.setBinaryStream(6, (InputStream)is, (int)file.length());
                //pst.setString(7,ctg.getId().toString());
                pst.setString(7,sc);
                pst.setString(8,stkin);
                Product pro=new Product(bcode,pname,des,upr,stkin,stkOut,im,ctg,sc);
                
                System.out.println("After execute only");
                if(pst.executeUpdate()!=0){
                    System.out.println("After execute");
                    plist.add(pro);
                    //inserting to inventory
                    qu="INSERT INTO inventory VALUES(?,?,?)";
                    pst=con.execQueryPrep(qu);
                    pst.setString(1, stkin);
                    pst.setString(2, bcode);
                    pst.setString(3, tsdf.format(today));
                    if(pst.execute()){
                        inventory.add(new ProductTracking(stkin,bcode,today));
                    }
                    pst.close();
                    puc.getList().add(pro);         //add product to the table
                    puc.refreshTable();
                    return true;
                }
                pst.close();
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
            }
            else{
               errorMessage("Duplicate Bar code"); 
                return false;
                }   
            }//end of inner if
        }//end of outer if
            System.out.println("at the end of execute");
        return false;
    }

    private boolean checkDuplicate(String id) {
        String qu="SELECT * FROM products WHERE BarCode="+id;
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

    private void loadSupplier() {
        String qu="SELECT SupplierID FROM supplier";
        ResultSet rs=con.execQuery(qu);
        try {
            while(rs.next()){
                supplierList.add(rs.getString("SupplierID"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
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
            System.out.println(ex.toString());
        }
    }
    
    private boolean isFieldEmpty() {
        if(barcode.getText().isEmpty() || name.getText().isEmpty() || uPrice.getText().isEmpty() || suppChose.getValue().isEmpty()
          || stockIn.getText().isEmpty() || stkInDate.getValue().toString().isEmpty() || category.getValue().isEmpty() ||
          desc.getText().isEmpty() || picField.getText().isEmpty()) {
           errorMessage("Fill All Field");
            return true;
        }
         if(suppChose.getSelectionModel().isEmpty() || category.getSelectionModel().isEmpty()){
         errorMessage("Select Value in Choice Box");
         suppChose.requestFocus();
         category.requestFocus();
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

    private Integer findId(String valueOf) {
        String query="SELECT CategoryID FROM category WHERE CategoryName='"+valueOf+"';";
        ResultSet rs=con.execQuery(query);
        try {
            if(rs.next()){
                return rs.getInt("CategoryID");
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return 0;
    }

    public void clearFields(){
        barcode.clear();
        name.clear();
        uPrice.clear();
        suppChose.setValue(null);
        stockIn.clear();
        stkInDate.setValue(null);
        category.setValue(null);
        desc.clear();
        picField.clear();
    }
    private boolean barcodeValidation(){
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(barcode.getText());
        if(m.find() && m.group().equals(barcode.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate BarCode");
            a1.setContentText("Please Enter Valid BarCode");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean uPriceValidation(){
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(uPrice.getText());
        if(m.find() && m.group().equals(uPrice.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Unit Price");
            a1.setContentText("Please Enter Valid Unit Price");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean stockInValidation(){
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(stockIn.getText());
        if(m.find() && m.group().equals(stockIn.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Stock In");
            a1.setContentText("Please Enter Valid Stock In");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean nameValidation() {
        Pattern p=Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
        Matcher m=p.matcher(name.getText());
        if(m.find() && m.group().equals(name.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Product Name");
            a1.setContentText("Please Enter Valid Product Name");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean descValidation() {
        Pattern p=Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
        Matcher m=p.matcher(desc.getText());
        //if(m.find() && m.group().equals(desc.getText()))
        if(desc.getText().matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)"))
        return true;  
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Product Description");
            a1.setContentText("Please Enter Valid Product Description");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    private boolean picFieldValidation() {
        Pattern p=Pattern.compile("([a-zA-Z]:)?(\\\\\\\\?[a-zA-Z0-9_.-]+)*\\\\?\\\\?");
        Matcher m=p.matcher(picField.getText());
        if(m.find() && m.group().equals(picField.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Image Path");
            a1.setContentText("Please Enter Valid Image Path");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }
    
}
