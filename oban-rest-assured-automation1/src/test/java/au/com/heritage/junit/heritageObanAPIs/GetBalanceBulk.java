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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/BalanceBulk.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetBalanceBulk extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String accountId;
    String availableBalance ;
    String currentBalance;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/BalancesBulk.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetBalanceBulk() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setAccountId(String accountId) { this.accountId = accountId; }

    public void setAvailableBalance(String availableBalance) {this.availableBalance = availableBalance; }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Test
    public void getDataDrivenBalanceAccounts() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadTemp = payload.replace("{{userId}}", userId);

            String payloadFinal = payloadTemp.replace("{{accountId}}", accountId);
            String url = "/Balances";

                res = comm.serenityPostRequestMethod(payloadFinal, url);
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                    bool = true;
                    if (statusCode.equals("200")) {
                        JsonPath jsonPathEvaluator = res.jsonPath();
                        String accountIdResponse = jsonPathEvaluator.get("response.data.balances.accountId[0]");
                        String  currentBalanceResponse  = jsonPathEvaluator.get("response.data.balances.currentBalance[0]");
                        String  availableBalanceResponse  = jsonPathEvaluator.get("response.data.balances.availableBalance[0]");


                        if (!accountIdResponse.contains(accountId)||(!availableBalanceResponse.contains(availableBalance))
                                ||(!currentBalanceResponse.contains(currentBalance)))
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
