package gr.ntua.ece.softeng18b.client.rest

import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.ProductList
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.model.ShopList

class XmlRestCallResult implements RestCallResult {

    final def xml
    
    XmlRestCallResult(def xml) {
        this.xml = xml
    }
    
    void writeTo(Writer w) {
        
    }
    
    String getToken() { 
        null
    }
    
    String getMessage() { 
        null
    }
    
    ProductList getProductList() { 
        null
    }
    
    Product getProduct() { 
        null
    }
    
    ShopList getShopList() { 
        null
    }
    
    Shop getShop() { 
        null
    }
    
}

