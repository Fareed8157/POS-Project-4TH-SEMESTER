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
        barcode.setText(product.getBarCode());
        name.setText(product.getName());
        uPrice.setText(product.getUnitPrice());
        suppChose.setValue(product.getSupp());
        desc.setText(product.getDesc());
        System.out.println("");
        String stkInDateOfThisProduct=getDateOfProduct(product.getBarCode());
        System.out.println("Date=="+stkInDateOfThisProduct);
        stkInDate.setValue(LocalDate.parse(stkInDateOfThisProduct));
        stockIn.setText(product.getStkIn());
        category.setValue(product.getCt().getName());
        picField.setText(product.getImv().toString()); 
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
    
    private boolean saveData() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Product.fxml"));
        try {
            Parent root=loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProductNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ProductController puc=(ProductController)loader.getController();
        Product pro=null;
        if(!isFieldEmpty()){
            String bcode=barcode.getText();
            String pname=name.getText();
            String des=desc.getText();
            String upr=uPrice.getText();
            String stkOut="";
            String picF=picField.getText();
            Integer cno=findId(category.getValue());
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
            //String ctg=category.getValue().toString();
            String sc=suppChose.getValue().toString();
            String stkin=stockIn.getText();                     //product tracking data
            String stkDate=stkInDate.getValue().toString();    //product tracking data
            String stkInType=stockType.getValue().toString();   //chose Type for existing recording or new Record which you are going to Update
            String qu="";
            PreparedStatement ps;
            
            if(!(picField.getText().equals(product.getImv().toString()))){    //execute when picture is changed
                qu="UPDATE products SET BarCode=?, ProductName=?,PDescription=?,unitPrice=?,stockOut=?,picture=?,CategoryID=?,SupplierID=? WHERE BarCode="+Integer.valueOf(product.getBarCode());
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
                            ps.setString(5, "0");
                            ps.setBinaryStream(6, (InputStream)is, (int)file.length());
                            ps.setInt(7, cno);
                            ps.setString(8, sc);
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
                   System.out.println("Inside else ");
                   qu="UPDATE products SET ProductName=?,PDescription=?,unitPrice=?,stockOut=?,picture=?,CategoryID=?,SupplierID=? WHERE BarCode =?";
//                    qu="UPDATE products SET ProductName=?, "
//                        + "PDescription=?,unitPrice=?,stockOut=?,picture=?,CategoryID=?"
//                            + ",SupplierID=? WHERE BarCode="+Integer.valueOf(bcode);
                    ps=con.execQueryPrep(qu);
                    System.out.println("excuted7");
                    System.out.println("after execution Inside else ");
                    try {
                       // ps.setString(1, bcode);
                            ps.setString(1, pname);
                            ps.setString(2, des);
                            ps.setString(3, upr);
                            ps.setString(4, "0");
                            ps.setBinaryStream(5, (InputStream)is, (int)file.length());
                            ps.setInt(6, cno);
                            ps.setString(7, sc);
                            ps.setInt(8, Integer.valueOf(bcode));
                        if(ps.executeUpdate()!=0){
                             ps.close();
                             
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
                            System.out.println("excuted1");
                        
                        puc.getList().add(pro);
                        puc.refreshTable();
                        return true;
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.toString());
                        return false;
                    }
                }
            }         //end of if
            else{
                qu="UPDATE products SET BarCode=?, productName=?, "
                + "PDescription=?,unitPrice=?,stockOut=?,"
                + "CategoryID=?,SupplierID=? WHERE BarCode="+Integer.valueOf(product.getBarCode());
               if(!(product.getBarCode().equals(barcode.getText()))){ //execute when bar code is not same
                    if(!checkDuplicate(bcode)){               //execute when there is not any record with new barcode
                        try {
                            ps=con.execQueryPrep(qu);
                            //product Table
                            ps.setString(1, bcode);
                            ps.setString(2, pname);
                            ps.setString(3, des);
                            ps.setString(4, upr);
                            ps.setString(5, "0");
                            ps.setInt(6, cno);
                            ps.setString(7, sc);
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
                    qu="UPDATE products SET ProductName='"+pname+"',PDescription='"+des+"',unitPrice="+0+",CategoryID="+cno+",SupplierID="+sc+" WHERE BarCode="+Integer.valueOf(bcode);
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
//                        ps.setString(1, pname);
//                        ps.setString(2, des);
//                        ps.setString(3, upr);
//                        ps.setString(4, "0");
//                        ps.setInt(5, cno);
//                        ps.setString(6, sc);
//                        System.out.println("excuted10");
//                        ps.executeUpdate(qu);
//                        System.out.println("excuted5");
                        //ps.close();
                        //inventory Table
                        
                    } catch (SQLException ex) {
                        System.out.println(ex.toString());
                        return false;
                    }
                }
            }
            
        }
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

    

   

    
}
