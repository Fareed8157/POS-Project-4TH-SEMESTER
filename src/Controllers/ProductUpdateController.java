/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Controllers.ProductNewController.flag;
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
import java.time.LocalDate;
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
public class ProductUpdateController implements Initializable{

   @FXML
    private MaterialDesignIconView close;

    @FXML
    private JFXTextField barcode;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField uPrice;

    @FXML
    private JFXComboBox<String> suppChose;

    @FXML
    private JFXTextField desc;

    @FXML
    private JFXComboBox<String> stockType;

    @FXML
    private JFXDatePicker stkInDate;

    @FXML
    private JFXTextField stockIn;

    @FXML
    private JFXComboBox<String> category;

    @FXML
    private JFXTextField picField;

    @FXML
    private JFXButton browse;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    Product product=null;
    
    Configs con;
    private ObservableList<Product> plist=FXCollections.observableArrayList();
    private ObservableList categoryList=FXCollections.observableArrayList();
    private ObservableList stockTypeList=FXCollections.observableArrayList();
    private ObservableList<ProductTracking> inventory=FXCollections.observableArrayList();
    private ObservableList supplierList=FXCollections.observableArrayList();
     
    private FileInputStream is=null;
    private ImageView imv=null;
    private Image im=null;
    private FileChooser chooseFile;
    private File file=null;  
    private Desktop desktop=Desktop.getDesktop();
    
    public void getProduct(Product product){
        this.product=product;
        barcode.setText(product.getBarCode().toLowerCase());
        name.setText(product.getName().toLowerCase());
        uPrice.setText(product.getUnitPrice().toLowerCase());
        suppChose.setValue(product.getSupp().toLowerCase());
        System.out.println("Supplier="+product.getSupp());
        desc.setText(product.getDesc().toLowerCase());
        System.out.println("");
        String stkInDateOfThisProduct=getDateOfProduct(product.getBarCode());
        System.out.println("Date=="+stkInDateOfThisProduct);
        if(stkInDateOfThisProduct==null){
            stkInDate.setValue(null);
        }else{
            stkInDate.setValue(LocalDate.parse(stkInDateOfThisProduct));
        }
        stockIn.setText(product.getStkIn().toLowerCase());
        category.setValue(product.getCt().getName().toLowerCase());
        picField.setText(product.getImv().toString().toLowerCase()); 
    }
    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAction(ActionEvent event) {
        if(event.getSource()==save){
               flag=saveData();            //laoding data into database
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
                try {
                    is=new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ProductUpdateController.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        stkInDate.setDisable(true);
        stockIn.setDisable(true);
        //stockType.setDisable(true);
        loadCategory();
        loadSupplier();
        loadType();
        category.getItems().addAll(categoryList);
        suppChose.getItems().addAll(supplierList);
        stockType.getItems().addAll(stockTypeList);
        stockType.setOnAction(e->{
            stkInDate.setDisable(false);
             stockIn.setDisable(false);
        });
        stockType.setValue("Existing");
    }
    private Integer getStockOut(Integer valueOf) {
    String qu="SELECT stockOut FROM products WHERE BarCode="+valueOf;
    ResultSet rs=con.execQuery(qu);
       try {
           if(rs.next()){
               return rs.getInt("stockOut");
           }  } catch (SQLException ex) {
           Logger.getLogger(ProductUpdateController.class.getName()).log(Level.SEVERE, null, ex);
       }
       return 0;
    }
    private boolean saveData() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Product.fxml"));
        try {
            Parent root=loader.load();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        ProductController puc=(ProductController)loader.getController();
        Product pro=null;
        if(!isFieldEmpty()){
           if(barcodeValidation() & nameValidation() & descValidation() & uPriceValidation() & stockInValidation()){
            String bcode=barcode.getText().toLowerCase();
            String pname=name.getText().toLowerCase();
            String des=desc.getText().toLowerCase();
            String upr=uPrice.getText().trim().toLowerCase();
            String stkOut="";
            String picF=picField.getText().toLowerCase();
            Integer cno=findId(category.getValue().toLowerCase());
            System.out.println("CategoryID="+cno);
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
            String stkInType=stockType.getValue().toString().toLowerCase();   //chose Type for existing recording or new Record which you are going to Update
            String qu="";
            PreparedStatement ps;
            
            if(!(picField.getText().equals(product.getImv().toString()))){    //execute when picture is changed
                qu="UPDATE products SET BarCode=?, ProductName=?,PDescription=?,unitPrice=?,stockOut=?,picture=?,CategoryID=?,SupplierID=?,availPro=? WHERE BarCode="+Integer.valueOf(product.getBarCode());
                System.out.println("after quwey");
               if(!(product.getBarCode().equals(barcode.getText()))){    //execute when brcode is changed
                    if(!checkDuplicate(bcode)){                           //ie checks for changed barcode whether it is in db or not
                        try {
                            ps=con.execQueryPrep(qu);
                            System.out.println("after execution");
                            //product Table
                            System.out.println("bcode=="+bcode);
                            ps.setInt(1, Integer.valueOf(bcode));
                            ps.setString(2, pname);
                            ps.setString(3, des);
                            ps.setString(4, upr);
                            System.out.println("Upr="+upr);
                            ps.setInt(5, getStockOut(Integer.valueOf(bcode)));
                            ps.setBinaryStream(6, (InputStream)is, (int)file.length());
                            ps.setInt(7, cno);
                            ps.setString(8, sc);
                            ps.setString(9,stkin);
                            
                            System.out.println("Before executupdate");
                            if(ps.executeUpdate()!=0){
                             ps.close();
                            pro=new Product(bcode,pname,des,upr,stkin,stkOut,im,ctg,sc);
                            //inventory Table
                             if((stkInType.equals("Existing"))){
                                qu="UPDATE inventory SET stockIn="+product.getStkIn()+",updateDate='"+tsdf.format(today)+"' WHERE BarCode="+Integer.valueOf(bcode);
                                ps=con.execQueryPrep(qu);
                                ps.executeUpdate();
                                ps.close();
                                }else{
                                qu="INSERT INTO inventory VALUES("+stkin+","+bcode+",'"+tsdf.format(today)+"')";
                                con.execAction(qu);
                                } 
                             puc.getList().add(pro);
                             puc.refreshTable();
                             return true;   
                            }
                            
                            System.out.println("excuted8");
                      } catch (SQLException ex) {
                            System.out.println(ex.toString());
                            return false;
                        }
                    }
                    else{
                        errorMessage("Duplicate BarCode");  
                        return false;
                    }
                }else{                                   //execute when bar code is same as in table 
                   try {
                        qu="UPDATE products SET BarCode=?, ProductName=?,PDescription=?,unitPrice=?,stockOut=?,picture=?,CategoryID=?,SupplierID=?,availPro=? WHERE BarCode="+Integer.valueOf(product.getBarCode());
                            ps=con.execQueryPrep(qu);
                            System.out.println("after execution");
                            //product Table
                            System.out.println("bcode=="+bcode);
                            ps.setInt(1, Integer.valueOf(bcode));
                            ps.setString(2, pname);
                            ps.setString(3, des);
                            ps.setString(4, upr);
                            System.out.println("Upr="+upr);
                            ps.setInt(5, getStockOut(Integer.valueOf(bcode)));
                            ps.setBinaryStream(6, (InputStream)is, (int)file.length());
                            ps.setInt(7, cno);
                            ps.setString(8, sc);
                            ps.setString(9,stkin);
                            
                            System.out.println("Before executupdate");
                            if(ps.executeUpdate()!=0){
                             ps.close();
                            pro=new Product(bcode,pname,des,upr,stkin,stkOut,im,ctg,sc);
                            //inventory Table
                             if((stkInType.equals("Existing"))){
                                qu="UPDATE inventory SET stockIn="+product.getStkIn()+",updateDate='"+tsdf.format(today)+"' WHERE BarCode="+Integer.valueOf(bcode);
                                ps=con.execQueryPrep(qu);
                                ps.executeUpdate();
                                ps.close();
                                }else{
                                qu="INSERT INTO inventory VALUES("+stkin+","+bcode+",'"+tsdf.format(today)+"')";
                                con.execAction(qu);
                                } 
                             puc.getList().add(pro);
                             puc.refreshTable();
                             return true;   
                            }
                            
                            System.out.println("excuted8");
                      } catch (SQLException ex) {
                            System.out.println(ex.toString());
                            return false;
                        }
                }
            }         //end of if
            else{
                qu="UPDATE products SET BarCode=?, productName=?, "
                + "PDescription=?,unitPrice=?,stockOut=?,"
                + "CategoryID=?,SupplierID=?,availPro=? WHERE BarCode="+Integer.valueOf(product.getBarCode());
               if(!(product.getBarCode().equals(barcode.getText()))){ //execute when bar code is not same
                    if(!checkDuplicate(bcode)){               //execute when there is not any record with new barcode
                        try {
                            ps=con.execQueryPrep(qu);
                            //product Table
                            ps.setString(1, bcode);
                            ps.setString(2, pname);
                            ps.setString(3, des);
                            ps.setString(4, upr);
                            System.out.println("Upr="+upr);
                            ps.setInt(5, getStockOut(Integer.valueOf(bcode)));
                            ps.setInt(6, cno);
                            ps.setString(7, sc);
                            ps.setString(7, stockIn.getText());
                            System.out.println("excuted12");
                            if(ps.executeUpdate()!=0){
                                pro=new Product(bcode,pname,des,upr,stkin,stkOut,im,ctg,sc);
                                puc.getList().add(pro);
                                puc.refreshTable();
                                

                                //inventory Table
                                
                                if((stkInType.equals("Existing"))){
                                qu="UPDATE inventory SET stockIn="+product.getStkIn()+",updateDate='"+tsdf.format(today)+"' WHERE BarCode="+Integer.valueOf(bcode);
                                ps=con.execQueryPrep(qu);
                                ps.executeUpdate();
                                ps.close();
                                }else{
                                qu="INSERT INTO inventory VALUES("+stkin+","+bcode+",'"+tsdf.format(today)+"')";
                                con.execAction(qu);
                                }
                                return true;
                            }
                            System.out.println("excuted13");
                            ps.close();
                        } catch (SQLException ex) {
                            System.out.println(ex.toString());
                        }
                   }
                    else{
                        errorMessage("Duplicate BarCode"); 
                        return false;
                    }
                }else{
//                    qu="UPDATE products SET ProductName=?, "
//                        + "PDescription=?,unitPrice=?,stockOut=?,CategoryID=?,SupplierID=? WHERE BarCode="+Integer.valueOf(bcode);
                    qu="UPDATE products SET ProductName='"+pname+"',PDescription='"+des+"',unitPrice="+Math.round(Double.valueOf(upr))+",stockOut="+getStockOut(Integer.valueOf(bcode))+",CategoryID="+cno+",SupplierID="+sc+",availPro="+Integer.valueOf(stockIn.getText())+" WHERE BarCode="+Integer.valueOf(bcode);
                    System.out.println("Query"+qu);
                    //ps=con.execQueryPrep(qu);
                    System.out.println("excuted6");
                    try {
                        
                        if(con.execAction(qu)){
                            if((stkInType.equals("Existing"))){
                            qu="UPDATE inventory SET stockIn="+product.getStkIn()+",updateDate='"+tsdf.format(today)+"' WHERE BarCode="+Integer.valueOf(bcode);
                            ps=con.execQueryPrep(qu);
                            ps.executeUpdate();
                            ps.close();
                        }else{
                            qu="INSERT INTO inventory VALUES("+stkin+","+bcode+",'"+tsdf.format(today)+"')";
                            con.execAction(qu);
                        }
                        puc.getList().add(pro);
                        puc.refreshTable();
                         return true;
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.toString());
                        return false;
                    }
                }
            }
            
        }//end of inner if              
    }//end of outer if

       return false;
}
    
    private boolean checkDuplicate(String id) {
        String qu="SELECT * FROM products WHERE BarCode="+id;
        try {
            ResultSet rs=con.execQuery(qu);
            if(rs.next()){
                System.out.println("BarCode in dupl=="+rs.getInt("BarCode"));
                return true;
            }
                
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
     private void loadType() {
         stockTypeList.addAll("New","Existing");
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
    private boolean isFieldEmpty() {
        if(barcode.getText().isEmpty() || name.getText().isEmpty() || uPrice.getText().isEmpty() || suppChose.getValue().isEmpty()
          || stockIn.getText().isEmpty() || stkInDate.getValue().toString().isEmpty() || category.getValue().isEmpty() ||
          desc.getText().isEmpty() || picField.getText().isEmpty()) {
           errorMessage("Fill All Field");
            return true;
        }
        if(suppChose.getSelectionModel().isEmpty() || 
        stockType.getSelectionModel().isEmpty() 
        || category.getSelectionModel().isEmpty()){
         errorMessage("Select Value in Choice Box");
         suppChose.requestFocus();
         stockType.requestFocus();
         category.requestFocus();
         return true;
        }
    return false;
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

    private String getDateOfProduct(String bc) {
        String qu="SELECT max(updateDate) updateDate FROM inventory WHERE BarCode="+Integer.valueOf(bc)+";";
        ResultSet rs=con.execQuery(qu);
        try {
            if(rs.next()){
                return rs.getString("updateDate");
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return null;
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
        Pattern p=Pattern.compile("[0.0-9.9]+");
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
        if(m.find() && m.group().equals(desc.getText()))
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
            a1.setTitle("Validate Image Pathe");
            a1.setContentText("Please Enter Valid Image Path");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }

    
    
}
