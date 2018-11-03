package service.core;

import java.io.IOException;

public interface BookieService extends Service{
      public boolean login(AuthInfo authInfo) throws IOException;
      public AuthInfo registryUser(UserInfo userInfo) throws IOException;
      public boolean ifExist(UserInfo userInfo) throws IOException;
}
