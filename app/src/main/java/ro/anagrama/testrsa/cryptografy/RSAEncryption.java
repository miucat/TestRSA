package ro.anagrama.testrsa.cryptografy;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;


public class RSAEncryption {

    //    private static final int RSA_KEY_SIZE = 2048;
    public Key priv_key = null;

    public RSAEncryption() {
    }

    //	@Override
    public String Decrypt(String msg) {
        byte[] data_in = Base64.decode(msg, Base64.DEFAULT);
        try {
            /* Create the cipher */
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //load private key
            Key priv = priv_key;
            rsaCipher.init(Cipher.DECRYPT_MODE, priv);
            byte[] decriptedText = rsaCipher.doFinal(data_in);
//            int maxLength = RSA_KEY_SIZE / 8;
//            int dataLength = data_in.length;
//            int iterations = dataLength / maxLength + ((dataLength % maxLength != 0) ? 1 : 0);
//            byte[] decrypt_data = new byte[dataLength];
//            int len = 0;
//            for (int i = 0; i < iterations; i++) {
//                byte[] tempBytes = new byte[Math.min(dataLength - maxLength * i, maxLength)];
//                System.arraycopy(data_in, maxLength * i, tempBytes, 0, tempBytes.length);
//                byte[] decryptedBytes = rsaCipher.doFinal(tempBytes);
//                System.arraycopy(decryptedBytes, 0, decrypt_data, len, decryptedBytes.length);
//                len += decryptedBytes.length;
//            }
//            byte[] out_data = new byte[len];
//            System.arraycopy(decrypt_data, 0, out_data, 0, len);
            return new String(decriptedText, StandardCharsets.UTF_8);
        } catch (Exception ee) {
            Log.e("Exceptie:", ee.getMessage());
        }
        return null;
    }
}
