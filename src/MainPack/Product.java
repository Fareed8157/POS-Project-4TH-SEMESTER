/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPack;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fareed
 */
public class Product {
    StringProperty barCode;
    StringProperty name;
    StringProperty desc;
    StringProperty selling;
    StringProperty unitPrice;
    StringProperty stockOut;
    ObjectProperty<ProductTracking> pr;
    ObjectProperty<Supplier> supp;
    ObjectProperty<ImageView> supp;
    
}
