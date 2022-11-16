package au.com.heritage.junit.heritageObanAPIs;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/Accounts.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetAccounts extends membershipOBANTestBase{
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/Accounts.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetAccounts() throws IOException {

    }
    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode( String statusCode) {
        this.statusCode = statusCode;
    }


    @Test
    public void getDataDrivenAccountsAPITest() {
        Response res;
        while (true) {
            String payloadFinal = payload.replace("{{userId}}", userId);
            String url = "/Accounts";
            try {
                   res= comm.serenityPostRequestMethod(payloadFinal,url);
                    if (res.statusCode() == Integer.parseInt(statusCode)) {
                      break;
                    }
                    else{
                        if (++count == maxTries){
                            assert false;
                        }
                    }

            } catch (Exception e) {
                System.out.println("");
                //     if (++count == maxTries) throw e;
            }
        }

    }
}
