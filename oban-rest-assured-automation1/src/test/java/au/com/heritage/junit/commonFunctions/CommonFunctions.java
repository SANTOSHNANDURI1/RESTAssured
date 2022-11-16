package au.com.heritage.junit.commonFunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class CommonFunctions {

    /**
     * generic read text from file method
     * @param filename
     * @return String
     * @throws IOException
     */
    public String readTextFile(String filename) throws IOException {
        String everything = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        return everything;
    }

    /**
     * Generic method to send post request
     * @param payloadFile
     * @param url
     * @return Serenity Response
     */
    public Response serenityPostRequestMethod(String payloadFile, String url){
        Response res = SerenityRest.given().
                header("version", "1").
                header("interaction-id", "sadasdsadsadasdsavgfh123-abc").
                header("api_key","031c9f08df23162159a95639bc94b826").
                contentType(ContentType.JSON).with().
                body(payloadFile).
                log().body().
                when().
                post(url).
                then().log().all().
                extract().response();
        return res;
    }

    /**
     * Generic Method to post a request with no header to generate 400
     * @param payloadFile
     * @param url
     * @return Sereity Response
     */
    public Response serenityPostRequestErrorCode400NoHeader(String payloadFile, String url){
        Response res = SerenityRest.given().
                header("interaction-id", "sadasdsadsadasdsavgfh123-abc").
                header("api_key","031c9f08df23162159a95639bc94b826").
                contentType(ContentType.JSON).with().
                body(payloadFile).
                log().body().
                when().
                post(url).
                then().log().all().
                extract().response();
        return res;
    }
    /**
     * Generic Method to post a request with no interaction ID to generate 400
     * @param payloadFile
     * @param url
     * @return Sereity Response
     */
    public Response serenityPostRequestErrorCode400NoInteractionID(String payloadFile, String url){
        Response res = SerenityRest.given().
                header("version", "1").
                header("api_key","031c9f08df23162159a95639bc94b826").
                contentType(ContentType.JSON).with().
                body(payloadFile).
                log().body().
                when().
                post(url).
                then().log().all().
                extract().response();
        return res;
    }
}
