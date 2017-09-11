/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class ProductTracking {
    private  SimpleStringProperty stockIn;
    private  SimpleStringProperty barcode;
    private  SimpleObjectProperty<Date> upDate;

    public ProductTracking(String stockIn, String barcode, Date upDate) {
        this.stockIn = new SimpleStringProperty(stockIn);
        this.barcode = new SimpleStringProperty(barcode);
        this.upDate = new SimpleObjectProperty(upDate);
    }
    //stockIn getter setter
    public String getStockIn() {
        return stockIn.get();
    }

    public StringProperty stockInProperty(){
        return this.stockIn;
    }
    
    public void setStockIn(String stockIn) {
        this.stockIn.set(stockIn);
    }
    
    //barcode setter and geteer
    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }
    
    public StringProperty barcodeProperty(){
        return this.barcode;
    }
    
    
    //date getter and setter
    
    public Date getUpDate() {
        return upDate.get();
    }
   
    public void setUpDate(Date upDate) {
        this.upDate.set(upDate);
    }
    
    public ObjectProperty<Date> upDateProperty(){
        return this.upDate;
    }
    
    
    
    
}
