package service.core;

public class BetInfo {

   private float amount;
   private String acountEmail;
   private String uniqueBetID;

   public BetInfo(float amount, String acountEmail) {
      this.amount = amount;
      this.acountEmail = acountEmail;
      uniqueBetID = AuthInfo.getRandomString(10);
   }

   public float getAmount() {
      return amount;
   }

   public String getAcountEmail() {
      return acountEmail;
   }

   public String getUniqueBetID() {
      return uniqueBetID;
   }
}
