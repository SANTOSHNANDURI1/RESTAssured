package au.com.heritage.junit.heritageObanAPIs;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/DirectDebitBulk.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetDirectDebitBulk extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String accountId ;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/DirectDebitBulk.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetDirectDebitBulk() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setEmailAddress(String accountId) { this.accountId = accountId; }


    @Test
    public void getDataDrivenAccountDetailsAPITest() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadFinal = payload.replace("{{userId}}", userId);
            String url = "/DirectDebits";

                res = comm.serenityPostRequestMethod(payloadFinal, url);
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                    bool = true;
                    if (statusCode.equals("200")) {
                        JsonPath jsonPathEvaluator = res.jsonPath();
                        int totalRecordsResponse = jsonPathEvaluator.get("response.totalRecords");
                        if(totalRecordsResponse!=0){
                            String accountIdResponse = jsonPathEvaluator.get("response.data.directDebitAuthorisations.accountId[0]");
                            if (!accountIdResponse.contains(accountId))
                                bool = false;
                        }else{
                            System.out.println("No Records present for any validations");
                        }

                    }
                }

                if (bool) {
                    assert true;
                    break;
                } else {
                    if (++count == maxTries) {
                        assert false;
                    }
                }



        }
    }
}
