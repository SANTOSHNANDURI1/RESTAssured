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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/CustomerDetails.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class GetCustomerDetail extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String statusCode;
    String userId;
    String address_em ;
    String mailingName;
    String number;
    String addressLine1;

    String payload = new String(Files.readAllBytes(Paths.get("src/main/resources/apiRequest/openBankingAPI/CustomerDetail.json")));
    CommonFunctions comm = new CommonFunctions();

    public GetCustomerDetail() throws IOException {

    }

    public void setMembershipID(String userId) {
        this.userId = userId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setEmailAddress(String address_em) { this.address_em = address_em; }

    public void setMailingName(String mailingName) {this.mailingName = mailingName;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setPhoneNumber(String phoneNumber) {this.number = number;}



    @Test
    public void getDataDrivenAccountDetailsAPITest() {
        Boolean bool = false;
        Response res;
        while (true) {
            String payloadFinal = payload.replace("{{userId}}", userId);
            String url = "/Customer/Detail";
            try {
                res = comm.serenityPostRequestMethod(payloadFinal, url);
                if (res.statusCode() == Integer.parseInt(statusCode)) {
                    bool = true;
                    if (statusCode.equals("200")) {
                        JsonPath jsonPathEvaluator = res.jsonPath();
                        String emailAddressResponse = jsonPathEvaluator.get("response.data.person.emailAddresses.address[0]");
                        String  displayNameResponse  = jsonPathEvaluator.get("response.data.person.physicalAddresses.simple.mailingName[0]");
                        String addressLine1Response = jsonPathEvaluator.get("response.data.person.physicalAddresses.simple.addressLine1[0]");
                        String phoneNumberResponse = jsonPathEvaluator.get("response.data.person.phoneNumbers.number[0]");

                        if (!emailAddressResponse.contains(address_em)||(!displayNameResponse.contains(mailingName))
                                ||(!addressLine1Response.contains(addressLine1))||(!phoneNumberResponse.contains(number)))
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
