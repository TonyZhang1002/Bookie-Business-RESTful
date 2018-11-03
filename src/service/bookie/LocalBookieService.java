package service.bookie;

import com.mongodb.*;
import service.core.AuthInfo;
import service.core.BookieService;

import service.core.UserInfo;

import java.util.Date;

public class LocalBookieService implements BookieService {
   @Override
   public boolean login(AuthInfo authInfo) {

      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("usersdb");
      DBCollection table = db.getCollection("user");

      /**** Find and display ****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", authInfo.getEmail());
      searchQuery.append("password", authInfo.getPassword());
      DBCursor cursor = table.find(searchQuery);

      while (cursor.hasNext()) {
         mongo.close();
         return true;
      }
      mongo.close();
      return false;
   }

   @Override
   public AuthInfo registryUser(UserInfo userInfo) {

      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("usersdb");
      DBCollection table = db.getCollection("user");

      // TODO: Need to determine if have similar email

      /**** Insert ****/
      // create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("username", userInfo.getUsername());
      document.put("age", userInfo.getAge());
      document.put("email", userInfo.getEmail());
      document.put("password", userInfo.getPassword());
      table.insert(document);
      mongo.close();

      AuthInfo newAuthInfo = new AuthInfo(userInfo.getEmail(), userInfo.getPassword());
      return newAuthInfo;
   }
   
}
