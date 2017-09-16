/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Fareed
 */
public class Category extends RecursiveTreeObject<Category>{
    private  SimpleIntegerProperty serialNo;
    private  SimpleIntegerProperty id;
    private  SimpleStringProperty name;
    private  SimpleStringProperty desc;
    private  ImageView imv;
    private  Image im;
    public Category(){
        
    }
    public Category(Integer id){
        this.id = new SimpleIntegerProperty(id);
    }
    
    public Category(Integer id,String name){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }
    
    public Category(Integer serialNo,Integer id, String name, String desc, Image imv) {
        this.serialNo=new SimpleIntegerProperty(serialNo);
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.desc = new SimpleStringProperty(desc);
        this.imv = new ImageView(imv);
        this.im=imv;
    }
    //getters
    public Integer getSerialNo() {
        return serialNo.get();
    }
    public Integer getId() {
        return id.get();
    }
    
    public Image getImage(){
        return this.im;
    }
    
    public String getName() {
        return name.get();
    }

    public String getDesc() {
        return desc.get();
    }

    public ImageView getImv() {
        return imv;
    }
    
    //setters

    public void setSerialNo(Integer serialNo) {
        this.serialNo.set(serialNo);
    }
    
    public void setId(Integer id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public void setImv(ImageView imv) {
        this.imv = imv;
    }
    
    //properties
    public IntegerProperty serialNoProperty(){
        return serialNo;
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty nameProperty(){
        return name;
    }
    
    public StringProperty descProperty(){
        return desc;
    }
    
}
