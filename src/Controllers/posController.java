/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Sale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Fareed
 */
public class posController implements Initializable{

    
    @FXML
    private JFXButton clear;
    
    @FXML
    private Label subTotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label grandTotalLab;

    @FXML
    private Label itemNoLabel;

    @FXML
    private JFXButton printInvoice;

    @FXML
    private JFXButton reprintInvoice;
    
    @FXML
    private JFXButton pay;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton deletItem;

    @FXML
    private Label invoiceNoLabel;

    @FXML
    private TableView<Sale> posTable;

    @FXML
    private TableColumn<Sale,String> id;

    @FXML
    private TableColumn<Sale,String> name;

    @FXML
    private TableColumn<Sale,String> desc;

    @FXML
    private TableColumn<Sale,String> qt;

    @FXML
    private TableColumn<Sale,Number> price;

    @FXML
    private JFXTextField search;

    @FXML
    private FontAwesomeIconView close;
    
    Integer subSum=null;
    JFXTextField am;
    
    
    Integer grandSum=null;
    Integer taxSum=null;
    Integer  n =null;
    public static Integer invoiceNo;
//    SimpleIntegerProperty invNo=new SimpleIntegerProperty();
//    SimpleIntegerProperty tItem=new SimpleIntegerProperty();
//    SimpleIntegerProperty subTotal=new SimpleIntegerProperty();
//    SimpleIntegerProperty tax=new SimpleIntegerProperty();
//    SimpleIntegerProperty grandTotal=new SimpleIntegerProperty();
    SimpleLongProperty invNo=new SimpleLongProperty();
    SimpleLongProperty tItem=new SimpleLongProperty();
    SimpleLongProperty subTotal=new SimpleLongProperty();
    SimpleLongProperty tax=new SimpleLongProperty();
    SimpleLongProperty grandTotal=new SimpleLongProperty();
    Random rand = new Random();
    
    ObservableList<Sale> tableItems=FXCollections.observableArrayList();
    ObservableList<Sale> sList=FXCollections.observableArrayList();
    ObservableList<Sale> saleList=FXCollections.observableArrayList();
    ObservableList<Sale> selectedRows,allProducts;
    private List<Sale> items;
    Configs con;
    @FXML
    void closeStage(MouseEvent event) {
       Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAction(ActionEvent event) {
        if(event.getSource()==printInvoice){
            if(saveInvoiceDetails()){
                infoMessage("Printed");
            }
            
        }else if(event.getSource()==deletItem){
            allProducts=posTable.getItems();
            items =  new ArrayList (posTable.getSelectionModel().getSelectedItems());
            System.out.println("size befre delt=="+saleList.size());
            Sale sa=posTable.getSelectionModel().getSelectedItem();
            
            
            Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
            a1.setTitle("Confirmation Dialog");
            a1.setContentText("Are Sure Want to Delete ?");
            a1.setHeaderText(null);
            Optional<ButtonType> action=a1.showAndWait();
            //win.initModality(Modality.WINDOW_MODAL);
            if(action.get()==ButtonType.OK){
                subTotal.set((subSum-sa.getUnitPrice()));
                grandTotal.set((grandSum-sa.getUnitPrice()));
                tax.set(taxSum-n);
                
                posTable.getItems().remove(sa);
                tItem.set(saleList.size());
                System.out.println("size after delt=="+saleList.size());
            }
        }else if(event.getSource()==clear){
            tItem.set(0);
            tax.set(0);
            subTotal.set(0);
            grandTotal.set(0);
            saleList.clear();
            Integer invoiceValueCheck=getNextInvoiceId();
            invNo.set(invoiceValueCheck);
            if(invoiceValueCheck==null){
                errorMessage("Enter Products into Cart");
            }
        }else if(event.getSource()==reprintInvoice){
            Label amount=new Label("Amount");
            amount.setStyle("-fx-font-size: 1em; ");
            am=new JFXTextField();
            am.setPromptText("Write Amount to Pay");
            GridPane grid=new GridPane();
            grid.setPadding(new Insets(40,40,40,40));
            grid.setVgap(16);
            grid.setHgap(20);
            GridPane.setConstraints(amount, 0, 0);
            GridPane.setConstraints(am, 1, 0);
            JFXButton printIn=new JFXButton("Print");
            printIn.setStyle("-fx-border-color: #ff0000; -fx-border-color: #B3E5FC; -fx-border-width: 5px; -fx-font-size: 1em;");
            //printIn.setStyle("-fx-font-size: 1em; ");
            printIn.setPrefWidth(150);
            printIn.setPrefHeight(40);
            //printIn.getGraphic().setStyle("-fx-background-color: #B3E5FC;");
            GridPane.setConstraints(printIn, 1, 2);
            printIn.setOnAction(e->{
                System.out.println("Printed");
            });
            grid.getChildren().addAll(amount,am,printIn);
            Scene sc=new Scene(grid,400,300);
            
            Stage win=new Stage();
            win.setScene(sc);
            win.showAndWait();
            
            
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //invoiceNoLabel.setText(invoiceNo.toString());
        subSum=new Integer(0);
        grandSum=new Integer(0);
        taxSum=new Integer(0);
        con=Configs.getInstance();
        posTable.setEditable(true);
        invoiceNo=getNextInvoiceId();
        
        loadData();
        invoiceNoLabel.textProperty().bind(invNo.asString());
        itemNoLabel.textProperty().bind(tItem.asString());
        subTotalLabel.textProperty().bind(subTotal.asString());
        taxLabel.textProperty().bind(tax.asString());
        grandTotalLab.textProperty().bind(grandTotal.asString());
        posTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //invNo.set(invoiceNo);
        int temp=++invoiceNo;
        invNo.set(temp);
        System.out.println("temp=="+invNo.get());
        Callback<TableColumn<Sale, String>, TableCell<Sale, String>> cellFactory
                = (TableColumn<Sale, String> param) -> new EditingCell();
        id.setCellValueFactory(cellData->cellData.getValue().barCodeProperty());
        name.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        desc.setCellValueFactory(cellData->cellData.getValue().descProperty());
        price.setCellValueFactory(cellData->cellData.getValue().unitPriceProperty());
        qt.setCellValueFactory(cellData->cellData.getValue().qtProperty());
        qt.setCellFactory(cellFactory);
        qt.setOnEditCommit(
                (TableColumn.CellEditEvent<Sale, String> t) -> {
                    System.out.println(t.getNewValue());
                    Sale sp=t.getTableView().getItems().get(t.getTablePosition().getRow());
                    changeDetailsOfProduct(t.getNewValue());
                    sp.setQt(t.getNewValue());
                        //updateCellValue(t,"1");
                   });
        search.textProperty().addListener((observable,oldVal,newVal)->{
            findProduct(newVal.toString());
        });
        
    }

    private void loadData() {
        String qu="SELECT * FROM products";
        Sale sale=null;
        
        ResultSet rs=con.execQuery(qu);
        try {
            while(rs.next()){
               String bc=rs.getString("BarCode");
               String pname=rs.getString("ProductName");
               String pd=rs.getString("PDescription");
               Integer up=rs.getInt("unitPrice");
               String so="1";
               sale=new Sale(bc,pname,pd,up,so);
               sList.add(sale);
               //posTable.setItems(sList);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

    }

    private void findProduct(String bcode) {
        n = rand.nextInt(10) + 1;
         ObservableList<Sale> sl=FXCollections.observableArrayList();
           for(Sale s : sList){
               if((s.getBarCode().equals(bcode))){
                   if(!(checkInDateFirst(bcode))){
                   sl.add(s);
                   subSum+=s.getUnitPrice();
                   grandSum+=s.getUnitPrice()+n;
                   taxSum+=n;
                   tax.set(taxSum);
                   subTotal.set(subSum);
                   grandTotal.set(grandSum);
                   }
                   
               }
           }
          // System.out.println("In sl=="+sl.get(0).getQt());
           saleList.addAll(sl);
           tItem.set(saleList.size());
           posTable.setItems(saleList);
           //System.out.println("At sale list=="+saleList.get(0).getQt());
    }

//    private void removeRecords(List<Sale> sa) {
//        for(int i=0; i<sa.size(); i++){
//            saleList.remove()
//            if(con.execAction(qu)){
//                System.out.println("Executed");
//             }
//        }
//        allProducts.removeAll(sa);
//    }
    private boolean saveInvoiceDetails() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Integer uId=getUid();
        //System.out.println(dateFormat.format(date));
        //int a=invoiceNo++;
        //invNo.set( a);
        String qu="INSERT INTO invoice(InvoiceID,invoiceDateTime,uId) VALUES("+invNo.get()+",'"+dateFormat.format(date)+"',"+uId+")";
        if(con.execAction(qu)){
            //infoMessage("Invoice Generated");
            if(saveDetailsIntoInvoiceProduct(invNo.get()))
                return true;
            System.out.println("Saved");
        }
        return false;
    }

    private Integer getUid() {
        String sCurrentLine="";
        Integer val=null;
       // String[] ar=null;
        String FILENAME="G:\\UserDetails.txt";
        BufferedReader br = null;
	FileReader fr = null;
        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
            //String value=br.readLine();
            //ar=(sCurrentLine = br.readLine()).split("/");
            String[] ar=(sCurrentLine = br.readLine()).split("/");
            val=Integer.valueOf(ar[0]);
            System.out.println("My nO"+val);
            return val;
           
        } catch (FileNotFoundException ex) {
           System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return val;
    }
    
    private boolean saveDetailsIntoInvoiceProduct(long get) {
        String qu="";
        boolean flag=false;
        for(Sale s: saleList){
            qu="INSERT INTO productinvoice(Quantity,InvoiceID,BarCode)VALUES("+s.getQt()+","+get+","+s.getBarCode()+")";
            if(con.execAction(qu))
                flag=true;
        }
        return flag;
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

    private boolean checkInDateFirst(String bcode) {
        tableItems=posTable.getItems();
        System.out.println("TableSize"+tableItems.size());
        if(tableItems.size()!=0){
            for(Sale s: tableItems){
                if(bcode.equals(s.getBarCode())){
                    return true;
                }
            }
        }
        return false;
    }

    private void changeDetailsOfProduct(String newValue) {
        System.out.println("newvalue=="+newValue);
                    taxSum=Integer.valueOf(newValue)*taxSum;
                    subSum=Integer.valueOf(newValue)*subSum;
                    grandSum=Integer.valueOf(newValue)*grandSum;
                    System.out.println("Mult::"+taxSum+"="+subSum+"="+grandSum);
                    tax.set(taxSum);
                    subTotal.set(subSum);
                    grandTotal.set(grandSum);
                    System.out.println("after Setting :"+tax.get()+"="+subTotal.get()+"="+grandTotal.get());
    }

    private Integer getNextInvoiceId() {
        String qu="SELECT max(InvoiceID) as id from invoice";
        Integer lastId=null;
        ResultSet rs=con.execQuery(qu);
         try {
            
              if (rs.next())
                lastId=rs.getInt("id");
             System.out.println(lastId);
            //invNo.set(lastId);
            return lastId++;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
             return lastId;
        }
    }

    
    class EditingCell extends TableCell<Sale, String> {

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
