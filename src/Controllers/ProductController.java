/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Category;
import MainPack.Product;
import MainPack.Supplier;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Fareed
 */
public class ProductController implements Initializable{

    @FXML
    private Label idLab;

    @FXML
    private Label nameLb;

    @FXML
    private Label supLab;

    @FXML
    private Label descLab;

    @FXML
    private Label upLab;

    @FXML
    private Label stkInLab;

    @FXML
    private Label stkOutLab;

    @FXML
    private Label ctLab;

    @FXML
    private TableView<Product> proTable;

    @FXML
    private TableColumn<Product,String> id;

    @FXML
    private TableColumn<Product,String> name;

    @FXML
    private TableColumn<Product,String> desc;

    @FXML
    private TableColumn<Product,String> unitPrice;

    @FXML
    private TableColumn<Product,String> stockIn;

    @FXML
    private TableColumn<Product,String> stockOut;
    
    @FXML
    private TableColumn<Product,String> pCategory;

    @FXML
    private ImageView imv;

    @FXML
    private JFXButton newItem;

    @FXML
    private JFXButton updateItem;

    @FXML
    private JFXButton deleteItem;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXTextField pSearch;

    @FXML
    private MaterialDesignIconView close;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    private JFXButton supplier;

    @FXML
    private JFXButton refresh;

    @FXML
    private AnchorPane rootPane;
   
    Configs con;
    private List<Product> items;
    ObservableList<Product> selectedRows,allProducts;
    ObservableList<Product> plist=FXCollections.observableArrayList();
    ObservableList<Category> categoryList=FXCollections.observableArrayList();
    ObservableList<Product> filteredData=FXCollections.observableArrayList();
    FilteredList<Product> filterList=new FilteredList<>(plist,e->true);
    public ProductController(){
        plist.addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Product> change) {
                
                updateFilteredData();
                System.out.println("In Constructor");
            }
        });
    }
    @FXML
    void searchIconMethod(MouseEvent event) {
        pSearch.requestFocus();
    }
    
    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Product> getList(){
        return plist;
    }
    @FXML
    void onAction(ActionEvent event) throws IOException {
        Stage win=new Stage();
        Scene sc;
        if(event.getSource()==newItem){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/productNew.fxml")); 
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
           
        }
        else if(event.getSource()==updateItem){
              FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/productUpdate.fxml"));
              Parent root=loader.load();
              ProductUpdateController puc=(ProductUpdateController)loader.getController();
              Product product=proTable.getSelectionModel().getSelectedItem();
              puc.getProduct(product);
             sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==closeButton){
            Stage stage=(Stage)closeButton.getScene().getWindow();
            stage.close();
        }
         else if(event.getSource()==supplier){
            Parent root=FXMLLoader.load(getClass().getResource("/FXML/productSupplier.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
        }
         else if(event.getSource()==deleteItem){
              ObservableList<Supplier> selectedRows,allCategories;
            allProducts=proTable.getItems();
            items =  new ArrayList (proTable.getSelectionModel().getSelectedItems());
            String id=proTable.getSelectionModel().getSelectedItem().getBarCode();
            Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
            a1.setTitle("Confirmation Dialog");
            a1.setContentText("Are Sure Want to Delete ?");
            a1.setHeaderText(null);
            Optional<ButtonType> action=a1.showAndWait();
            if(action.get()==ButtonType.OK){
            removeRecords(items);   
            }
         }
         else if(event.getSource()==refresh){
             refreshTable();
         }
        win.setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        con=Configs.getInstance();
        updateItem.setDisable(true);
        deleteItem.setDisable(true);
        loadTable();
        refreshTable();
        imv.setPreserveRatio(false);
        
        proTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        proTable.getSelectionModel().selectedItemProperty().addListener(((observable,oldVal,newVal)-> {
           Product sp=proTable.getSelectionModel().getSelectedItem();
           if(sp!=null){
            imv.setImage(sp.getImv());
            idLab.setText(sp.getBarCode());
            nameLb.setText(sp.getName());
            supLab.setText(sp.getSupp());
            descLab.setText(sp.getDesc());
            upLab.setText(sp.getUnitPrice());
            stkInLab.setText(sp.getStkIn());
            stkOutLab.setText(sp.getStockOut());
            ctLab.setText(sp.getCt().getName());
           }
           
        }));
        
        proTable.setRowFactory(new Callback<TableView<Product>, TableRow<Product>>() {  
        @Override  
        public TableRow<Product> call(TableView<Product> tableView2) {  
            final TableRow<Product> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                @Override  
                public void handle(MouseEvent event) {  
                    final int index = row.getIndex();
                    System.out.println(index);
                    if (index >= 0 && index < proTable.getItems().size() && proTable.getSelectionModel().isSelected(index)  ) {
                        proTable.getSelectionModel().clearSelection();
                        Product sp=proTable.getSelectionModel().getSelectedItem();
                        //proTable.getSelectionModel().getSelectedItem();
                        imv.setImage(null);
                        idLab.setText(null);
                        nameLb.setText(null);
                        supLab.setText(null);
                        descLab.setText(null);
                        upLab.setText(null);
                        stkInLab.setText(null);
                        stkOutLab.setText(null);
                        ctLab.setText(null);
                        updateItem.setDisable(true);
                        deleteItem.setDisable(true);
                        System.out.println("unClick");
                        event.consume();  
                    }
                    else if(!row.isSelected()){
                        System.out.println("Click");
                        
                        Product sp=proTable.getSelectionModel().getSelectedItem();
                        if(sp!=null){
                        imv.setImage(sp.getImv());
                        idLab.setText(sp.getBarCode());
                        nameLb.setText(sp.getName());
                        supLab.setText(sp.getSupp());
                        descLab.setText(sp.getDesc());
                        upLab.setText(sp.getUnitPrice());
                        stkInLab.setText(sp.getStkIn());
                        stkOutLab.setText(sp.getStockOut());
                        ctLab.setText(sp.getCt().getName());
                        
                        }
                        
                        updateItem.setDisable(false);
                        deleteItem.setDisable(false);
                    }
                }  
            });  
            return row;  
        }  
    }); 
        
        id.setCellValueFactory(cellData->cellData.getValue().barCodeProperty());
        name.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        desc.setCellValueFactory(cellData->cellData.getValue().descProperty());
        unitPrice.setCellValueFactory(cellData->cellData.getValue().unitPriceProperty());
        stockIn.setCellValueFactory(cellData->cellData.getValue().stkInProperty());
        stockOut.setCellValueFactory(cellData->cellData.getValue().stockOutProperty());
        pCategory.setCellValueFactory(cellData->cellData.getValue().getCt().nameProperty());
        
        pSearch.textProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                updateFilteredData();
            }
        });
        
        
    }

    private void updateFilteredData() {
        filteredData.clear();

        for(Product p : plist) {
            if (matchesFilter(p)) {
                filteredData.add(p);
            }
        }

      }
    private boolean matchesFilter(Product ct) {
        String filterString = pSearch.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }
        String lowerCaseFilterString = filterString.toLowerCase();

        if (ct.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (ct.getBarCode().toString().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }
    
    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<Product, ?>> sortOrder = new ArrayList<>(proTable.getSortOrder());
        proTable.getSortOrder().clear();
        proTable.getSortOrder().addAll(sortOrder);
    }
    
    private void loadTable() {
        String qu="SELECT * FROM products ";
        ResultSet rs=con.execQuery(qu);
        int s=0;
        int i=0;
         Product p=null;
        try {
            while(rs.next()){
               String bc=rs.getString("BarCode");
               String pname=rs.getString("ProductName");
               String pd=rs.getString("PDescription");
               String up=rs.getString("unitPrice");
               String so=rs.getString("stockOut");
                System.out.println("bc=="+bc);
               String sin=getQt(bc).toString();
               InputStream in=rs.getBinaryStream("picture");                //getting stream 
               OutputStream ou=new FileOutputStream(new File("photo.jpg"));
                byte[] content=new byte[1024];
                int size=0;
                while((size=in.read(content))!=-1){                          //converting stream into o/p stream
                    ou.write(content, 0, size);
                }
                Image im=new Image("file:photo.jpg",203,139,false,false);       // finally getting image object
               imv.setPreserveRatio(false);
               Category ci=new Category(rs.getInt("CategoryID"),getCategoryId(rs.getString("CategoryID"))); //category object with category id and name
               
               categoryList.add(ci);
               
               String si=rs.getString("SupplierID");
               Integer stkIn=rs.getInt("availPro");
               
               p=new Product(bc,pname,pd,up,stkIn.toString(),so,im,ci,si);
               
            plist.add(p);
            proTable.setItems(filteredData);
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private void removeRecords(List<Product> prod) {
        for(int i=0; i<prod.size(); i++){
            String qu="DELETE FROM products where BarCode="+prod.get(i).getBarCode();
            if(con.execAction(qu)){
                System.out.println("Executed");
             }
        }
        allProducts.removeAll(prod);
        refreshTable();
    }
    
    private String getCategoryId(String cId) {                                     //get Category Name here
        String query="SELECT CategoryName FROM category WHERE CategoryID="+Integer.valueOf(cId)+";";
        ResultSet rs=con.execQuery(query);
        try {
            if(rs.next()){
                return rs.getString("CategoryName");
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    private Integer getQt(String bc) {
        System.out.println("In getQt=="+bc);
        String qu="SELECT stockIn FROM inventory WHERE updateDate=(SELECT max(updateDate) FROM inventory WHERE BarCode="+Integer.valueOf(bc)+");";
        ResultSet rs=con.execQuery(qu);
        try {
            if(rs.next()){
                return rs.getInt("stockIn");
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return 0;
    }   

    public void refreshTable() {
         String qu="SELECT * FROM products ";
         filteredData.clear();
         plist.clear();
         ResultSet rs=con.execQuery(qu);
        int s=0;
        int i=0;
        try {
            while(rs.next()){
               String bc=rs.getString("BarCode");
               String pname=rs.getString("ProductName");
               String pd=rs.getString("PDescription");
               String up=rs.getString("unitPrice");
               String so=rs.getString("stockOut");
               String sin=getQt(bc).toString();
               InputStream in=rs.getBinaryStream("picture");                //getting stream 
               OutputStream ou=new FileOutputStream(new File("photo.jpg"));
                byte[] content=new byte[1024];
                int size=0;
                while((size=in.read(content))!=-1){                          //converting stream into o/p stream
                    ou.write(content, 0, size);
                }
                Image im=new Image("file:photo.jpg",203,139,false,false);      // finally getting image object
               imv.setPreserveRatio(false);
               Category ci=new Category(rs.getInt("CategoryID"),getCategoryId(rs.getString("CategoryID"))); //category object with category id and name
               
               categoryList.add(ci);
               Integer stkIn=rs.getInt("availPro");
               String si=rs.getString("SupplierID");
               Product p=new Product(bc,pname,pd,up,stkIn.toString(),so,im,ci,si);
               
               plist.add(p);
               proTable.setItems(filteredData) ;
               //proTable.setItems(plist);
               Product pr=plist.get(i++);
                System.out.println("Name="+pr.getBarCode()
                        +",Pname="+pr.getName()
                        +",pd="+pr.getDesc()
                        +",up="+pr.getUnitPrice()
                        +",so="+pr.getStockOut()
                        +",sin="+pr.getStkIn()
                        +",ci="+pr.getCt().getName()
                        +",si="+pr.getSupp()
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
    
    
}
