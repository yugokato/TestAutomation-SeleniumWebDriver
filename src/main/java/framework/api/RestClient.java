package framework.api;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


public class RestClient {
    private static final Logger logger = Logger.getLogger(RestClient.class);
    
    public RestClient(){
        logger.setLevel(Level.ALL);
    }

    protected Response requestGET(String urlStr){
        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .get(urlStr);
        logger.info("GET: url = " + urlStr);
        logger.info("GET: response = " + response.asString().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", ""));
        
        return response;

    }
    
    protected Response requestPOST(String urlStr, String data){
        Response response;
        if (data == null){
            response = given()
                    .contentType(ContentType.JSON)
                    .post(urlStr);
            logger.info("POST: url = " + urlStr);
            logger.info("POST: response = " + response.asString().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", ""));
        }
        
        else{
            response = given()
                    .contentType(ContentType.JSON)
                    .body(data)
                    .post(urlStr);
            logger.info("POST: url = " + urlStr + " body = " + data);
            logger.info("POST: response = " + response.asString().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", ""));
        }
        
        return response;
        
    }
    
    protected Response requestDELETE(String urlStr, String data){
        Response response;
        if (data == null){
            response = given()
                    .contentType(ContentType.JSON)
                    .delete(urlStr);
            logger.info("DELETE: url = " + urlStr);
            logger.info("DELETE: response = " + response.asString().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", ""));
        }
        
        else{
            response = given()
                    .contentType(ContentType.JSON)
                    .body(data)
                    .delete(urlStr);
            logger.info("DELETE: url = " + urlStr + " body = " + data);
            logger.info("DELETE: response = " + response.asString().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", ""));
        }
        
        return response;
 
    }
}
