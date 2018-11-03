package service.bookie;

import com.mongodb.*;
import service.core.AuthInfo;
import service.core.BookieService;

import service.core.UserInfo;

import java.io.IOException;
import java.util.Date;

public class LocalBookieService implements BookieService {
   /*
   This is used to define whether the client have correct email and password
    */
   @Override
   public boolean login(AuthInfo authInfo) {

      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("usersdb");
      DBCollection table = db.getCollection("user");

      /**** Find whether the database has same info****/
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

   /*
   This is used to register and put user info into the database
    */
   @Override
   public AuthInfo registryUser(UserInfo userInfo) {

      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("usersdb");
      DBCollection table = db.getCollection("user");

      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("username", userInfo.getUsername());
      document.put("age", userInfo.getAge());
      document.put("email", userInfo.getEmail());
      document.put("password", userInfo.getPassword());
      table.insert(document);
      mongo.close();

      // Return the auth info
      AuthInfo newAuthInfo = new AuthInfo(userInfo.getEmail(), userInfo.getPassword());
      return newAuthInfo;
   }

   /*
   This is used to determine whether there are existing email in the database
    */
   @Override
   public boolean ifExist(UserInfo userInfo) {
      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("usersdb");
      DBCollection table = db.getCollection("user");

      /**** Find whether there are existing users****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", userInfo.getEmail());
      DBCursor cursor = table.find(searchQuery);

      while (cursor.hasNext()) {
         mongo.close();
         return true;
      }
      mongo.close();
      return false;
   }

}
