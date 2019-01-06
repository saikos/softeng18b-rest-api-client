package gr.ntua.ece.softeng18b.client

import gr.ntua.ece.softeng18b.client.rest.LowLevelAPI
import gr.ntua.ece.softeng18b.client.rest.RestCallFormat
import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.ProductList
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.model.ShopList

import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.WireMockServer
import static com.github.tomakehurst.wiremock.client.WireMock.*

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import groovy.json.JsonOutput

@Stepwise class RestAPISpecification extends Specification {       
        
    @Shared WireMockServer wms    
    @Shared RestAPI api = new RestAPI(Helper.HOST, Helper.PORT, false)
    
    def setupSpec() {
        wms = new WireMockServer(WireMockConfiguration.options().port(Helper.PORT))                
        wms.start()                                
    }
    
    def cleanupSpec() {
        wms.stop()
    }        
    
    def "User logs in" () {        
        given:
        wms.givenThat(
            post(urlEqualTo("$LowLevelAPI.BASE_PATH/login")).
            withRequestBody(equalTo(LowLevelAPI.encode([username:Helper.USER,password:Helper.PASS]))).
            willReturn(
                okJson("""{"token":"${Helper.TOKEN}"}""")
            )
        )
        
        when:
        api.login(Helper.USER, Helper.PASS, RestCallFormat.JSON)
        
        then:
        api.isLoggedIn()
    }
        
    def "User adds two products" () {               
        given:                 
        wms.givenThat(
            post("$LowLevelAPI.BASE_PATH/products").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).
            withRequestBody(equalTo(LowLevelAPI.encode(data))).
            willReturn(
                okJson(JsonOutput.toJson(product))
            )
        )        
        
        expect:
        product == api.postProduct(new Product(data), RestCallFormat.JSON)
        
        where:
        data              | product 
        Helper.PROD1_DATA | Helper.PROD1
        Helper.PROD2_DATA | Helper.PROD2
                
    }
    
    def "User lists two products"() {
        given:                 
        wms.givenThat(
            get("$LowLevelAPI.BASE_PATH/products?start=0&count=10&status=ACTIVE&sort=id%7CASC").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).            
            willReturn(
                okJson(JsonOutput.toJson(Helper.PRODUCTS))
            )
        )
        
        when:
        ProductList results = api.getProducts(0, 10, "ACTIVE", "id|ASC", RestCallFormat.JSON)
        
        then:
        results == Helper.PRODUCTS
    }
    
    def "User updates a product" () {
        given:         
        Product p     = Helper.PROD1
        p.name        = "NewName"
        p.description = "NewDescription" 
        wms.givenThat(
            put("$LowLevelAPI.BASE_PATH/products/1").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).
            withRequestBody(equalTo(LowLevelAPI.encode(p))).
            willReturn(
                okJson(JsonOutput.toJson(p))
            )
        )
        
        expect:
        p == api.putProduct("1", p, RestCallFormat.JSON)
    }
        
    def "User deletes a product"() {
        given:                 
        wms.givenThat(
            delete("$LowLevelAPI.BASE_PATH/products/2").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).            
            willReturn(
                okJson('{"message":"OK"}')
            )
        )
        
        when:
        api.deleteProduct("2", RestCallFormat.JSON)
        
        then:
        noExceptionThrown()
    }
    
    def "User adds two shops" () {               
        given:                 
        wms.givenThat(
            post("$LowLevelAPI.BASE_PATH/shops").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).
            withRequestBody(equalTo(LowLevelAPI.encode(data))).
            willReturn(
                okJson(JsonOutput.toJson(shop))
            )
        )        
        
        expect:
        shop == api.postShop(new Shop(data), RestCallFormat.JSON)
        
        where:
        data              | shop 
        Helper.SHOP1_DATA | Helper.SHOP1
        Helper.SHOP2_DATA | Helper.SHOP2
                
    }
    
    def "User lists two shops"() {
        given:                 
        wms.givenThat(
            get("$LowLevelAPI.BASE_PATH/shops?start=0&count=10&status=ACTIVE&sort=id%7CASC").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).            
            willReturn(
                okJson(JsonOutput.toJson(Helper.SHOPS))
            )
        )
        
        when:
        ShopList results = api.getShops(0, 10, "ACTIVE", "id|ASC", RestCallFormat.JSON)
        
        then:
        results == Helper.SHOPS
    }
    
    def "User updates a shop" () {
        given:         
        Shop s = Helper.SHOP1
        s.name = "NewName"
        s.tags = ["New", "Tags"] 
        wms.givenThat(
            put("$LowLevelAPI.BASE_PATH/shops/1").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).
            withRequestBody(equalTo(LowLevelAPI.encode(s))).
            willReturn(
                okJson(JsonOutput.toJson(s))
            )
        )
        
        expect:
        s == api.putShop("1", s, RestCallFormat.JSON)
    }
        
    def "User deletes a shop"() {
        given:                 
        wms.givenThat(
            delete("$LowLevelAPI.BASE_PATH/shops/2").
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).            
            willReturn(
                okJson('{"message":"OK"}')
            )
        )
        
        when:
        api.deleteShop("2", RestCallFormat.JSON)
        
        then:
        noExceptionThrown()
    }
    
    def "User logs out"() {
        given:
        wms.givenThat(
            post(urlEqualTo("$LowLevelAPI.BASE_PATH/logout")).
            withHeader(LowLevelAPI.HEADER, equalTo(Helper.TOKEN)).
            willReturn(
                okJson('{"message":"OK"}')
            )
        )
        
        when:
        api.logout(RestCallFormat.JSON)
        
        then:
        !api.isLoggedIn()
    }
}

