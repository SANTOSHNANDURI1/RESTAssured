package au.com.heritage.junit.heritageObanAPIs;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.Concurrent;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


//Test to Validate the GET membershipAPI with multiple ID's

@SuppressWarnings("ALL")
@RunWith(SerenityParameterizedRunner.class)
//@Concurrent(threads = "4X")
//CSV Location
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/FindUser.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetUserWithTestData extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String secondaryId;
    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/FindUser.json")));

    public GetUserWithTestData() throws IOException {
    }
    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setSecondaryID( String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public void setStatusCode( String statusCode) {
        this.statusCode = statusCode;
    }



    @Title("This Test will display details of an Heritage API Find User, with Membership ID and DOB as primary and secondary Fields:" +
            "Expected Status: 200 OK, 422 Unprocessible, 401 Unauth, 400 Bad Request")
    @Test
    public void getDataDrivenFindUserTest() {
        Response res;
        while (true
        ) {
            String payloadTemp = payload.toString().replace("{{memberID}}", userId);
            String payloadFinal = payloadTemp.toString().replace("{{secondaryID}}", secondaryId);
         //   try {
                res = SerenityRest.given().
                        header("interaction-id", "jkasdd9030923ieisaokq09q-postman").
                      // header("dataholderbrand-id", "1").
                        header("version", "1").
                        header("api_key","031c9f08df23162159a95639bc94b826").
                        contentType(ContentType.JSON).with().
                        body(payloadFinal).
                        log().body().
                        when().
                        post("/FindUser").
                        then().log().all().
                        extract().response();
                System.out.println("RESPONSE"+res.body());
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                   if(statusCode.equals("200")){
                       JsonPath jsonPathEvaluator = res.jsonPath();
                       String userIDResponse = jsonPathEvaluator.get("response.userId");
                       if (userIDResponse.contains(userId))
                           assert true;
                   }
                    break;
                }
                else{
                    if (++count == maxTries){
                        assert false;
                    }
                }

//            } catch (Exception e) {
//                System.out.println("");
//                if (++count == maxTries) throw e;
//            }
        }

    }
}
