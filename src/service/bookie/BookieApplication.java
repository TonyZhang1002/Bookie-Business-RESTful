package service.bookie;
import com.google.gson.Gson;
import org.restlet.*;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import service.core.AuthInfo;
import service.core.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class BookieApplication extends Application{
   private static Map<String, AuthInfo> onlineRecords = new HashMap<String, AuthInfo>();
   static Gson gson = new Gson();

   public Restlet createInboundRoot() {
      Router router = new Router(getContext());
      LocalBookieService bookieService = new LocalBookieService();

      router.attach("/login", new Restlet() {
         public void handle(Request request, Response response) {
            if (request.getMethod().equals(Method.POST)) {
               String json = request.getEntityAsText();
               AuthInfo authInfo = gson.fromJson(json, AuthInfo.class);
               if (bookieService.login(authInfo)) {
                  if (!onlineRecords.containsKey(authInfo.getUniqueID())) {
                     onlineRecords.put(authInfo.getUniqueID(), authInfo);
                     response.setLocationRef(request.getHostRef() + "/user/" + authInfo.getUniqueID());
                  } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
               } else response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            } else response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
         }
      });

      router.attach("/user/{uniqueID}", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            String uniqueID = (String) request.getAttributes().get("uniqueID");
            if(onlineRecords.containsKey(uniqueID)) {
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

      router.attach("/registry", new Restlet() {
         @Override
         public void handle(Request request, Response response) {
            if (request.getMethod().equals(Method.POST)) {
               String json = request.getEntityAsText();
               UserInfo userInfo = gson.fromJson(json, UserInfo.class);
               AuthInfo authInfo = bookieService.registryUser(userInfo);
               if(!onlineRecords.containsKey(authInfo.getUniqueID())) {
                  onlineRecords.put(authInfo.getUniqueID(), authInfo);
                  response.setLocationRef(request.getHostRef()+"/user/"+authInfo.getUniqueID());
               } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            } else response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
         }
      });

      return router;
   }

   public static void main(String[] args) throws Exception{
      Component component = new Component();
      component.getServers().add(Protocol.HTTP, 7000);
      component.getDefaultHost().
              attach("", new BookieApplication());
      component.start();
   }
}
