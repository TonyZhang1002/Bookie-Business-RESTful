package service.bookie;

import com.google.gson.Gson;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import service.core.AuthInfo;
import service.core.BetInfo;
import service.core.UserInfo;

import java.io.IOException;

public class AuthTestClient {
   @SuppressWarnings("unchecked")
   public static void main(String[] args)
           throws ResourceException, IOException {
      Gson gson = new Gson();
      ClientResource client = new
              ClientResource("http://localhost:7000/login");
      client.post(gson.toJson(new AuthInfo("15205944@ucdconnect.ie", "PQR253/1")));
      String location =
              ((Series<NamedValue<String>>)
                      client.
                              getResponseAttributes().
                              get("org.restlet.http.headers")).
                      getFirstValue("Location");
      System.out.println("URL: " + location);
      new ClientResource(location).get().write(System.out);
      // Now bet
      System.out.println("\n" + "Bet on url: " + location + "/bet");
      ClientResource betClient = new
              ClientResource(location + "/bet");
      betClient.post(gson.toJson(new BetInfo(20, "15205944@ucdconnect.ie")));
      String betLocation =
              ((Series<NamedValue<String>>)
                      betClient.
                              getResponseAttributes().
                              get("org.restlet.http.headers")).
                      getFirstValue("Location");
      System.out.println("URL: " + betLocation);
      new ClientResource(betLocation).get().write(System.out);
   }
}
