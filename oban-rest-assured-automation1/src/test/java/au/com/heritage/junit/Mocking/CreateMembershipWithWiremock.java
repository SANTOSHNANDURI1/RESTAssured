package au.com.heritage.junit.Mocking;

import au.com.heritage.junit.commonFunctions.CommonFunctions;
import com.github.tomakehurst.wiremock.WireMockServer;
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
public class CreateMembershipWithWiremock {

    public WireMockServer wireMockServer = new WireMockServer(18080); //No-args constructor will start on port 18080, no HTTPS
    CommonFunctions comm = new CommonFunctions();

    @Title("This Test will Mock a 500 response for a new membershipAPI request")
    @Test
    public void status503Check() throws IOException {

        //stubbing the response to the relevant request using Wiremock API
        String strRequestBody = comm.readTextFile("src/main/resources/apiRequest/WireMock/MembershipCreationBody.txt");
        System.out.println("Request Body: \n" + strRequestBody);
        wireMockServer.start();
        wireMockServer.stubFor(post(urlEqualTo("/memberships"))
                .withHeader("breadcrumbid", equalTo("d214394a46684631a74ff160b72e3c2b"))
                .withHeader("operatorusername", equalTo("svc_originationTAP"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalTo(strRequestBody))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("{\"errors\":[{\"code\":\"503\",\"title\":\" Server Error\",\"detail\":\"\"}]}")
                        .withStatus(503).withStatusMessage("Server Unavailable")));

        String mockEndPointUrl = "http://localhost:18080/memberships";

        //sending the HTTP request and stubbed response is sent back
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost getReq = new HttpPost(mockEndPointUrl);
            getReq.addHeader("breadcrumbid", "d214394a46684631a74ff160b72e3c2b");
            getReq.addHeader("operatorusername", "svc_originationTAP");
            StringEntity bodyStrEntity = new StringEntity(strRequestBody);
            getReq.addHeader("Content-Type", "application/json");
            getReq.addHeader("Accept", "application/json");
            getReq.setEntity(bodyStrEntity);
            bodyStrEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            CloseableHttpResponse httpResponse = client.execute(getReq);
            System.out.println(httpResponse);
            assert httpResponse.getStatusLine().getStatusCode() == 503;
        } catch (IOException e) {
            throw new RuntimeException("Unable to call " + mockEndPointUrl, e);
        }
        wireMockServer.stop();
    }
}
