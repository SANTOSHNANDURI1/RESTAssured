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
@UseTestDataFrom("src/main/resources/testData/openBankingAPI/ErrorCodesAPIList.csv")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ErrorCodesTestAllAPIs extends membershipOBANTestBase {
    int count = 0;
    int maxTries = 2;
    String urlAPI;
    String payloadDir;

    CommonFunctions comm = new CommonFunctions();

    public ErrorCodesTestAllAPIs() throws IOException {

    }

    public void setPayloadPath(String payloadDir) {
        this.payloadDir = payloadDir;
    }

    public void setStrAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }



    @Test
    public void getDataDrivenErrorCodesAPITestNoHeader() throws IOException {
        Response res;
        while (true) {
            String strDirectory = "src/main/resources/apiRequest/openBankingAPI/"+payloadDir;
            String relPath = urlAPI;
            String payload = new String(Files.readAllBytes(Paths.get(strDirectory)));
            String url = "/"+relPath;
            res = comm.serenityPostRequestErrorCode400NoHeader(payload, urlAPI);
            if (res.statusCode() == 400) {
                assert true;
                break;
            }
            else{
                assert false;
            }
        }
    }

    @Test
    public void getDataDrivenErrorCodesAPITestNoInteractionID() throws IOException {
        Response res;
        while (true) {
            String strDirectory = "src/main/resources/apiRequest/openBankingAPI/"+payloadDir;
            String relPath = urlAPI;
            String payload = new String(Files.readAllBytes(Paths.get(strDirectory)));
            String url = "/"+relPath;
            res = comm.serenityPostRequestErrorCode400NoInteractionID(payload, urlAPI);
            if (res.statusCode() == 400) {
                assert true;
                break;
            }
            else{
                assert false;
            }
        }
    }
}
