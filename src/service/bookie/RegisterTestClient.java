package service.bookie;

import com.google.gson.Gson;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import service.core.UserInfo;

import java.io.IOException;

public class RegisterTestClient {
   @SuppressWarnings("unchecked")
   public static void main(String[] args)
           throws ResourceException, IOException {
      Gson gson = new Gson();
      ClientResource client = new
              ClientResource("http://localhost:7000/registry");
      client.post(gson.toJson(new UserInfo("Tony Zhang", 22, "PQR253/1", "z496722204@gmail.com")));
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
