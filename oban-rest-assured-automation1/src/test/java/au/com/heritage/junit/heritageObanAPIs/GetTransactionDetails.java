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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/TransactionDetails.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetTransactionDetails extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String accountId;
    String transactionId;
    String type;
    String status;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/TransactionDetails.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetTransactionDetails() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setAccountId(String accountId) { this.accountId = accountId; }

    public void setType(String transactionId) { this.transactionId = transactionId; }

    public void setStatus(String status) { this.status = status; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    @Test
    public void getDataDrivenTransactions() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadTemp = payload.replace("{{userId}}", userId);

            String payloadTemp1 = payloadTemp.replace("{{accountId}}", accountId);
            String payloadFinal = payloadTemp1.replace("{{transactionId}}", transactionId);
            String url = "Transaction/Detail";

                res = comm.serenityPostRequestMethod(payloadFinal, url);
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                    bool = true;
                    if (statusCode.equals("200")) {
                        JsonPath jsonPathEvaluator = res.jsonPath();
                        String accountIdResponse = jsonPathEvaluator.get("response.data.transactions.accountId[0]");
                        String  transactionIdResponse  = jsonPathEvaluator.get("response.data.transactions.transactionId[0]");
                        String  statusResponse  = jsonPathEvaluator.get("response.data.transactions.status[0]");
                        String  typeResponse  = jsonPathEvaluator.get("response.data.transactions.type[0]");

     //                   if (!accountIdResponse.contains(accountId)||(!transactionIdResponse.contains(transactionId))
       //                         ||(!statusResponse.contains(status))||!typeResponse.contains(type))
         //                   bool = false;
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
