package service.core;

public class UserInfo {

   private String username;
   private int age;
   private float balance;
   private String password;
   private String email;

   public UserInfo(String username, int age, String password, String email) {
      this.username = username;
      this.age = age;
      this.password = password;
      this.email = email;
   }

   public UserInfo() {}

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   public String getUsername() {
      return username;
   }

   public int getAge() {
      return age;
   }

   public float getBalance() {
      return balance;
   }

   public float changeBalance (float a) {
      this.balance += a;
      return balance;
   }
}
