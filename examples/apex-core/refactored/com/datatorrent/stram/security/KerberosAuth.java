/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.stram.security;

import java.io.IOException;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * <p>KerberosAuth class.</p>
 *
 * @since 0.3.2
 */
public class KerberosAuth
{

  public static Subject loginUser(String principal, char[] password) throws LoginException, IOException
  {
    Subject subject = new Subject();
    LoginContext lc = new LoginContext(KerberosAuth.class.getName(), subject, new AuthenticationHandler(principal, password), new KerberosConfiguration(principal));
    lc.login();
    return subject;
    //return UserGroupInformation.getUGIFromTicketCache(ticketCache, principal);
  }

  private static class AuthenticationHandler implements CallbackHandler
  {

    private final String principal;
    private final char[] password;

    AuthenticationHandler(String principal, char[] password)
    {
      this.principal = principal;
      this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
      for (Callback callback : callbacks) {
        if (callback instanceof NameCallback) {
          handleNameCallback((NameCallback) callback);
        } else if (callback instanceof PasswordCallback) {
          handlePasswordCallback((PasswordCallback) callback);
        }
      }
    }

    private void handleNameCallback(NameCallback nameCallback)
    {
      nameCallback.setName(principal);
    }

    private void handlePasswordCallback(PasswordCallback passwordCallback)
    {
      passwordCallback.setPassword(password);
    }
  }

  private static class KerberosConfiguration extends Configuration
  {

    private final String principal;

    KerberosConfiguration(String principal)
    {
      this.principal = principal;
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name)
    {
      if (KerberosAuth.class.getName().equals(name)) {
        return createAppConfigurationEntries();
      } else {
        return null;
      }
    }

    private AppConfigurationEntry[] createAppConfigurationEntries()
    {
      AppConfigurationEntry[] configEntries = new AppConfigurationEntry[1];
      HashMap<String, String> params = createParams();
      configEntries[0] = new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
          LoginModuleControlFlag.REQUIRED, params);
      return configEntries;
    }

    private HashMap<String, String> createParams()
    {
      HashMap<String, String> params = new HashMap<>();
      params.put("useTicketCache", "true");
      params.put("principal", principal);
      return params;
    }
  }
}