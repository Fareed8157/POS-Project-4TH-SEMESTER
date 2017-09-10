/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import DBConnection.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import student.Category;

/**
 *
 * @author Fareed
 */
public class CategoryController implements Initializable{
    
    @FXML
    private JFXButton browse;
     
    @FXML
    private TableColumn<?,?> sFname;

    @FXML
    private TableColumn<?, ?> sLname;

    @FXML
    private TableColumn<?, ?> sEmail;

    @FXML
    private TableColumn<?, ?> sPhno;

    @FXML
    private JFXTextField sFnamField;

    @FXML
    private JFXTextField sLnamField;

    @FXML
    private JFXTextField sEmailField;

    @FXML
    private JFXTextField sPhnoFiled;

    @FXML
    private JFXButton sAdd;

    @FXML
    private JFXButton sDel;

    @FXML
    private JFXButton closeButton;
    
    @FXML
    private TableView<Category> catTable;
    
    @FXML
    private TableColumn<Category, Number> cId;

     @FXML
    private TableColumn<Category, Number> sno;
    
    @FXML
    private TableColumn<Category, String> cname;

    @FXML
    private JFXButton refresh;
    
    @FXML
    private TableColumn<Category, String> desc;

    @FXML
    private TableColumn<Category, ImageView> pic;

    @FXML
    private JFXButton newItem;

    @FXML
    private JFXButton updateItem;

    @FXML
    private JFXButton deleteItem;

    @FXML
    private MaterialDesignIconView close;
    
    Configs dbCon=null;
    
    //ObservableList<Category> clist=FXCollections.observableArrayList();
    static int serialno=0;
    ObservableList<Category> list=FXCollections.observableArrayList();
    public CategoryController(){
       //list.add(new Category(1,"SHOES","SHOES ONLY",new Image("file:///C:/Users/AbdulWaheed/Desktop/images.jpg"),100,150,true,true)));
       //list.add(new Category(1,"SHOES","SHOES ONLY",));
    }
    
    @FXML
    void closeStage(MouseEvent event) {
        Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }
   
    @FXML
    void onAction(ActionEvent event) throws IOException {
        Stage win=new Stage();
        Scene sc;
        if(event.getSource()==newItem){
          Parent root=FXMLLoader.load(getClass().getResource("/FXML/categoryNew.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            
            win.show();
        }
        if(event.getSource()==updateItem){
            Category cat=catTable.getSelectionModel().getSelectedItem();
             FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/categoryUpdate.fxml"));
            Parent root=loader.load();
            CategoryUpdateContoller cnc=loader.getController();
            cnc.getCategory(cat);
//            Parent root=FXMLLoader.load(getClass().getResource("/FXML/categoryUpdate.fxml"));           
            sc=new Scene(root);
            win.initModality(Modality.WINDOW_MODAL);
            win.setScene(sc);
            win.show();
        }
        else if(event.getSource()==closeButton){
            Stage stage=(Stage)closeButton.getScene().getWindow();
            stage.close();
        }
        else if(event.getSource()==deleteItem){
            Integer id=catTable.getSelectionModel().getSelectedItem().getId();
            Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
            a1.setTitle("Confirmation Dialog");
            a1.setContentText("Are Sure Want to Delete ?");
            a1.setHeaderText(null);
            Optional<ButtonType> action=a1.showAndWait();
            win.initModality(Modality.WINDOW_MODAL);
            if(action.get()==ButtonType.OK){
            removeRecord(id);    
            }
            
        }
        else if(event.getSource()==refresh){ 
           refreshTable();
            deleteItem.setDisable(true);
            updateItem.setDisable(true);
        }
        if(catTable.getSelectionModel().getFocusedIndex()==-1){
            deleteItem.setDisable(true);
            updateItem.setDisable(true);
            
        }
       
        win.setResizable(false);
    }
    
    
    public void insertData(Category e){
        list.add(e);
        
        //list.removeAll(list);
        //list.clear();
        //list.add(item);
        //loadData();
        //catTable.setItems(list);
        
        System.out.println("Executed");
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateItem.setDisable(true);
        deleteItem.setDisable(true);
        catTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        
        catTable.setRowFactory(new Callback<TableView<Category>, TableRow<Category>>() {  
        @Override  
        public TableRow<Category> call(TableView<Category> tableView2) {  
            final TableRow<Category> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                @Override  
                public void handle(MouseEvent event) {  
                    final int index = row.getIndex();
                    System.out.println(index);
                    if (index >= 0 && index < catTable.getItems().size() && catTable.getSelectionModel().isSelected(index)  ) {
                        catTable.getSelectionModel().clearSelection();
                       updateItem.setDisable(true);
                        deleteItem.setDisable(true);
                        event.consume();  
                    }
                    else if(!row.isSelected()){
                         updateItem.setDisable(false);
                        deleteItem.setDisable(false);
                    }
                    
                }  
            });  
            return row;  
        }  
    });  
    


        //define columns here
        //cId.setCellValueFactory(new PropertyValueFactory<Category,Number>("id"));
        sno.setCellValueFactory(cellData->cellData.getValue().serialNoProperty());
        //sno.setCellValueFactory(new PropertyValueFactory<Category,Integer>("serialNo"));
        //sno.setCellValueFactory(cellData->cellData.getValue().serialNoProperty());
        cId.setCellValueFactory(cellData->cellData.getValue().idProperty());
        cname.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        desc.setCellValueFactory(cellData->cellData.getValue().descProperty());
        pic.setCellValueFactory(new PropertyValueFactory<Category,ImageView>("imv"));
        close.requestFocus();
        
        //listnener and make selection
        catTable.getSelectionModel().selectedItemProperty().addListener(((observable,oldVal,newVal)-> {
            updateItem.setDisable(false);
            deleteItem.setDisable(false);
            
        }));
        catTable.setOnKeyPressed(event -> {
            TablePosition<Category,String> pos = catTable.getFocusModel().getFocusedCell() ;
            if (pos != null) {
                catTable.edit(pos.getRow(), pos.getTableColumn());
            }
        });
        
        //set editting
        cId.setOnEditCommit(e->setEditableCell1(e));
        cId.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        cname.setOnEditCommit(e->setEditableCell2(e));
        cname.setCellFactory(TextFieldTableCell.forTableColumn());
        desc.setOnEditCommit(e->setEditableCell3(e));
        desc.setCellFactory(TextFieldTableCell.forTableColumn());
        //cname.setCellValueFactory(cellData->cellData.getValue().idProperty());
        //file Coding
        
       //catTable.setItems(list);
       //catTable.refresh();
        //get Database Connection 
        dbCon=Configs.getInstance();
        loadData();
        catTable.setEditable(true);
    }
    
     void loadData() {
        String qu="SELECT * FROM category";
        ResultSet rs=dbCon.execQuery(qu);
        
        try {
            while(rs.next()){
                Integer id=rs.getInt(1);
                String name=rs.getString(2);
                String desc=rs.getString(3);
                InputStream in=rs.getBinaryStream(4);
                OutputStream ou=new FileOutputStream(new File("photo.jpg"));
                byte[] content=new byte[1024];
                int size=0;
                while((size=in.read(content))!=-1){
                    ou.write(content, 0, size);
                }
                
                Image im=new Image("file:photo.jpg",50,100,true,true);
                
                
                list.add(new Category(serialno++,id,name,desc,im));
                catTable.setItems(list);
            }
        } catch (SQLException ex) {
          
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //catTable.setItems(list);
    }
     
     public void refreshTable(){
         list.clear();
         serialno=0;
         String qu="SELECT * FROM category";
        ResultSet rs=dbCon.execQuery(qu);
        
        try {
            while(rs.next()){
                Integer id=rs.getInt(1);
                String name=rs.getString(2);
                String desc=rs.getString(3);
                InputStream in=rs.getBinaryStream(4);
                OutputStream ou=new FileOutputStream(new File("photo.jpg"));
                byte[] content=new byte[1024];
                int size=0;
                while((size=in.read(content))!=-1){
                    ou.write(content, 0, size);
                }
                
                Image im=new Image("file:photo.jpg",50,100,true,true);
                
                list.add(new Category(serialno++,id,name,desc,im));
                
                catTable.setItems(list) ;
            }
        } catch (SQLException ex) {
          
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //catTable.setItems(list);
     }

    
    private void removeRecord(Integer id) {
        String qu="DELETE FROM category where CategoryId="+id;
        if(dbCon.execAction(qu)){
            refreshTable();
            serialno--;
        }
    }

    //UPDATE METHODS
    private void setEditableCell1(TableColumn.CellEditEvent<Category, Number> e) {
        
        TableColumn.CellEditEvent<Category,Number> cell;
         cell=(TableColumn.CellEditEvent<Category, Number>) e;
         int i=Integer.parseInt(cell.getNewValue().toString());
         try {
                 
                 if(cell.getNewValue().toString()!=cell.getOldValue().toString()){
             
                     update(cell);
                }
                 
             } catch (Exception  numberFormatException) {
                 errorMessage("Enter Valide Number");
             }
         
         
        //Category st=cell.getRowValue();
        
         
         refreshTable();
    }

    private void setEditableCell2(TableColumn.CellEditEvent<Category, String> e) {
        TableColumn.CellEditEvent<Category, String> cell;
        cell=(TableColumn.CellEditEvent<Category, String>) e;
        Category st=cell.getRowValue();
         if(cell.getNewValue().toString()!=cell.getOldValue().toString()){
             updateString(cell,1);          
        }
         refreshTable();
    }

    private void setEditableCell3(TableColumn.CellEditEvent<Category, String> e) {
        TableColumn.CellEditEvent<Category, String> cell;
        cell=(TableColumn.CellEditEvent<Category, String>) e;
        Category st=cell.getRowValue();
         if(cell.getNewValue().toString()!=cell.getOldValue().toString()){
            updateString(cell,2);           
        }
         refreshTable();
    }
    
    private void update(TableColumn.CellEditEvent<Category, Number> cell) {
        Category ct=cell.getRowValue();
        String id=ct.getId().toString();
        String name=ct.getName();
        String desc=ct.getDesc();
        boolean flag=checkDuplicate(cell.getNewValue().toString(),1);
        if(!flag){
            String qu="UPDATE category SET CategoryID="+cell.getNewValue()+",CategoryName='"+name+"',Description='"+desc+"' WHERE CategoryID="+id;
            dbCon.execAction(qu);
            infoMessage("Updated Successfully In Database");
        }
        if(flag){
            errorMessage("Duplicate ID");
        }
        
    }
        

    //String Update 
    private void updateString(TableColumn.CellEditEvent<Category, String> cell,Integer type) {
        Category ct=cell.getRowValue();
        String id=ct.getId().toString();
        String name=ct.getName();
        String desc=ct.getDesc();
        String qu;
        if(type==1){
            qu="UPDATE category SET CategoryID="+id+",CategoryName='"+cell.getNewValue()+"',Description='"+desc+"' WHERE CategoryID="+id;
            boolean flag=checkDuplicate(cell.getNewValue().toString(),2);
            if(!flag){
                if(dbCon.execAction(qu))
            infoMessage("Update Successfully In Database");
            }
            if(flag){
                errorMessage("Duplicate ID");
            }
        }
        else{
            qu="UPDATE category SET CategoryID="+id+",CategoryName='"+desc+"',Description='"+cell.getNewValue()+"' WHERE CategoryID="+id;
            if(dbCon.execAction(qu))
            infoMessage("Update Successfully In Database");
        }
        
       
    }
    
    
    private boolean checkDuplicate(String id,Integer type) {
        String qu;
        if(type==1)
            qu="SELECT * FROM category WHERE CategoryID="+Integer.valueOf(id);
        else
             qu="SELECT CategoryName FROM category WHERE CategoryID="+id;
        ResultSet rs=dbCon.execQuery(qu);
        try {
            if(rs.next()){
                return true;
            }
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
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
    
}