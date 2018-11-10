package service.bookie;
import com.google.gson.Gson;
import org.restlet.*;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import service.core.AuthInfo;
import service.core.BetInfo;
import service.core.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class BookieApplication extends Application{
   private static Map<String, AuthInfo> onlineRecords = new HashMap<String, AuthInfo>();
   private static Map<String, AuthInfo> MACRecords = new HashMap<String, AuthInfo>();
   static Gson gson = new Gson();

   public Restlet createInboundRoot() {
      Router router = new Router(getContext());
      LocalBookieService bookieService = new LocalBookieService();

      /*
      This is a post method to tell if the client can login
       */
      router.attach("/login", new Restlet() {
         public void handle(Request request, Response response) {
            if (request.getMethod().equals(Method.POST)) {
               String json = request.getEntityAsText();
               AuthInfo authInfo = gson.fromJson(json, AuthInfo.class);
               // If login is true (find matching email and password)
               if (bookieService.login(authInfo)) {
                  if (!onlineRecords.containsKey(authInfo.getUniqueID())) {
                     // Put the user online record
                     onlineRecords.put(authInfo.getUniqueID(), authInfo);
                     MACRecords.put(authInfo.getMACAddress(), authInfo);
                     response.setLocationRef(request.getHostRef() + "/user/" + authInfo.getUniqueID());
                  } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
               } else response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            } else response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
         }
      });

      /*
      This is a get method to give the client the auth info
       */
      router.attach("/user/{uniqueID}", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            String uniqueID = (String) request.getAttributes().get("uniqueID");
            // We need to make sure the MAC address is in our record
            // This is used to prevent other computer login without a correct MAC address
            if(onlineRecords.containsKey(uniqueID) && MACRecords.containsKey(onlineRecords.get(uniqueID).getMACAddress())) {
               response.setStatus(Status.SUCCESS_OK);
               if (request.getMethod().equals(Method.GET)) {
                  response.setEntity(gson.toJson(onlineRecords.get(uniqueID)), MediaType.APPLICATION_JSON);
                  response.setStatus(Status.SUCCESS_OK);
               }
            } else {
               response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            }
         }
      });

      /*
      This is a post method to register user
       */
      router.attach("/registry", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            if (request.getMethod().equals(Method.POST)) {
               String json = request.getEntityAsText();
               UserInfo userInfo = gson.fromJson(json, UserInfo.class);
               // If not exist this email
               if(!bookieService.ifExist(userInfo)) {
                  // Then register the user
                  bookieService.registryUser(userInfo);
               } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
         }
      });

      /*
      This is a demo implement for betting
       */
      router.attach("/user/{uniqueID}/bet", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            String uniqueID = (String) request.getAttributes().get("uniqueID");
            // We need to make sure the MAC address is in our record
            // This is used to prevent other computer login without a correct MAC address
            if(onlineRecords.containsKey(uniqueID) && MACRecords.containsKey(onlineRecords.get(uniqueID).getMACAddress())) {
               if (request.getMethod().equals(Method.POST)) {
                  String json = request.getEntityAsText();
                  BetInfo betInfo = gson.fromJson(json, BetInfo.class);
                  bookieService.bet(betInfo);
                  response.setLocationRef(request.getHostRef() + "/user/" + uniqueID + "/bet/" + betInfo.getUniqueBetID());
               } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
         }
      });

      /*
      This is a get method to give the client the current balance
       */
      router.attach("/user/{uniqueID}/bet/{uniqueBetID}", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            String uniqueID = (String) request.getAttributes().get("uniqueID");
            // We need to make sure the MAC address is in our record
            // This is used to prevent other computer login without a correct MAC address
            if(onlineRecords.containsKey(uniqueID) && MACRecords.containsKey(onlineRecords.get(uniqueID).getMACAddress())) {
               response.setStatus(Status.SUCCESS_OK);
               if (request.getMethod().equals(Method.GET)) {
                  response.setEntity(gson.toJson(bookieService.getCurrentBalance(onlineRecords.get(uniqueID))),
                          MediaType.APPLICATION_JSON);
                  response.setStatus(Status.SUCCESS_OK);
               }
            } else {
               response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            }
         }
      });

      return router;
   }

   // Publish the application
   public static void main(String[] args) throws Exception{
      Component component = new Component();
      component.getServers().add(Protocol.HTTP, 7000);
      component.getDefaultHost().
              attach("", new BookieApplication());
      component.start();
   }
}
