package service.core;

import java.io.IOException;

public interface BookieService extends Service{
   public boolean login(AuthInfo authInfo) throws IOException;
   public void registryUser(UserInfo userInfo) throws IOException;
   public boolean ifExist(UserInfo userInfo) throws IOException;
   public boolean bet(BetInfo betInfo) throws IOException;
   public int getCurrentBalance(AuthInfo authInfo) throws IOException;
}
