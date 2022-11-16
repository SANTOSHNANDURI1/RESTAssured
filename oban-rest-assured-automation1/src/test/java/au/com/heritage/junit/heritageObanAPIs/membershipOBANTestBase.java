package au.com.heritage.junit.heritageObanAPIs;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class membershipOBANTestBase {

    @BeforeClass
    public static void init() {
        RestAssured.baseURI = "https://open-banking-ey-gateway-stage-ob.apps.ocp-tstb.hbs.net.au";
    }
}
