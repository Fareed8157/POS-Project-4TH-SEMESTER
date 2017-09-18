/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DBConnection.Configs;
import MainPack.Sale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
    private JFXComboBox<String> chooseType;
    
    @FXML
    private JFXButton clear;
    
    @FXML
    private Label subTotalLabel;

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
    JFXComboBox<String> delOrPrint;
    String userName="";
    Integer grandSum=null;
    Integer recieved=null;
    Long returned=null;
    Integer rcvd=null;
    Integer retnd=null;
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
   
    SimpleLongProperty grandTotal=new SimpleLongProperty();
    
    JFXButton printIn;
    ObservableList<Sale> tableItems=FXCollections.observableArrayList();
    ObservableList<String> chooseTypeList=FXCollections.observableArrayList();
    ObservableList<Sale> invoiceList=FXCollections.observableArrayList();
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
    void onAction(ActionEvent event) throws SQLException, IOException {
        if(event.getSource()==printInvoice){
           if(!(saleList.isEmpty())){
            Label amount=new Label("Amount");
            amount.setStyle("-fx-font-size: 1em; ");
            am=new JFXTextField();
            am.setPromptText("Amount to Pay");
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
            if(!(am.getText().isEmpty())){
                    String qu="";
                    String un=userName;
                    int in=(int)invNo.get();
                    if(saveInvoiceDetails()){
                    HashMap<String,Object> hm=new HashMap<>();
                    System.out.println("invoiceID="+in);
                    System.out.println("UserName="+un);
                    hm.put("invoiceId",in);
                    hm.put("cashier",un);
                    hm.put("rcvd",recieved);
                    hm.put("returned",returned);
                    PrintReport pr=new PrintReport(qu,hm);
                    pr.showReport();
                    infoMessage("Printed");
                }
            }
            });
            grid.getChildren().addAll(amount,am,printIn);
            Scene sc=new Scene(grid,400,300);
            Stage win=new Stage();
            win.setScene(sc);
            win.showAndWait();
            }//end of inner most if
           else
               errorMessage("No Products");
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
                posTable.getItems().remove(sa);
                tItem.set(saleList.size());
                System.out.println("size after delt=="+saleList.size());
            }
        }else if(event.getSource()==clear){
            tItem.set(0);
            subTotal.set(0);
            grandTotal.set(0);
            saleList.clear();
            Integer invoiceValueCheck=getNextInvoiceId();
            invNo.set(invoiceValueCheck);
            if(invoiceValueCheck==null){
                errorMessage("Enter Products into Cart");
            }
        }else if(event.getSource()==reprintInvoice){
         Stage win=new Stage();
         Scene sc;
         Parent root=FXMLLoader.load(getClass().getResource("/FXML/RePrintInvoiceAndDel.fxml"));           
            sc=new Scene(root);
            win.setScene(sc);
            win.show();
//            String mydate="2017/09/15";
//            Date today = new Date(mydate);
//            SimpleDateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd");
//            try{
//                System.out.println(tsdf.format(today));
//            }
//            catch (Exception e){
//                System.out.println("Error occurred" + e.getMessage());
//            }          
         }else{
            closeButton.getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
        //invoiceNoLabel.setText(invoiceNo.toString());
        chooseTypeList.addAll("Edit Invoice","New Transaction");
        chooseType.getItems().addAll(chooseTypeList);
        chooseType.setValue("New Transaction");
        invoiceNo=getNextInvoiceId();
        
        int temp=++invoiceNo;
        invNo.set(temp);
        System.out.println("temp=="+invNo.get());
        chooseType.setOnAction(e->{
            if(chooseType.getValue()=="Edit Invoice"){
                reprintInvoice.setDisable(true);
                clear.setDisable(true);
                search.setPromptText("Enter Invoice No");
                saleList.clear();
                invNo.set(0);
            }else{
                invoiceList.clear();
                invNo.set(temp);
                reprintInvoice.setDisable(false);
                clear.setDisable(false);
                search.setPromptText("Barcode");
            }
        });
        subSum=new Integer(0);
        grandSum=new Integer(0);
        posTable.setEditable(true);
        loadData();
        invoiceNoLabel.textProperty().bind(invNo.asString());
        itemNoLabel.textProperty().bind(tItem.asString());
        subTotalLabel.textProperty().bind(subTotal.asString());
       
        grandTotalLab.textProperty().bind(grandTotal.asString());
        posTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //invNo.set(invoiceNo);
        
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
                    if(chooseType.getValue()=="Edit Invoice"){
                        changeDetailsOfExistingProduct(sp,t.getNewValue());
                    }else{
                        changeDetailsOfProduct(t.getNewValue());
                    }
                    
                    sp.setQt(t.getNewValue());
                        //updateCellValue(t,"1");
                   });
        search.textProperty().addListener((observable,oldVal,newVal)->{
            if(chooseType.getValue()=="Edit Invoice"){
                invNo.set(Integer.valueOf(newVal.toString()));
                findInvoice(newVal.toString());
            }else
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
               sale=new Sale(bc,pname,pd,so,up);
               sList.add(sale);
               //posTable.setItems(sList);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

    }

    private void findProduct(String bcode) {
         ObservableList<Sale> sl=FXCollections.observableArrayList();
           for(Sale s : sList){
               if((s.getBarCode().equals(bcode))){
                   if(!(checkInDateFirst(bcode))){
                   sl.add(s);
                   subSum+=s.getUnitPrice();
                   grandSum+=s.getUnitPrice();
                   subTotal.set(subSum);
                   grandTotal.set(grandSum);
                   }
               }
           }
           saleList.addAll(sl);
           tItem.set(saleList.size());
           posTable.setItems(saleList);
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
        returned=grandTotal.get();
        returned-=recieved;
        String qu="INSERT INTO invoice(InvoiceID,invoiceDateTime,uId,rcvdAmount,returnedAmount) VALUES("+invNo.get()+",'"+dateFormat.format(date)+"',"+uId+","+recieved+","+returned+")";
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
            userName=ar[1];
            System.out.println(ar[1]);
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
                    
                    subSum=Integer.valueOf(newValue)*subSum;
                    grandSum=Integer.valueOf(newValue)*grandSum;
                    subTotal.set(subSum);
                    grandTotal.set(grandSum);
                    
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

    private void rePrintInvoice(String invId) {
        String qu="";
        String un="";
        Integer rc=null;
        Integer ret=null;
        String qu1="SELECT rcvdAmount,returnedAmount FROM invoice WHERE InvoiceID="+Integer.valueOf(invId);
        ResultSet rs=con.execQuery(qu1);
        try {
            if(rs.next()){
              rc=rs.getInt(1);
              ret=rs.getInt(2);
                System.out.println("rcd="+rc+",ret="+ret);
            }
         qu1="SELECT u.uName FROM user u inner join invoice i on(u.uId=i.uId) where i.InvoiceID="+Integer.valueOf(invId);
         rs.close();
         rs=con.execQuery(qu1);
         if(rs.next()){
              un=rs.getString(1);
            }
         rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        HashMap<String,Object> hm=new HashMap<>();
        hm.put("invoiceId",Integer.valueOf(invId));
        hm.put("cashier",un);
        hm.put("rcvd",rc);
        hm.put("returned",ret);
        PrintReport pr=new PrintReport(qu,hm);
        pr.showReport();
    }

    private void findInvoice(String inv) {
        invoiceList.clear();
        String qu="select p.BarCode, p.ProductName,p.PDescription,pro.Quantity,p.unitPrice,(select sum(pro.Quantity) from productinvoice pro inner join invoice i \n" +
        "on(i.InvoiceID=pro.InvoiceID) where i.InvoiceID="+Integer.valueOf(inv.trim())+") as totalItem,i.InvoiceID id,sum(( select sum(pro.Quantity*p.unitPrice) from user u\n" +
        " inner join invoice i on(u.uId=i.uId) \n" +
        "inner join productinvoice pro on(i.InvoiceID=pro.InvoiceID) inner join products p\n" +
        "on(pro.BarCode=p.BarCode) \n" +
        "group by i.InvoiceID\n" +
        "having i.InvoiceID="+Integer.valueOf(inv.trim())+")) grandSum,i.rcvdAmount,i.returnedAmount\n" +
        "from invoice i inner join productinvoice pro on(i.InvoiceID=pro.InvoiceID)\n" +
        "inner join products p on(pro.BarCode=p.BarCode)\n" +
        "group by i.InvoiceID,p.BarCode\n" +
        "having id="+Integer.valueOf(inv.trim());
        ResultSet rs=con.execQuery(qu);
        Integer pCode=null;
        String pname="";
        String pDesc="";
        Integer up=null;
        Integer qty=null;
        Integer tItm=null;
        Integer gSum=null;
        try {
            while(rs.next()){
               
                pCode=rs.getInt(1);
                pname=rs.getString(2);
                pDesc=rs.getString(3);
                qty=rs.getInt(4);
                up=rs.getInt(5);
                tItm=rs.getInt(6);
                gSum=rs.getInt(8);
                rcvd=rs.getInt(9);
                retnd=rs.getInt(10);
                Sale sal=new Sale(pCode.toString(),pname,pDesc,qty.toString(),up);
                invoiceList.add(sal);
            }
            subTotal.set(gSum);
            grandTotal.set(gSum);
            tItem.set(tItm);
            posTable.setItems(invoiceList);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        
    }

    private void changeDetailsOfExistingProduct(Sale sp,String nv) {
        String qu="UPDATE productinvoice SET Quantity="+Integer.valueOf(nv)
                + "where InvoiceID="+invNo.get()+" and BarCode="+Integer.valueOf(sp.getBarCode());
        if(con.execAction(qu)){
            Long oldGtotal=grandTotal.get();
            Long sbTotal=Long.valueOf(nv)*sp.getUnitPrice();
            Long newGtotal=oldGtotal;
            newGtotal+=sbTotal;
            if(newGtotal<=rcvd){
                retnd=(int)((long)sbTotal)+retnd;
            }else{
                
            }
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
