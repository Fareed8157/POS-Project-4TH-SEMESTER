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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Fareed
 */
public class Product {
    private final SimpleStringProperty barCode;
    private final SimpleStringProperty name;
    private final SimpleStringProperty desc;
    private final SimpleStringProperty selling;
    private final SimpleStringProperty unitPrice;
    private final SimpleStringProperty stockOut;
    private final SimpleObjectProperty<ProductTracking> pt;
    private final SimpleObjectProperty<Supplier> supp;
    private final SimpleObjectProperty<Date> stockInDate;
    private final SimpleObjectProperty<ImageView> imv;

    public Product(String barCode, String name, String desc, String selling, String unitPrice, String stockOut, ProductTracking pt, Supplier supp, Image imv,Date stockInDate) {
        this.barCode = new SimpleStringProperty(barCode);
        this.name =new SimpleStringProperty( name);
        this.desc =new SimpleStringProperty( desc);
        this.selling =new SimpleStringProperty( selling);
        this.unitPrice =new SimpleStringProperty( unitPrice);
        this.stockOut =new SimpleStringProperty( stockOut);
        this.pt=new SimpleObjectProperty(pt);
        this.supp =new SimpleObjectProperty( supp);
        this.imv =new SimpleObjectProperty(imv);
        this.stockInDate=new SimpleObjectProperty(stockInDate);
    }
    //bar code getter setter
    public String getBarCode(){
        return this.barCode.get();
    }
    public void setBarCode(String barCode){
        this.barCode.set(barCode);
    }
    public StringProperty barCodeProperty(){
        return this.barCode;
    }
    //name getter and setter
    public String getName(){
        return this.name.get();
    }
    public void setName(String name){
        this.name.set(name);
    }
    public StringProperty nameProperty(){
        return this.name;
    }
    
    //description setter and getter
    
    public String getDesc(){
        return this.desc.get();
    }
    public void setDesc(String desc){
        this.desc.set(desc);
    }
    public StringProperty descProperty(){
        return this.desc;
    }
    
    //selling getter and Setter
    
    public String getSelling(){
        return this.selling.get();
    }
    public void setSelling(String selling){
        this.selling.set(selling);
    }
    public StringProperty sellingProperty(){
        return this.selling;
    }
    
    //Unit price getter and setter
    
    public String getUnitPrice(){
        return this.unitPrice.get();
    }
    public void setUnitPrice(String unitPrice){
        this.unitPrice.set(unitPrice);
    }
    public StringProperty unitPriceProperty(){
        return this.unitPrice;
    }
    
    //stockOut getter and Setter
    
    public String getStockOut(){
        return this.stockOut.get();
    }
    public void setStockOut(String stockOut){
        this.stockOut.set(stockOut);
    }
    public StringProperty stockOutProperty(){
        return this.stockOut;
    }
    
    //product Tracking getter and Setter
    
    public ProductTracking getPt(){
        return this.pt.get();
    }
    public void setPt(ProductTracking pt){
        this.pt.set(pt);
    }
    public ObjectProperty<ProductTracking> ptProperty(){
        return this.pt;
    }
    
    //Supplier getter and setter
    
    public Supplier getSupp(){
        return this.supp.get();
    }
    public void setSupp(Supplier supp){
        this.supp.set(supp);
    }
    public ObjectProperty<Supplier> suppProperty(){
        return this.supp;
    }
    
    //image getter and Setter
    public ImageView getImv(){
        return this.imv.get();
    }
    public void setImv(ImageView imv){
        this.imv.set(imv);
    }
    public ObjectProperty<ImageView> imvProperty(){
        return this.imv;
    }
    
    //date setter and getter
    
    public Date getStockInDate(){
        return this.stockInDate.get();
    }
    public void setStockInDate(Date imv){
        this.stockInDate.set(imv);
    }
    public ObjectProperty<Date> stockInDateProperty(){
        return this.stockInDate;
    }
    
}
