package au.com.heritage.junit.heritageObanAPIs;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
import com.jayway.jsonpath.DocumentContext;
//import io.restassured.path.json.JsonPath;
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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/AccountsDetails.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetAccountDetails extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String accountID;
    String creationDate;
    String displayName;
    String openStatus;
    //String isOwned;
    //String maskedNumber;
    //String productCategory;
    String productCategory;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/AccountDetails.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetAccountDetails() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public void setProductCode(String productCategory) {
        this.productCategory = productCategory;
    }

    //This will test Get Account Details with multiple data from CSV, status 200, 401
    @Test
    public void getDataDrivenAccountDetailsAPITest() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payLoadTemp = payload.replace("{{accountId}}", accountID);
            String payloadFinal = payLoadTemp.replace("{{userId}}", userId);
            String url = "/Accounts";
            try {
                res = comm.serenityPostRequestMethod(payloadFinal, url);
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                    bool = true;
                    if (statusCode.equals("200")) {
                        JsonPath jsonPathEvaluator = res.jsonPath();
                        String accountIDResponse = jsonPathEvaluator.get("response.data.accounts.accountId[0]");
                        String creationDateResponse = jsonPathEvaluator.get("response.data.accounts.creationDate[0]");
                        String displayNameResponse = jsonPathEvaluator.get("response.data.accounts.displayName[0]");
                        String openStatusResponse = jsonPathEvaluator.get("response.data.accounts.openStatus[0]");
                        String productCategoryResponse = jsonPathEvaluator.get("response.data.accounts.productCategory[0]");

                        if (!productCategoryResponse.contains(productCategory)||(!accountIDResponse.contains(accountID))
                                ||(!creationDateResponse.contains(creationDate))||(!displayNameResponse.contains(displayName))
                                ||(!openStatusResponse.equalsIgnoreCase(openStatus)))
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
            catch (Exception e) {
                System.out.println("");
                //     if (++count == maxTries) throw e;
            }


        }
    }
}
