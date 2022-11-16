package au.com.heritage.junit.heritageObanAPIs;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.en.Given;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Title;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SuppressWarnings("ALL")
@RunWith(SerenityRunner.class)
//@RunWith(PowerMockRunner.class)

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MockErrorCodes {

    public WireMockServer wireMockServer = new WireMockServer(18080); //No-args constructor will start on port 18080, no HTTPS
    CommonFunctions comm = new CommonFunctions();


    //@Given("^I receive a 500 error for a valid FindUser request")
    /*
    This test will mock 500 Response for Accounts API valid Request payload
     */
    @Test
    public void i_receive_500_error_Accounts() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/Accounts500.txt";
        String stubForURl="/Accounts";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    /*
    This test will mock 500 Response for FindUser API valid Request payload
     */
    @Test
    public void i_receive_500_error_FindUser() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/FindUser500.txt";
        String stubForURl="/FindUser";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    /*
    This test will mock 500 Response for Customer Detail API valid Request payload
     */
    @Test
    public void i_receive_500_error_CustomerDetail() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/CustomerDetail500.txt";
        String stubForURl="/Customer/Detail";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    /*
    This test will mock 500 Response for Balances Accounts API valid Request payload
     */
    @Test
    public void i_receive_500_error_BalancesAccount() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/BalancesAccount500.txt";
        String stubForURl="/Balances";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    //@Given("^I receive a 500 error for a valid SchedPaymentAccounts request")
    /*
    This test will mock 500 Response for SchedPaymentAccounts API valid Request payload
     */
    @Test
    public void i_receive_500_error_SchedPaymentAccounts() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/ScheduledPaymentAccount500.txt";
        String stubForURl="/ScheduledPayments";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    //@Given("^I receive a 500 error for a valid Transactions API request")
    /*
    This test will mock 500 Response for Transactions API valid Request payload
     */
    @Test
    public void i_receive_500_error_Transactions() throws Exception{
        String requestBodyFilePath = "src/main/resources/apiRequest/WireMock/Transactions500.txt";
        String stubForURl="/Transactios";
        status500CheckCommonFunction(requestBodyFilePath,stubForURl);
    }

    private void status500CheckCommonFunction(String requestBodyFilePath, String stubForURl) throws IOException {
        String strRequestBody = comm.readTextFile(requestBodyFilePath);
        System.out.println("Request Body: \n" + strRequestBody);
        wireMockServer.start();
        wireMockServer.stubFor(post(urlEqualTo(stubForURl))
                .withHeader("interaction-id", equalTo("jkasdd9030923ieisaokq09q-postman"))
                .withHeader("version", equalTo("1"))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalTo(strRequestBody))
                .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("{\"errors\":[{\"code\":\"500\",\"title\":\" Server Error\",\"detail\":\"\"}]}")
                .withStatus(500).withStatusMessage("Internal Server Error")));

        String mockEndPointUrl = "http://localhost:18080"+stubForURl;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost getReq = new HttpPost(mockEndPointUrl);
            getReq.addHeader("interaction-id", "jkasdd9030923ieisaokq09q-postman");
            getReq.addHeader("version", "1");
            StringEntity bodyStrEntity = new StringEntity(strRequestBody);
            getReq.addHeader("Content-Type", "application/json");
            getReq.addHeader("Accept", "application/json");
            getReq.setEntity(bodyStrEntity);
            bodyStrEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            CloseableHttpResponse httpResponse = client.execute(getReq);
            System.out.println(httpResponse);
            assert httpResponse.getStatusLine().getStatusCode() == 500;
        } catch (IOException e) {
            throw new RuntimeException("Unable to call " + mockEndPointUrl, e);
        }
        wireMockServer.stop();
    }
}
