/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Category;
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
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

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
    private MaterialDesignIconView close;
    
    ObservableList<Supplier> filteredData=FXCollections.observableArrayList();
    public ProductSuppController(){
       list.addListener(new ListChangeListener<Supplier>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Supplier> change) {
                
                updateFilteredData();
                System.out.println("In Constructor");
            }
        });
    }
    Configs con;
    ObservableList<Supplier> list=FXCollections.observableArrayList();
    
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
            refreshTable();
        }

    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update.setDisable(true);
        suppTable.setEditable(true);
        suppTable.focusedProperty().addListener((a,b,c) -> {
        suppTable.getSelectionModel().clearSelection();
        });
        con=Configs.getInstance();
        loadData();
        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory
                = (TableColumn<Supplier, String> param) -> new EditingCell();
//        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFname
//                = (TableColumn<Person, String> param) -> new EditingFname();
//        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellLname
//                = (TableColumn<Person, String> param) -> new EditingLname();
//        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellEmail
//                = (TableColumn<Person, String> param) -> new EditingEmail();
//        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellPhno
//                = (TableColumn<Person, String> param) -> new EditingPhno();
        sId.setCellValueFactory(cellData->cellData.getValue().sidProperty());
        sId.setCellFactory(cellFactory);
        sId.setOnEditCommit(
                (TableColumn.CellEditEvent<Supplier, String> t) -> {
                    System.out.println(t.getNewValue());
                    Supplier sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                    updateCellValue(sp,t.getNewValue());
                    
//                    ((Supplier) t.getTableView().getItems()
//                    .get(t.getTablePosition().getRow()))
//                    .setName(t.getNewValue());
                });
        sFname.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        sLname.setCellValueFactory(cellData->cellData.getValue().lnameProperty());
        sEmail.setCellValueFactory(cellData->cellData.getValue().emailProperty());
        sPhno.setCellValueFactory(cellData->cellData.getValue().phnoProperty());
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
                        event.consume();  
                    }
                    else if(!row.isSelected()){
                         update.setDisable(false);
                        delete.setDisable(false);
                    }
                    
                }  
            });  
            return row;  
        }  
    }); 
        suppTable.getSelectionModel().selectedItemProperty().addListener(((observable,oldVal,newVal)-> {
            update.setDisable(false);
            delete.setDisable(false);
            
        }));
        
        //sId.setOnEditCommit(e->setEditableCell1(e));
//        sId.setCellFactory(TextFieldTableCell.forTableColumn());
//        sFname.setOnEditCommit(e->setEditableCell2(e));
//        sFname.setCellFactory(TextFieldTableCell.forTableColumn());
//        sEmail.setOnEditCommit(e->setEditableCell3(e));
//        sEmail.setCellFactory(TextFieldTableCell.forTableColumn());
//        sLname.setOnEditCommit(e->setEditableCell3(e));
//        sLname.setCellFactory(TextFieldTableCell.forTableColumn());
//        sPhno.setOnEditCommit(e->setEditableCell3(e));
//        sPhno.setCellFactory(TextFieldTableCell.forTableColumn());
        search.textProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                updateFilteredData();
            }
        });
        
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
            String qu="INSERT INTO supplier VALUES(?,?,?,?,?)";
            PreparedStatement pst=con.execQueryPrep(qu);
            if(!checkDuplicate(suId)){
                try {
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
    
    
    private boolean checkDuplicate(String id) {
        String qu="SELECT SupplierID FROM supplier WHERE SupplierID="+Integer.valueOf(id);
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

    private void updateCellValue(Supplier sp,String newValue) {
        System.out.println("Inside UpdateCell Method");
//        String qu="UPDATE supplier SET SupplierID='"+newValue+
//                "',sname='"+sp.getName()+
//                "',phone='"+sp.getPhno()+
//                "',email='"+sp.getEmail()+
//                "',lname="+sp.getLname()+"'";
        
        String qu="UPDATE supplier SET SupplierID='"+newValue+"' WHERE SupplierID='"+sp.getSid()+"';";
        if(!checkDuplicate(newValue)){
            if(con.execAction(qu)){
                sp.setSid(newValue);
                infoMessage("Updated");
                refreshTable();
            }
        }
        else{
            errorMessage("Duplicate Value Not Allowed");
            refreshTable();
        }
    }

    
    
    class EditingCell extends TableCell<Supplier, String> {

        private JFXTextField textField;

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
            textField = new JFXTextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnAction((e) -> commitEdit(textField.getText()));
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    System.out.println("Commiting " + textField.getText());
                    if(!checkDuplicate(newValue))
                    commitEdit(textField.getText());
                }
                else{
                    System.out.println("CreatTextField Method");
                }
                System.out.println(textField.getText());
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
    
}
