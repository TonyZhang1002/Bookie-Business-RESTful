package service.bookie;

import com.google.gson.Gson;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import service.core.AuthInfo;
import service.core.UserInfo;

import java.io.IOException;

public class AuthTestClient {
   @SuppressWarnings("unchecked")
   public static void main(String[] args)
           throws ResourceException, IOException {
      Gson gson = new Gson();
      ClientResource client = new
              ClientResource("http://localhost:7000/login");
      client.post(gson.toJson(new AuthInfo("z496722204a@gmail.com", "PQR254/1")));
      String location =
              ((Series<NamedValue<String>>)
                      client.
                              getResponseAttributes().
                              get("org.restlet.http.headers")).
                      getFirstValue("Location");
      System.out.println("URL: " + location);
      new ClientResource(location).get().write(System.out);
   }
}
