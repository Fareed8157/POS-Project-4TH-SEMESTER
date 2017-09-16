/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Supplier;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Fareed
 */
public class ProductSuppController implements Initializable{

     @FXML
    private TableView<Supplier> suppTable;

    @FXML
    private TableColumn<Supplier,String> sId;
       
    @FXML
    private TableColumn<Supplier,String> sFname;

    @FXML
    private TableColumn<Supplier,String> sLname;

    @FXML
    private TableColumn<Supplier,String> sEmail;

    @FXML
    private TableColumn<Supplier,String> sPhno;

    @FXML
    private JFXTextField id;
     
    @FXML
    private JFXTextField fname;

    @FXML
    private JFXTextField lname;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField phno;

    @FXML
    private JFXButton add;

    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton update;

    @FXML
    private JFXTextField search;

    @FXML
    private FontAwesomeIconView searcIcon;

    @FXML
    private List<Supplier> items;
    private MaterialDesignIconView close;
    ObservableList<Supplier> selectedRows,allSupplier;
    ObservableList<Supplier> filteredData=FXCollections.observableArrayList();
    Configs con;
    ObservableList<Supplier> list=FXCollections.observableArrayList();
    public ProductSuppController(){
       list.addListener(new ListChangeListener<Supplier>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Supplier> change) {
                
                updateFilteredData();
                System.out.println("In Constructor");
            }
        });
    }
    
    @FXML
    void closeStage(MouseEvent event) {
        Stage win=(Stage)close.getScene().getWindow();
        win.close();
    }

    @FXML
    void searchIconMethod(MouseEvent event) {
        search.requestFocus();
    }
   
    @FXML
    void onAction(ActionEvent event) {
        if(event.getSource()==add){
            if(saveData()){
            clearFields();
            infoMessage("Sucessfully Added");
            refreshTable();    
            }
            
        }
        else if(event.getSource()==delete){
            ObservableList<Supplier> selectedRows,allCategories;
            allSupplier=suppTable.getItems();
            //selectedRows=suppTable.getSelectionModel().getSelectedItems();
            items =  new ArrayList (suppTable.getSelectionModel().getSelectedItems());
            String id=suppTable.getSelectionModel().getSelectedItem().getSid();
            Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
            a1.setTitle("Confirmation Dialog");
            a1.setContentText("Are Sure Want to Delete ?");
            a1.setHeaderText(null);
            Optional<ButtonType> action=a1.showAndWait();
            //win.initModality(Modality.WINDOW_MODAL);
            if(action.get()==ButtonType.OK){
            removeRecords(items);    
            }
        }else if(event.getSource()==update){
            Supplier sid=suppTable.getSelectionModel().getSelectedItem();
            if(updateData()){
                refreshTable();    
            infoMessage("Updated");
            }
           }

    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update.setDisable(true);
        delete.setDisable(true);
        suppTable.setEditable(true);
//        suppTable.focusedProperty().addListener((a,b,c) -> {
//        suppTable.getSelectionModel().clearSelection();
//        });
        
        suppTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        con=Configs.getInstance();
        loadData();
        
        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory
                = (TableColumn<Supplier, String> param) -> new EditingCell();
        sId.setCellValueFactory(cellData->cellData.getValue().sidProperty());
        sId.setCellFactory(cellFactory);
        sId.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                    
                        updateCellValue(t,"1");
                   });
        sFname.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        sFname.setCellFactory(cellFactory);
        sFname.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                        updateCellValue(t,"2");
                   });
        sLname.setCellValueFactory(cellData->cellData.getValue().lnameProperty());
        sLname.setCellFactory(cellFactory);
        sLname.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                    //if(t.getNewValue()!=t.getOldValue())
                        updateCellValue(t,"3");
                        //refreshTable();
                   });
        sEmail.setCellValueFactory(cellData->cellData.getValue().emailProperty());
        sEmail.setCellFactory(cellFactory);
        sEmail.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if(t.getNewValue()!=t.getOldValue())
                        updateCellValue(t,"4");
                   });
        sPhno.setCellValueFactory(cellData->cellData.getValue().phnoProperty());
        sPhno.setCellFactory(cellFactory);
        sPhno.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                        updateCellValue(t,"5");
                   });
        suppTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    Supplier sp=suppTable.getSelectionModel().getSelectedItem();
                        if(sp!=null){
                        id.setText(sp.getSid());
                        fname.setText(sp.getName());
                        lname.setText(sp.getLname());
                        email.setText(sp.getEmail());
                        phno.setText(sp.getPhno());
                        }
                        
                }
            }
        });
        
        suppTable.setRowFactory(new Callback<TableView<Supplier>, TableRow<Supplier>>() {  
        @Override  
        public TableRow<Supplier> call(TableView<Supplier> tableView2) {  
            final TableRow<Supplier> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                @Override  
                public void handle(MouseEvent event) {  
                    final int index = row.getIndex();
                    System.out.println(index);
                    if (index >= 0 && index < suppTable.getItems().size() && suppTable.getSelectionModel().isSelected(index)  ) {
                        suppTable.getSelectionModel().clearSelection();
                       update.setDisable(true);
                        delete.setDisable(true);
                        id.setText("");
                        fname.setText("");
                        lname.setText("");
                        email.setText("");
                        phno.setText("");
                        event.consume();  
                    }
                    else if(!row.isSelected()){
                        Supplier sp=suppTable.getSelectionModel().getSelectedItem();
                        if(sp!=null){
                         update.setDisable(false);
                        delete.setDisable(false);
                    }
                      
                    }
                }  
            });  
            return row;  
        }  
    }); 
    
        search.textProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                updateFilteredData();
            }
        });
        suppTable.getSelectionModel().selectedItemProperty().addListener(((observable,oldVal,newVal)-> {
            Supplier sp=suppTable.getSelectionModel().getSelectedItem();
         update.setDisable(false);
            delete.setDisable(false);
            
        }));
    }

    private void updateFilteredData() {
        filteredData.clear();

        for(Supplier p : list) {
            if (matchesFilter(p)) {
                filteredData.add(p);
            }
        }

        // Must re-sort table after items changed
        //reapplyTableSortOrder();
    }
    private boolean matchesFilter(Supplier ct) {
        String filterString = search.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }
        String lowerCaseFilterString = filterString.toLowerCase();

        if (ct.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (ct.getSid().toString().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }
    
    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<Supplier, ?>> sortOrder = new ArrayList<>(suppTable.getSortOrder());
        suppTable.getSortOrder().clear();
        suppTable.getSortOrder().addAll(sortOrder);
    }
    
    private void removeRecords(List<Supplier> supp) {
        for(int i=0; i<supp.size(); i++){
            String qu="DELETE FROM supplier where SupplierID="+supp.get(i).getSid();
            if(con.execAction(qu)){
                System.out.println("Executed");
             }
        }
        allSupplier.removeAll(supp);
        refreshTable();
    }
    private boolean saveData() {
        String suId=id.getText();
        String fnam=fname.getText();
        String lnam=lname.getText();
        String em=email.getText();
        String phn=phno.getText();
        if(suId.isEmpty() || fnam.isEmpty() || lnam.isEmpty() || em.isEmpty() || phn.isEmpty()){
            errorMessage("Complete All Fields");
            return false;
        }   
        else{
            Supplier sp1=new Supplier(suId,fnam,lnam,em,phn);
            String qu="INSERT INTO supplier VALUES(?,?,?,?,?)";
            
            if(!checkDuplicateForFields(sp1)){
                try {
                    System.out.println("Inside ADD METHOD");
                PreparedStatement pst=con.execQueryPrep(qu);
                pst.setString(1, suId);
                pst.setString(2, fnam);
                pst.setString(5, lnam);
                pst.setString(4, em);
                pst.setString(3, phn);
                list.add(new Supplier(suId,fnam,lnam,em,phn));
                if(pst.executeUpdate()!=0)
                    return true;
                else
                    return false;
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                return false;
            }//end catch 
            }//end of inner if
            else{
                errorMessage("Duplicate Values");
                return false;
            } //end of inner else
        } //end of outer else
      }
    
    
    private boolean checkDuplicateForFields(Supplier sp) {
        String qu="";
        qu="SELECT SupplierID,email,phone FROM supplier WHERE SupplierID='"+sp.getSid()+"' OR email='"+sp.getEmail()+"' OR phone='"+sp.getPhno()+"'";
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
    
    
    private void refreshTable() {
        list.clear();
         filteredData.clear();
        String qu="SELECT * FROM supplier";
            ResultSet rs=con.execQuery(qu);
         try {
             while(rs.next()){
                 Supplier sp=new Supplier(rs.getString("SupplierID"),rs.getString("sname"),rs.getString("lname"),rs.getString("email"),rs.getString("phone"));
                 list.add(sp);
                 suppTable.setItems(filteredData) ;
             }
         } catch (SQLException ex) {
             System.out.println(ex.toString());
         }
    }
    
    private void loadData() {
        list.clear();
        String qu="SELECT * FROM supplier";
            ResultSet rs=con.execQuery(qu);
         try {
             while(rs.next()){
                 Supplier sp=new Supplier(rs.getString("SupplierID"),rs.getString("sname"),rs.getString("lname"),rs.getString("email"),rs.getString("phone"));
                 list.add(sp);
                 suppTable.setItems(filteredData);
             }
             //suppTable.setItems(list);
             suppTable.setItems(filteredData);
         } catch (SQLException ex) {
             System.out.println(ex.toString());
         }
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
    void clearFields(){
        id.clear();
        fname.clear();
        lname.clear();
        email.clear();
        phno.clear();
    }
    
    private void updateCellValue(TableColumn.CellEditEvent<Supplier, String> t,String type) {
        Supplier sp=t.getRowValue();
        String old=t.getOldValue();
        if(type=="1"){
        boolean flag=checkDuplicateForCells(t,"1");
        String qu="UPDATE supplier SET SupplierID='"+t.getNewValue()+"' WHERE SupplierID='"+sp.getSid()+"';";
        if(!flag){
            if(con.execAction(qu)){
                sp.setSid(t.getNewValue());
                if(t.getOldValue()!=t.getNewValue()){
                    return;
                }
                infoMessage("Updated");
                refreshTable();
            }
        }
        if(flag){
           refreshTable();
           return; 
            //errorMessage("Duplicate");
        }
        }else if(type=="2"){
            String qu="UPDATE supplier SET sname='"+t.getNewValue()+"' WHERE SupplierID='"+sp.getSid()+"';";
            if(con.execAction(qu)){
                sp.setName(t.getNewValue());
                refreshTable();
                infoMessage("Updated");
                return;
            }
        }else if(type=="3"){
            String qu="UPDATE supplier SET lname='"+t.getNewValue()+"' WHERE SupplierID='"+sp.getSid()+"';";
            if(con.execAction(qu)){
                sp.setLname(t.getNewValue());
                
                if(t.getNewValue()!=t.getOldValue()){
                    
                return;
                }  
                infoMessage("Updated");
                refreshTable();
            }
            return;
        }
        else if(type=="4"){
            String qu="UPDATE supplier SET email='"+t.getNewValue()+"' WHERE SupplierID='"+sp.getSid()+"';";
            boolean flag=checkDuplicateForCells(t,"4");
        if(!flag){
            if(con.execAction(qu)){
                sp.setEmail(t.getNewValue());
                if(t.getOldValue()!=t.getNewValue()){
                    return;
                }
                infoMessage("Updated");
                refreshTable();
            }
        }
        if(flag){
            refreshTable();
            return;     
        }
        }
        else if(type=="5"){
            boolean flag=checkDuplicateForCells(t,"5");
        String qu="UPDATE supplier SET Phone='"+t.getNewValue()+"' WHERE SupplierID='"+sp.getSid()+"';";
        if(!flag){
            if(con.execAction(qu)){
                sp.setPhno(t.getNewValue());
                if(t.getOldValue()!=t.getNewValue()){
                    return;
                }
                infoMessage("Updated");
                refreshTable();
            }
        }
        if(flag){
            refreshTable();
            return;      
        }
        }
        
    }

    private boolean checkDuplicateForCells(TableColumn.CellEditEvent<Supplier, String> t,String type) {
        String qu="",colName="";
        //qu="SELECT SupplierID,email,phone FROM supplier WHERE SupplierID='"+sp.getSid()+"' AND email='"+sp.getEmail()+"' phone='"+sp.getPhno()+"';";
        if(type=="1"){
            if(t.getNewValue()!=t.getOldValue()){
                qu="SELECT SupplierID FROM supplier WHERE SupplierID!='"+t.getOldValue()+"';";
                System.out.println(t.getOldValue()+"--"+t.getNewValue());
                colName="SupplierID";
            }
        }
            
        else if(type=="4"){
            if(t.getNewValue()!=t.getOldValue()){
                qu=qu="SELECT email FROM supplier WHERE email!='"+t.getOldValue()+"';";
                colName="email";
            }
        }
            
        else if(type=="5")
            if(t.getNewValue()!=t.getOldValue()){
                qu="SELECT phone FROM supplier WHERE phone!='"+t.getOldValue()+"';";
                colName="phone";
            }
        try {
            ResultSet rs=con.execQuery(qu);
            while(rs.next()){
                System.out.println(" In while ifAfter while=="+rs.getString(colName)+"--"+t.getNewValue());
                if(rs.getString(colName).equals(t.getNewValue().toString())){
                    
                    return true;
                }
            }
            System.out.println("After while=="+t.getOldValue()+"--"+t.getNewValue());    
            return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
    
    
    private boolean updateData() {
        String suId=id.getText();
        String fnam=fname.getText();
        String lnam=lname.getText();
        String em=email.getText();
        String phn=phno.getText();
        Supplier sp=suppTable.getSelectionModel().getSelectedItem();
        if(suId.isEmpty() || fnam.isEmpty() || lnam.isEmpty() || em.isEmpty() || phn.isEmpty()){
            errorMessage("Complete All Fields");
            return false;
        }   
        else{
            
            System.out.println(phn+","+sp.getPhno());
            boolean flag=(!checkDuplicateForUpdate(suId,"1") && !checkDuplicateForUpdate(em,"2") && !checkDuplicateForUpdate(phn,"3") );
            //boolean flag1=(!checkDuplicateForUpdate(phn,"3"));
           System.out.println("Flag=="+flag);
            Supplier sp1=new Supplier(suId,fnam,lnam,em,phn);
            String qu="UPDATE supplier SET supplierID='"+suId+"', sname='"+fnam+"' ,phone='"+phn+"' ,email='"+em+"', lname='"+lnam+
                    "' WHERE SupplierID='"+suId+"';";
            System.out.println("phone=="+phn);
            if(flag){
//                if(!checkDuplicateForUpdate(phn,"3")){
//                    System.out.println("Inside if of phn");
//                }
                System.out.println("Inside ADD METHOD"); //end catch
                list.add(new Supplier(suId,fnam,lnam,em,phn));
                if(con.execAction(qu)){
                    System.out.println("inside add exection if");
                    return true;
                }
                    
                else
                    return false;
            }//end of inner if
            else{
                errorMessage("Duplicate Values");
                return false;
            } //end of inner else
        } //end of outer else
    }

    private boolean checkDuplicateForUpdate(String sp,String type) {
        String qu = null;//="SELECT SupplierID,email,phone FROM supplier WHERE email='"+sp.getEmail()+"' OR phone='"+sp.getPhno()+"' AND SupplierID!='"+sp.getSid()+"';";
        String cname=""; 
        Supplier sp3=suppTable.getSelectionModel().getSelectedItem();
         System.out.println("id=="+sp3.getSid()+"=="+sp);
         System.out.println("id=="+sp3.getEmail()+"=="+sp);
         System.out.println("id=="+sp3.getPhno()+"=="+sp);
        if(type=="1"){
            if(sp3.getSid()!=sp){
                qu="SELECT SupplierID FROM supplier WHERE SupplierID!='"+sp3.getSid()+"'";
                cname="SupplierID";
            }
            System.out.println("Check ID");
            
        }
            
        else if(type=="2" && sp3.getEmail()!=sp){
            System.out.println("CHEKC email");
            if(sp3.getEmail()!=sp){
                qu="SELECT email FROM supplier WHERE email!='"+sp3.getEmail()+"'";
                cname="email";
            }
        }
            
        else if(type=="3" && sp3.getPhno()!=sp ){
            System.out.println("CHECK PHNO");
            if(sp3.getPhno()!=sp)
            qu="SELECT phone FROM supplier WHERE phone!='"+sp3.getPhno()+"'";
            cname="phone";
        }
            
        try {
            ResultSet rs=con.execQuery(qu);
            while(rs.next()){
                if(sp.equals(rs.getString(cname))){
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    private void updatFields() {
        Supplier sid=suppTable.getSelectionModel().getSelectedItem();
        String qu="SELECT * FROM supplier WHERE SupplierID='"+sid.getSid()+"'";
        ResultSet rs=con.execQuery(qu);
         try {
             while(rs.next()){
             id.setText(sid.getSid());
             fname.setText(sid.getName());
             lname.setText(sid.getLname());
             email.setText(sid.getEmail());
             phno.setText(sid.getPhno());    
             }} catch (SQLException ex) {
             Logger.getLogger(ProductSuppController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    
    
    class EditingCell extends TableCell<Supplier, String> {

        private TextField textField;

        private EditingCell() {
            
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                System.out.println("Start Edit");
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
            else{
            System.out.println("start edit else");
        }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(item);
                System.out.println("Inside Update Item=="+item);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
//                        setGraphic(null);
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//            textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent e) {
//                    if(checkDuplicate(textField.getText())){
//                    commitEdit(textField.getText());
//                    errorMessage("Duplication Not Allowed");
//                    }
//                }
//            });
            textField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    
                    commitEdit(textField.getText());
//                    if(checkDuplicate(textField.getText()))
//                    errorMessage("Duplication Not Allowed");
                }
            });
            
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                
                if (!newValue) {
                    System.out.println("Commiting " + textField.getText());
                    
                    commitEdit(textField.getText());
                    System.out.println(textField.getText()+"==="+newValue);
                }
                
                else{
                    //errorMessage("Duplication Not Allowed");
                    System.out.println(textField.getText()+"="+newValue);
                    System.out.println("CreatTextField Method");
                }
                
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
    
}
