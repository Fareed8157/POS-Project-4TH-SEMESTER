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
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    ArrayList<Integer> oldStockOut=new ArrayList<Integer>();
    ArrayList<Integer> oldAvail=new ArrayList<Integer>();
    ArrayList<Integer> arr;
    Integer oldProSum=0;
    Integer newProSum=0;
    Integer subSum=null;
    JFXTextField am;
    JFXComboBox<String> delOrPrint;
    String userName="";
    Integer grandSum=0;
    Integer recieved=0;
    Integer returned=0;
    Integer rcvd=0;
    Integer retnd=0;
    Integer nv1=0;
    Integer  n =0;
    Integer newSubAndGrandSum=0;
    Integer totalOfProductPrice=0;
    public static Integer invoiceNo;
    SimpleLongProperty invNo=null;
    SimpleLongProperty tItem=null;
    SimpleLongProperty subTotal=null;
    SimpleLongProperty grandTotal=null;
    JFXButton printIn=null;
    ObservableList<Sale> tableItems=FXCollections.observableArrayList();
    ObservableList<String> chooseTypeList=FXCollections.observableArrayList();
    ObservableList<Sale> invoiceList=FXCollections.observableArrayList();
    ObservableList<Sale> sList=FXCollections.observableArrayList();
    ObservableList<Sale> saleList=FXCollections.observableArrayList();
    ObservableList<Sale> selectedRows,allProducts;
    private List<Sale> items;
    private List<Sale> invoiceDelList;
    Configs con;
    Stage win=null;
    public posController() {
    invNo=new SimpleLongProperty(0);
    tItem=new SimpleLongProperty(0);
    grandTotal=new SimpleLongProperty(0);
    subTotal=new SimpleLongProperty(0);
    System.out.println("Subtotal=="+subTotal.get());
    this.arr = new ArrayList<Integer>();
    }
    @FXML
    void closeStage(MouseEvent event) {
       Stage stage=(Stage)close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAction(ActionEvent event) throws SQLException, IOException {
        if(event.getSource()==printInvoice){
            if(chooseType.getValue()!="Edit Invoice"){
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
            printIn=new JFXButton("Print");
            printIn.setOnAction(e->{
                System.out.println("Inside SetOn");
            if(!(am.getText().isEmpty())){
                if(amountValidation()){
                System.out.println("Inside SetOn");
                    String qu="";
                    recieved=Integer.valueOf(am.getText());
                    int in=(int)invNo.get();
                    returned=(int) (long)grandTotal.get();
                    if(recieved>=returned){
                    if(saveInvoiceDetails()){
                    HashMap<String,Object> hm=new HashMap<>();
                    hm.put("invoiceId",in);
                    hm.put("cashier",userName);
                    hm.put("rcvd",recieved);
                    hm.put("returned",returned);
                    PrintReport pr=new PrintReport(qu,hm);
                    pr.showReport("invoice");
                    infoMessage("Printed");
                    win.close();
                    saveStockOutIn();
                    oldProSum=0;
                    for(Sale s: posTable.getItems()){
                     oldProSum+=Integer.valueOf(s.getQt());
                    }
                    tItem.set(0);
                    subTotal.set(0);
                    grandTotal.set(0);
                    saleList.clear();
                    invoiceNo=getNextInvoiceId();
                    int temp=++invoiceNo;
                    invNo.set(temp);
                        }
                   }else{
                     errorMessage("Amount Entered Is less Than Grand Total");
                    }
                }//end of inner if
                
             }else{
                errorMessage("Fill the Field");
            }
            });
            printIn.setStyle("-fx-border-color: #ff0000; -fx-border-color: #B3E5FC; -fx-border-width: 5px; -fx-font-size: 1em;");
            printIn.setPrefWidth(150);
            printIn.setPrefHeight(40);
            GridPane.setConstraints(printIn, 1, 2);
            grid.getChildren().addAll(amount,am,printIn);
            Scene sc=new Scene(grid,400,300);
            win=new Stage();
            win.setScene(sc);
            win.showAndWait();  
            }//end of inner most if
           else
               errorMessage("No Products");   
        }else{                              //when we edit invoice
            if(!(invoiceList.isEmpty())){
            Label amount=new Label("Amount");
            amount.setStyle("-fx-font-size: 1em; ");
            am=new JFXTextField();
            newProSum=0;
            for(Sale s: posTable.getItems()){
            newProSum+=Integer.valueOf(s.getQt());
            }
            if(newProSum<oldProSum)
             am.setPromptText("Enter The Amount To be Returned");  
            else
            am.setPromptText("Amount to Pay");
            am.setLabelFloat(true);
            GridPane grid=new GridPane();
            grid.setPadding(new Insets(40,40,40,40));
            grid.setVgap(16);
            grid.setHgap(20);
            GridPane.setConstraints(amount, 0, 0);
            GridPane.setConstraints(am, 1, 0);
            printIn=new JFXButton("Print");
            printIn.setOnAction(e->{
          if(!(am.getText().isEmpty())){
            if(amountValidation()){
                String qu="";
          recieved=Integer.valueOf(am.getText().trim());
          changeInDbForExistingInvoice();
          int in=(int)invNo.get();
          HashMap<String,Object> hm=new HashMap<>();
          hm.put("invoiceId",in);
          if(newProSum<oldProSum){
           hm.put("rcvd",rcvd);
          hm.put("returned",retnd);   
          }else{
          hm.put("rcvd",recieved);
          hm.put("returned",returned);    
          }
          hm.put("cashier",userName);
          PrintReport pr=new PrintReport(qu,hm);
          pr.showReport("invoice");
          infoMessage("Printed");
          win.close();
          saveStockOutIn();
          tItem.set(0);
          subTotal.set(0);
          grandTotal.set(0);
         invoiceList.clear();   
            }//end of inner if
     
          }//end of outer if
        });
            printIn.setStyle("-fx-border-color: #ff0000; -fx-border-color: #B3E5FC; -fx-border-width: 5px; -fx-font-size: 1em;");
            //printIn.setStyle("-fx-font-size: 1em; ");
            printIn.setPrefWidth(150);
            printIn.setPrefHeight(40);
            GridPane.setConstraints(printIn, 1, 2);
        grid.getChildren().addAll(amount,am,printIn);
        Scene sc=new Scene(grid,400,300);
        Stage win=new Stage();
        win.setScene(sc);
        win.showAndWait();
        }//end of outer most if
        else
            errorMessage("No Products");   
        }//end of outer else
      }//end of print button
        else if(event.getSource()==deletItem){
          if(chooseType.getValue()!="Edit Invoice"){
          allProducts=posTable.getItems();
          items =  new ArrayList (posTable.getSelectionModel().getSelectedItems());
          Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
          a1.setTitle("Confirmation Dialog");
          a1.setContentText("Are Sure Want to Delete ?");
          a1.setHeaderText(null);
          Optional<ButtonType> action=a1.showAndWait();
          if(action.get()==ButtonType.OK){
            for(Sale sa: items){
            subSum=subSum-sa.getUnitPrice();
            grandSum=grandSum-sa.getUnitPrice();
            subTotal.set(subSum);
            grandTotal.set(subSum);
            posTable.getItems().remove(sa);
            }
            tItem.set(saleList.size());
            }
          }
          else{
              System.out.println("In del invoice");
              deleteProductsFromInvoice();
          }    
          
        }else if(event.getSource()==clear){
            tItem.set(0);
            subTotal.set(0);
            grandTotal.set(0);
            saleList.clear();
            Integer invoiceValueCheck=getNextInvoiceId();
            invoiceValueCheck++;
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
         }else{
            closeButton.getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con=Configs.getInstance();
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
                subTotal.set(0);
                grandTotal.set(0);
                tItem.set(0);
                invNo.set(0);
                search.clear();
                
            }else{
                invoiceList.clear();
                invNo.set(temp);
                subTotal.set(0);
                grandTotal.set(0);
                tItem.set(0);
                reprintInvoice.setDisable(false);
                clear.setDisable(false);
                search.setPromptText("Barcode");
                search.clear();
                invoiceNo=getNextInvoiceId();
                invNo.set(temp);
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
                        nv1=Integer.valueOf(t.getNewValue());
                    }else{
                        changeDetailsOfProduct(t.getNewValue().trim());
                    }
                    sp.setQt(t.getNewValue());
                        //updateCellValue(t,"1");
                   });
        search.textProperty().addListener((observable,oldVal,newVal)->{
            if(chooseType.getValue()=="Edit Invoice"){
                if(newVal.isEmpty()){
                invoiceList.clear();
                subTotal.set(0);
                grandTotal.set(0);
                tItem.set(0);
                invNo.set(Integer.valueOf(0));
                }else{
                invNo.set(Integer.valueOf(newVal));
                System.out.println("inv="+newVal);
                findInvoice(newVal);
                oldProSum=0;
                for(Sale s: posTable.getItems()){
                oldProSum+=Integer.valueOf(s.getQt());
                }
                }
                
            }else{
                findProduct(newVal);
            }
            
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
                System.out.println("up=="+up);
               sale=new Sale(bc,pname,pd,so,up);
               sList.add(sale);
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

  private boolean saveInvoiceDetails() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Integer uId=getUid();
        returned=(int) (long)grandTotal.get();
        returned-=recieved;
        String qu="INSERT INTO invoice(InvoiceID,invoiceDateTime,uId,rcvdAmount,returnedAmount) VALUES("+invNo.get()+",'"+dateFormat.format(date)+"',"+uId+","+recieved+","+returned+")";
        if(con.execAction(qu)){
             if(saveDetailsIntoInvoiceProduct(invNo.get()))
                return true;
            System.out.println("Saved");
        }
        return false;
    }

    private Integer getUid() {
        String sCurrentLine="";
        Integer val=null;
        String FILENAME="G:\\UserDetails.txt";
        BufferedReader br = null;
	FileReader fr = null;
        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
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
                    Sale sal=posTable.getSelectionModel().getSelectedItem();
                    Integer upForChange=sal.getUnitPrice();
                    Integer oldQt=Integer.valueOf(sal.getQt());
                    Integer newQt=Integer.valueOf(newValue.trim());
                    newQt-=oldQt;
                    Integer sumTemp=(newQt)*upForChange;
                    subSum+=sumTemp;
                    subTotal.set(subSum);
                    grandTotal.set(subSum);
                    
        }

    private Integer getNextInvoiceId() {
        String qu="SELECT max(InvoiceID) as id from invoice";
        Integer lastId=null;
        ResultSet rs=con.execQuery(qu);
         try {
            
            if (rs.next())
                lastId=rs.getInt("id");
             System.out.println(lastId);
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
        pr.showReport("invoice");
    }

    private void findInvoice(String inv) {
        invoiceList.clear();
        System.out.println("inv="+inv);
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
        System.out.println(qu);
        Integer pCode=0;
        String pname="";
        String pDesc="";
        Integer up=0;
        Integer qty=0;
        Integer tItm=0;
        Integer gSum=0;
        int i=0;
        try {
            while(rs.next()){
                System.out.println("Size of i="+i);
                i++;
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
                System.out.println("Size of list="+invoiceList.size());
                System.out.println("Inside Whil");
                System.out.println("Inside Whil=="+gSum);
            }
             System.out.println("Out Whil=="+gSum);
            subTotal.set(gSum);
            grandTotal.set(gSum);
            tItem.set(tItm);
            posTable.setItems(invoiceList);
            
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        
    }

  
    private void changeDetailsOfExistingProduct(Sale sp,String nv) {
        Sale sal=posTable.getSelectionModel().getSelectedItem();
        String qu="UPDATE productinvoice SET Quantity="+Integer.valueOf(nv)
                + " where InvoiceID="+invNo.get()+" and BarCode="+Integer.valueOf(sp.getBarCode());
        if(con.execAction(qu)){
        Integer oldQt=Integer.valueOf(sal.getQt());
        Integer newQt=Integer.valueOf(nv);
        newQt-=oldQt;
        if(newQt<0){
        newQt=-newQt;
        newSubAndGrandSum=newQt*sp.getUnitPrice();
        System.out.println("newSubAndGrandSum="+newSubAndGrandSum);
        subTotal.set(subTotal.get()-newSubAndGrandSum);
        grandTotal.set(grandTotal.get()-newSubAndGrandSum);    
        }else{ 
        newSubAndGrandSum=newQt*sp.getUnitPrice();
        subTotal.set(subTotal.get()+newSubAndGrandSum);
        grandTotal.set(grandTotal.get()+newSubAndGrandSum);  
        }
        arr.add(newSubAndGrandSum);
        }
    }
    
    private void changeInDbForExistingInvoice() {
        for(Integer i :arr){
            totalOfProductPrice+=i;
        }
        String qu1="";
        rcvd=getRcvdAmount();
        System.out.println("rcvd="+rcvd);
        retnd=getRetndAmount();
        if(newProSum<oldProSum){
            System.out.println("rcvd="+rcvd);
            System.out.println("rtnd="+retnd);
        retnd+=(-totalOfProductPrice);
        qu1="UPDATE invoice SET rcvdAmount="+rcvd+" ,returnedAmount="+retnd+" Where InvoiceID="+invNo.get();
        }else{
        retnd-=(recieved-totalOfProductPrice);
        recieved+=rcvd;
        returned=retnd;
        qu1="UPDATE invoice SET rcvdAmount="+recieved+" ,returnedAmount="+retnd+" Where InvoiceID="+invNo.get();
        }
        con.execAction(qu1);
        totalOfProductPrice=0;
        arr.clear();
    }

    private Integer getRcvdAmount() {
        Integer rcd=null;
        try {
            String qu="SELECT rcvdAmount FROM invoice WHERE InvoiceID="+invNo.get();
            ResultSet rs=con.execQuery(qu);
            rs.next();
            rcd=rs.getInt("rcvdAmount");
            return rcd;
        } catch (SQLException ex) {
            Logger.getLogger(posController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rcd;
    }

    private Integer getRetndAmount() {
        Integer rtnd=null;
        try {
            String qu="SELECT returnedAmount FROM invoice WHERE InvoiceID="+invNo.get();
            ResultSet rs=con.execQuery(qu);
            rs.next();
            rtnd=rs.getInt("returnedAmount");
            return rtnd;
        } catch (SQLException ex) {
            Logger.getLogger(posController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rtnd;
    }

    private void saveStockOutIn() {
        ArrayList<Integer> a2=new ArrayList<Integer>();
        ArrayList<Integer> a3=new ArrayList<Integer>();
        for(Sale l: posTable.getItems()){
            try {
                String qu="SELECT availPro,stockOut from products where BarCode="+Integer.valueOf(l.getBarCode());
                ResultSet rs=con.execQuery(qu);
                rs.next();
                Integer t=rs.getInt("availPro");
                Integer t1=rs.getInt("stockOut");
                a2.add(t);         //available products
                a3.add(t1);         //stockOut
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        changeInStockInAndStockOut(a2,a3);
    }

    private void changeInStockInAndStockOut(ArrayList<Integer> a2, ArrayList<Integer> a3) {
        int i=0;
        String qu="";
        for(Sale sl: posTable.getItems()){
            if(newProSum<oldProSum){
             qu="UPDATE products set stockOut="+(a3.get(i)-Integer.valueOf(sl.getQt()))+",availPro="+(a2.get(i)+Integer.valueOf(sl.getQt()))+" where BarCode="+Integer.valueOf(sl.getBarCode());
            }else{
             qu="UPDATE products set stockOut="+(a3.get(i)+Integer.valueOf(sl.getQt()))+",availPro="+(a2.get(i)-Integer.valueOf(sl.getQt()))+" where BarCode="+Integer.valueOf(sl.getBarCode());
            }
           con.execAction(qu);
           i++;
        }
    }

     public void refreshInvoice(){
        String inv=search.getText();
       invoiceList.clear();
        System.out.println("inv="+inv);
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
        System.out.println(qu);
        Integer pCode=null;
        String pname="";
        String pDesc="";
        Integer up=0;
        Integer qty=0;
        Integer tItm=0;
        Integer gSum=0;
        int i=0;
        try {
            while(rs.next()){
                System.out.println("Size of i="+i);
                i++;
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
                System.out.println("Size of list="+invoiceList.size());
                System.out.println("Inside Whil");
                System.out.println("Inside Whil=="+gSum);
            }
             System.out.println("Out Whil=="+gSum);
            subTotal.set(gSum);
            grandTotal.set(gSum);
            tItem.set(tItm);
            posTable.setItems(invoiceList);
            
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
         
    }
     public void loadOldStkAvail(){
        ArrayList<Integer> a2=new ArrayList<Integer>();
        ArrayList<Integer> a3=new ArrayList<Integer>();
        for(Sale l: posTable.getItems()){
            try {
                String qu="SELECT availPro,stockOut from products where BarCode="+Integer.valueOf(l.getBarCode());
                ResultSet rs=con.execQuery(qu);
                rs.next();
                Integer t=rs.getInt("availPro");
                Integer t1=rs.getInt("stockOut");
                a2.add(t);         //available products
                a3.add(t1);         //stockOut
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        oldStockOut=a3;
        oldAvail=a2;
     }
    
    private void deleteProductsFromInvoice() {
        System.out.println("Inside delproMethd");
        Integer sumOfDeletePro=0;
        String qu="";
        int i=0;
          invoiceDelList =  new ArrayList (posTable.getSelectionModel().getSelectedItems());
          Alert a1= new Alert(Alert.AlertType.CONFIRMATION);
          a1.setTitle("Confirmation Dialog");
          a1.setContentText("Are Sure Want to Delete ?");
          a1.setHeaderText(null);
          loadOldStkAvail();
          Optional<ButtonType> action=a1.showAndWait();
          if(action.get()==ButtonType.OK){
              System.out.println("Size of posTable="+invoiceDelList.size());
            for(Sale sa: invoiceDelList){
            sumOfDeletePro+=Integer.valueOf(sa.getQt())*sa.getUnitPrice();
            qu="UPDATE products set stockOut="+(oldStockOut.get(i)-Integer.valueOf(sa.getQt()))+",availPro="+(oldAvail.get(i)+Integer.valueOf(sa.getQt()))+" where BarCode="+Integer.valueOf(sa.getBarCode());
            con.execAction(qu);
            qu="DELETE from productinvoice where InvoiceID="+invNo.get()+" AND BarCode="+Integer.valueOf(sa.getBarCode());
            con.execAction(qu);
            subSum=subSum-sa.getUnitPrice();
            grandSum=grandSum-sa.getUnitPrice();
            subTotal.set(subSum);
            grandTotal.set(grandSum);
            posTable.getItems().remove(sa);
            i++;
            }
            tItem.set(invoiceList.size());
            Integer rdAmount=getRetndAmount();
            System.out.println("SumOfDel="+sumOfDeletePro);
            System.out.println("rdAmount="+rdAmount);
            rdAmount+=(-sumOfDeletePro);
            qu="UPDATE invoice set returnedAmount="+rdAmount+" WHERE InvoiceID="+invNo.get();
            con.execAction(qu);
            refreshInvoice();
            oldStockOut.clear();
            oldAvail.clear();
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
                    System.out.println(textField.getText()+"="+newValue);
                    System.out.println("CreatTextField Method");
                }
                
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
    private boolean amountValidation() {
        Pattern p=Pattern.compile("[0-9]+");
        Matcher m=p.matcher(am.getText());
        if(m.find() && m.group().equals(am.getText()))
            return true;
        else{
            Alert a1= new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Validate Amount");
            a1.setContentText("Please Enter Valid Amount");
            a1.setHeaderText(null);
            a1.showAndWait();
            return false;
        }
    }

}
