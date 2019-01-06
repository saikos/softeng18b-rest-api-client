package gr.ntua.ece.softeng18b.client.rest

import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.model.ProductList
import gr.ntua.ece.softeng18b.client.model.ShopList

class JsonRestCallResult implements RestCallResult {

    private final def json     
    
    JsonRestCallResult(def json) {
        this.json = json
    }
    
    void writeTo(Writer w) {
        
    }
    
    String getToken() {
        return json['token'] as String
    }
    
    String getMessage() {
        return json['message'] as String
    }
        
    ProductList getProductList() {        
        int start    = json['start'] as Integer
        int count    = json['count'] as Integer
        long total   = json['total'] as Long        
        def products = json['products']
        List<Product> productList = products.collect { p ->
            parseProduct(p)
        }

        return new ProductList(
            start   : start,
            count   : count,
            total   : total,
            products: productList
        )
    }
    
    Product getProduct() {
        return parseProduct(json)
    }

    protected Product parseProduct(p) {
        return new Product(
            id         : p['id'] as String,
            name       : p['name'] as String,
            description: p['description'] as String,
            category   : p['category'] as String,
            tags       : p['tags'] as List,
            withdrawn  : p['withdrawn'] as Boolean
        )
    }

    ShopList getShopList() {
        int start  = json['start'] as Integer
        int count  = json['count'] as Integer
        long total = json['total'] as Long        
        def shops  = json['products']
        List<Shop> shopList = shops.collect { s ->
            parseShop(s)
        }

        return new ShopList(
            start : start,
            count : count,
            total : total,
            shops : shopList
        )
    }
    
    protected Shop parseShop(s) {
        return new Shop(
            id        : s['id'] as String,
            name      : s['name'] as String,
            address   : s['address'] as String,
            lat       : s['lat'] as double,
            lng       : s['lng'] as double,            
            tags      : s['tags'] as List,
            withdrawn : s['withdrawn'] as Boolean
        )
    }

    Shop getShop() {
        return parseShop(json)
    }
    
}

