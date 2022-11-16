package au.com.heritage.junit.heritageObanAPIs;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/Accounts.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetPayees extends membershipOBANTestBase{
    String statusCode;
    String userId;
    int count = 0;
    int maxTries = 2;
    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/Payees.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetPayees() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Test
    public void getDataDrivenAccountsAPITest() {
        Response res;
        while(true){
            String url = "/Payees";
            String payloadFinal = payload.replace("{{userId}}", userId);
            res= comm.serenityPostRequestMethod(payload,url);
            if (res.statusCode() == 200) {
                assert true;
                break;
            }
            else{
                if (++count == maxTries){
                    assert false;
                }
            }
        }
    }
}
