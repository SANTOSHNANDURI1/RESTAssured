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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/ScheduledPaymentsAccounts.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetScheduledPaymentAccount extends membershipOBANTestBase{
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String accountId;
    String scheduledPaymentId;
    String accountName;
    String bsb;
    String accountNumber;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/ScheduledPaymentAccount.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetScheduledPaymentAccount() throws IOException {

    }
    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode( String statusCode) {
        this.statusCode = statusCode;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setScheduledPaymentId(String scheduledPaymentId) {
        this.scheduledPaymentId = scheduledPaymentId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBSB(String bsb) { this.bsb = bsb;}

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }



    @Test
    public void getDataDrivenScheduledPaymentTest() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadTemp = payload.replace("{{userId}}", userId);
            String payloadFinal = payloadTemp.replace("{{accountId}}", accountId);
            String url = "/ScheduledPayments";
                   res= comm.serenityPostRequestMethod(payloadFinal,url);
                    if (res.statusCode() == Integer.parseInt(statusCode)) {
                        bool= true;
                        if(statusCode.equals("200")){
                            JsonPath jsonPathEvaluator = res.jsonPath();
                            int totalRecordsResponse = jsonPathEvaluator.get("response.totalRecords");
                            if(totalRecordsResponse!=0){
                                String accountNameResponse = jsonPathEvaluator.get("response.data.scheduledPayments.paymentSet.to.domestic.account.accountName[0]").toString();
                                String accountIDResponse = jsonPathEvaluator.get("response.data.scheduledPayments.from.accountId[0]").toString();
                                String bsbResponse = jsonPathEvaluator.get("response.data.scheduledPayments.paymentSet.to.domestic.account.bsb[0]").toString();
                                String accountNumberResponse = jsonPathEvaluator.get("response.data.scheduledPayments.paymentSet.to.domestic.account.accountNumber[0]").toString();
                                String schedPaymentIdFirstResponse = jsonPathEvaluator.get("response.data.scheduledPayments.scheduledPaymentId[0]").toString();

                                if (!accountIDResponse.contains(accountId)||!accountNameResponse.contains(accountName)
                                        ||!bsbResponse.contains(bsb)||!accountNumberResponse.contains(accountNumber)
                                        ||!schedPaymentIdFirstResponse.contains(scheduledPaymentId))
                                    bool = false;


                            }

                            }
                        if(bool){
                            assert true;
                            break;
                        }
                        else{
                            if (++count == maxTries){
                                assert false;
                                break;
                            }
                    }

                    }
        }

    }
}
