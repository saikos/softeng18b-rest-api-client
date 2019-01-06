package gr.ntua.ece.softeng18b.client

import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.ProductList
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.model.ShopList
import gr.ntua.ece.softeng18b.client.rest.LowLevelAPI
import gr.ntua.ece.softeng18b.client.rest.RestCallFormat
import gr.ntua.ece.softeng18b.client.rest.RestResponseHandler

class RestAPI {
    
    private final LowLevelAPI api
    private String token = null //user is not logged in
    
    RestAPI(String host, int port, boolean secure) {
        api = new LowLevelAPI(host, port, secure)
    }
    
    void login(String username, String password, RestCallFormat format) {
        
        String token = api.login(username, password, format).getToken()
                                    
        if (token) {
            this.token = token
        }
        else {
            throw new RuntimeException("No token in response")
        }
    }
    
    boolean isLoggedIn() {
        return (this.token != null)
    }
    
    void logout(RestCallFormat format) {
        
        String message = api.logout(token, format).getMessage()
                        
        if ('OK'.equals(message)) {
            this.token = null
        }
        else {
            throw new RuntimeException("Empty or invalid message")
        }
    }
    
    ProductList getProducts(int start, int count, String status, String sort, RestCallFormat format) {                        
        return api.getProducts(token, start, count, status, sort, format).getProductList()        
    }
    
    Product postProduct(Product product, RestCallFormat format) {        
        return api.postProduct(token, product, format).getProduct()                
    }
    
    Product putProduct(String id, Product product, RestCallFormat format){
        return api.putProduct(token, id, product, format).getProduct()
    }
    
    Product patchProduct(String id, String field, def value, RestCallFormat format) {
        return api.patchProduct(token, id, field, value, format).getProduct()
    }
    
    void deleteProduct(String id, RestCallFormat format) {
        String message = api.deleteProduct(token, id, format).getMessage()
                        
        if (! 'OK'.equals(message)) {
            throw new RuntimeException("Deletion failed")
        }
    }    
    
    ShopList getShops(int start, int count, String status, String sort, RestCallFormat format) {
        return api.getShops(token, start, count, status, sort, format).getShopList()
    }
    
    Shop postShop(Shop shop, RestCallFormat format) {
        return api.postShop(token, shop, format).getShop()
    }
    
    Shop putShop(String id, Shop shop, RestCallFormat format) {
        return api.putShop(token, id, shop, format).getShop()
    }
    
    Shop patchShop(String id, String field, def value, RestCallFormat format) {
        return api.patchShop(token, id, field, value, format).getShop()
    }
    
    void deleteShop(String id, RestCallFormat format) {
        String message = api.deleteShop(token, id, format).getMessage()
                        
        if (! 'OK'.equals(message)) {
            throw new RuntimeException("Deletion failed")
        }
    }
    
}