package client;

import com.google.gson.Gson;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import service.core.BetInfo;

import java.io.IOException;

public class BetTestClient {
   @SuppressWarnings("unchecked")
   public static void bet(String location) throws ResourceException, IOException {
      // Now bet
      Gson gson = new Gson();
      System.out.println("\n" + "Bet on url: " + location + "/bet");
      ClientResource betClient = new
              ClientResource(location + "/bet");
      betClient.post(gson.toJson(new BetInfo(20, "123@ucdconnect.ie")));
      String betLocation =
              ((Series<NamedValue<String>>)
                      betClient.
                              getResponseAttributes().
                              get("org.restlet.http.headers")).
                      getFirstValue("Location");
      new ClientResource(betLocation).get().write(System.out);
   }

   public static void main(String[] args) throws Exception {
      // Need to run the AuthTestClient first and paste the url here
      bet("http://localhost:7000/user/acKHyk9BncrdrVe4");
   }
}
