package client;

import com.google.gson.Gson;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import service.core.AuthInfo;
import service.core.BetInfo;
import service.core.UserInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

import static client.MACAddress.getMACAddress;

public class AuthTestClient {
   @SuppressWarnings("unchecked")
   public static void main(String[] args)
           throws Exception {
      Gson gson = new Gson();
      ClientResource client = new
              ClientResource("http://localhost:7000/login");

      // Get the MAC address
      InetAddress ia = InetAddress.getLocalHost();
      String MACAddress = getMACAddress(ia);

      client.post(gson.toJson(new AuthInfo("123@ucdconnect.ie", "PQR253/1", MACAddress)));
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
