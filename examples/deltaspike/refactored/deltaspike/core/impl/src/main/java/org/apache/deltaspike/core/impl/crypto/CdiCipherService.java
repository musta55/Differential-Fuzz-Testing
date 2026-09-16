package org.apache.deltaspike.core.impl.crypto;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import org.apache.deltaspike.core.api.crypto.CipherService;

@ApplicationScoped
public class CdiCipherService implements CipherService
{
    private DefaultCipherService cipherService = new DefaultCipherService();

    @Override
    public void setMasterHash(String masterPassword, String masterSalt, boolean overwrite) throws IOException
    {
        cipherService.setMasterHash(masterPassword, masterSalt, overwrite);
    }

    @Override
    public String encrypt(String cleartext, String masterSalt)
    {
        return cipherService.encrypt(cleartext, masterSalt);
    }

    @Override
    public String decrypt(String encryptedValue, String masterSalt)
    {
        return cipherService.decrypt(encryptedValue, masterSalt);
    }
}