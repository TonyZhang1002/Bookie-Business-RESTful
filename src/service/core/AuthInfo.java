package service.core;

import java.util.Random;

public class AuthInfo {

   private String email;
   private String password;
   private String uniqueID;

   public AuthInfo(String email, String password) {
      this.email = email;
      this.password = password;
      uniqueID = getRandomString(10);
   }

   public AuthInfo() {}

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   public String getUniqueID() {
      return uniqueID;
   }

   /*
      Use to generate random uniqueID
       */
   public static String getRandomString(int length) {
      String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      Random random = new Random();
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < length; ++i) {
         //int number = random.nextInt(62);// [0,62)
         sb.append(str.charAt(random.nextInt(62)));
      }
      return sb.toString();
   }
}
