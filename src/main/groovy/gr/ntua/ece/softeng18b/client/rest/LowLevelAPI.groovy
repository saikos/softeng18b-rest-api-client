package gr.ntua.ece.softeng18b.client.rest

import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Response
import org.apache.http.client.fluent.Form

import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.ProductList
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.model.ShopList

class LowLevelAPI {
    
    static final def SORT_OPTIONS = [
        "id|ASC",
        "id|DESC",
        "name|ASC",
        "name|DESC"
    ]
    
    static final def STATUSES = [
        "ACTIVE",
        "WITHDRAWN",
        "ALL"
    ]
    
    static final String DEFAULT_SORT   = SORT_OPTIONS[0]    
    static final String DEFAULT_STATUS = STATUSES[0]
	
    static final String BASE_PATH = "/observatory/api"
    static final String HEADER    = "X-OBSERVATORY-AUTH"
    
    private final String host
    private final int port
    private final boolean secure       
    
    LowLevelAPI(String host, int port, boolean secure) {
        this.host   = host
        this.port   = port
        this.secure = secure
    }
    
    private String createUrl(String endPoint, RestCallFormat format, Map params = null ) {
        Map queryParams
        if (format == RestCallFormat.JSON) {
            queryParams = [:]
        }
        else {
            pueryParms = [format: format.getName()]
        }        
        if (params) queryParams.putAll(params)
        String queryString
        if  (queryParams) {
            queryString = "?" + encode(params)
        }
        else {
            queryString = ""
        }             
        String url = "${secure ? 'https' : 'http'}://$host:$port$BASE_PATH/$endPoint$queryString"        
        //println url
        return url
    }              
    
    RestCallResult login(String username, String password, RestCallFormat format) {
        
        return Request.
                Post(createUrl("login", format)).
                bodyForm(
                    Form.form().
                        add("username", username).
                        add("password", password).
                        build()
                    ).
                execute().
                handleResponse(new RestResponseHandler(format))
    }    
    
    RestCallResult logout(String token, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        return Request.
                Post(createUrl("logout", format)).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult getProduct(String token, String id, RestCallFormat format) {
        
        def req = Request.Get(createUrl("products/$id", format))
        
        if (token) req.addHeader(HEADER, token)
        
        return req.execute().handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult getProducts(String token, int start, int count, String status, String sort, RestCallFormat format) {                  
        
        def req = Request.Get(createUrl("products", format, [
            start : start,
            count : count,
            status: status,
            sort  : sort
        ]))
                    
        if (token) req.addHeader(HEADER, token)            
        
        return req.execute().handleResponse(new RestResponseHandler(format))        
    }
    
    RestCallResult postProduct(String token, Product product, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addToForm(form, product)
        
        return Request.
                Post(createUrl("products", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
                
    }
    
    RestCallResult putProduct(String token, String id, Product product, RestCallFormat format){
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addToForm(form, product)
        
        return Request.
                Put(createUrl("products/$id", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult patchProduct(String token, String id, String field, def value, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addFieldToForm(form, field, value)
        
        return Request.
                Patch(createUrl("products/$id", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult deleteProduct(String token, String id, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        return Request.
                Delete(createUrl("products/$id", format)).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult getShop(String token, String id, RestCallFormat format) {
        
        def req = Request.Get(createUrl("shops/$id", format))
        
        if (token) req.addHeader(HEADER, token)
        
        return req.execute().handleResponse(new RestResponseHandler(format))
    }
            
    RestCallResult getShops(String token, int start, int count, String status, String sort, RestCallFormat format) {                
        
        def req = Request.Get(createUrl("shops", format, [
            start : start,
            count : count,
            status: status,
            sort  : sort
        ]))

        if (token) req.addHeader(HEADER, token)
        
        return req.execute().handleResponse(new RestResponseHandler(format))        
    }
    
    RestCallResult postShop(String token, Shop shop, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addToForm(form, shop)
        
        return Request.
                Post(createUrl("shops", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult putShop(String token, String id, Shop shop, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addToForm(form, shop)
        
        return Request.
                Put(createUrl("shops/$id", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult patchShop(String token, String id, String field, def value, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        Form form = Form.form()
        addFieldToForm(form, field, value)
        
        return Request.
                Patch(createUrl("shops/$id", format)).
                bodyForm(form.build()).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
    
    RestCallResult deleteShop(String token, String id, RestCallFormat format) {
        
        if (!token) throw new RuntimeException("Empty token")
        
        return Request.
                Delete(createUrl("shops/$id", format)).
                addHeader(HEADER, token).
                execute().
                handleResponse(new RestResponseHandler(format))
    }
        
    private static void addToForm(Form form, def item) {               
        item.class.declaredFields.each {
            if (!it.synthetic) {
                addFieldToForm(form, it.name, item[(it.name)])
            }
        }
    }        
    
    private static void addFieldToForm(Form form, String field, def value) {
        if (value != null) {
            if (value instanceof List) {
                value.each { v ->
                    if (v) form.add(field, v.toString())
                }
            }
            else {
                form.add(field, value.toString())
            }
        }
    }
    
    private static String encodeForm(Form form) {
        def list = form.build()                
        def p = list.collect {
            return it.getName() + "=" + URLEncoder.encode(it.getValue(), 'UTF-8')
        }
        return p.join("&")                
    }
    
    static String encode(Map params) {        
        Form form = Form.form()
        params.each { k, v ->
            addFieldToForm(form, k, v)
        }
        return encodeForm(form)
    }        
    
    static String encode(Object o) {
        Form form = Form.form()
        addToForm(form, o)
        return encodeForm(form)
    }
}

