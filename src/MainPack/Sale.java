/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class Sale {
    private  SimpleStringProperty barCode;
    private  SimpleStringProperty name;
    private  SimpleStringProperty desc;
    private  SimpleIntegerProperty unitPrice;
    private  SimpleStringProperty qt;

    public Sale(String barCode, String name, String desc,String qt,Integer unitPrice) {
        this.barCode = new SimpleStringProperty(barCode);
        this.name =new SimpleStringProperty( name);
        this.desc =new SimpleStringProperty( desc);
        this.unitPrice =new SimpleIntegerProperty( unitPrice);
        this.qt =new SimpleStringProperty( qt);
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
    
    //Unit price getter and setter
    
    public Integer getUnitPrice(){
        return this.unitPrice.get();
    }
    public void setUnitPrice(Integer unitPrice){
        this.unitPrice.set(unitPrice);
    }
    public IntegerProperty unitPriceProperty(){
        return this.unitPrice;
    }
    
    
    public String getQt(){
        return this.qt.get();
    }
    public void setQt(String qt){
        this.qt.set(qt);
    }
    public StringProperty qtProperty(){
        return this.qt;
    }
}
