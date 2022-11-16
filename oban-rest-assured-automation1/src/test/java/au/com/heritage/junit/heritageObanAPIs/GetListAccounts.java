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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/ListAccounts.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetListAccounts extends membershipOBANTestBase{
    int count = 0;
    int maxTries = 2;
    String accountHolderCount;
    String statusCode;
    String userId;
    String accountId;
    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/ListAccounts.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetListAccounts() throws IOException {

    }
    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode( String statusCode) {
        this.statusCode = statusCode;
    }

    public void setHolderCount(String accountHolderCount) {
        this.accountHolderCount = accountHolderCount;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Test
    public void getDataDrivenAccountsAPITest() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadFinal = payload.replace("{{userId}}", userId);
            String url = "/ListAccounts";

                   res= comm.serenityPostRequestMethod(payloadFinal,url);
                    if (res.statusCode() == Integer.parseInt(statusCode)) {
                        bool = true;
                        if(statusCode.equals("200")){
                            JsonPath jsonPathEvaluator = res.jsonPath();
                            String accountHolderCountRes = jsonPathEvaluator.get("response.data.accounts.jointAccountHolderCount[0]").toString();
                            String accountIdResponse = jsonPathEvaluator.get("response.data.accounts.accountId[0]");
                            if (!accountHolderCountRes.equals(accountHolderCount)||! accountIdResponse.contains(accountId))
                                bool = false;
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
