package ro.anagrama.testrsa.cryptografy;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import org.spongycastle.openssl.jcajce.JcaPEMWriter;

import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class KeyUtils {
    private static final String TAG = "KeyUtils";

    public static final String KeyNameRSA = "my_special_key_rsa2";

    public static Key getRSAKey() {
        Key key = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            try {
                KeyStore.Entry entry = keyStore.getEntry(KeyNameRSA, null);
//                if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
//                    Log.w(TAG, "Not an instance of a PrivateKeyEntry");
//                    return null;
//                }
                //get private key
                key = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
                Log.d(TAG, "getRSAKey: entry");
            } catch (NullPointerException ee) {
                //get private key
                Log.d(TAG, "getRSAKey: key");
                key = keyStore.getKey(KeyNameRSA, null);
            }

        } catch (Exception ee) {
            Log.w(TAG, "getRSAKey error: " + ee);
        }
        return key;
    }

    public static PublicKey getRSAPublicKey(String alias) {
        PublicKey key = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            //get public key
            Certificate cert = keyStore.getCertificate(alias);
            key = cert.getPublicKey();
        } catch (Exception ee) {
            Log.w(TAG, "getRSAKey error: " + ee);
        }
        return key;
    }


    public static KeyPair CreateKeyRSA() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            kpg.initialize(new KeyGenParameterSpec.Builder(
                    KeyNameRSA,
                    KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setRandomizedEncryptionRequired(false)
                    .setDigests(
                            KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5,
                            KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224,
                            KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384,
                            KeyProperties.DIGEST_SHA512)
                    .setKeySize(2048)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_NONE,
                            KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
                            KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .build());

            kp = kpg.generateKeyPair();
        } catch (Exception ee) {
            Log.w(TAG, "CreateKeyRSA error: " + ee);
        }
        return kp;
    }

    public static String toPEM(PublicKey pk) {
        try {
            StringWriter sw = new StringWriter();
            JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
            pemWriter.writeObject(pk);
            pemWriter.close();
            return sw.toString();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }
}
