package ro.anagrama.testrsa;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;

import java.security.KeyPair;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.anagrama.testrsa.cryptografy.KeyUtils;
import ro.anagrama.testrsa.cryptografy.RSAEncryption;
import ro.anagrama.testrsa.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private KeyPair key;

//    static {
//        System.loadLibrary("chilkat");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         /*

            se vor importa fisierele din cryptografy
            +
            de adaugat in build.gradle (Module) implementation 'com.madgag.spongycastle:bctls-jdk15on:1.58.0.0' //pentru transformarea in format PEM

             */
        binding.tvSuccessCounter.setText("0");
        binding.tvErrCounter.setText("0");
        //create RSAKey only if it is not exists one
        if (KeyUtils.getRSAKey() == null) {
            key = KeyUtils.CreateKeyRSA();
        }

//            Log.e("PUBLIC:",KeyUtils.getRSAPublicKeyAsXMLString((RSAPublicKey) KeyUtils.getRSAPublicKey(KeyUtils.KeyNameRSA)));

        //show public key in PEM format
//            Log.e("PEMPublic: ", KeyUtils.toPEM(KeyUtils.getRSAPublicKey(KeyUtils.KeyNameRSA)));
        String epem = KeyUtils.toPEM(KeyUtils.getRSAPublicKey(KeyUtils.KeyNameRSA));
        if (epem != null && !epem.isEmpty())
            setPem(epem);
        else
            setPem("Nu sa putut genera o cheie publica");


        //create RSAEncryptionInstance
        RSAEncryption rsa = new RSAEncryption();

        rsa.priv_key = KeyUtils.getRSAKey();
        //set private key for decrypt; if need to encrypt will set public key
        //decrypt will result a string or null
//            String RSADecr = rsa.Decrypt("N9HTfm3tEMBAkU6dD0RPcnnohwvGd/JuOewfW83eu6gnfrn2nvF4sG1eHZTPXBps2WxUmsHSeL44xf6v8Cqx9ry4YpJMLglBqHQ8msHoq4JUVNf5Zi5KFXSN9tP8+hLHWsBsIAsuc+eAnx4KsvGgBjPpps5tXu39yhmhei4SknlpPH0lPbm9UxFOxDU+cqEambUglCY7ZZA3dNjWAMfoFTfcT/xY8KxALwq/HFv56qWakv2af9bk31f5+ZTphcI5qyRsDbVasGkftk8/cY/d5vUWz0k9V6vesPcFyS3x+562vU1h5nQqY822k8blAmqbZi5HHQxQZDV3tuoqA6yWZQ==");
//            Log.e("!Decrypted:", RSADecr);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://staging24pay.azurewebsites.net/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .build();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);

        binding.fab.setOnClickListener(view -> {
            getData(service, rsa, KeyUtils.toPEM(KeyUtils.getRSAPublicKey(KeyUtils.KeyNameRSA)));
        });
    }

    private void getData(RetrofitInterface service, RSAEncryption rsa, String s) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.loading.setVisibility(View.VISIBLE);
        Endpoint endpoint = new Endpoint();
        endpoint.setRsa(s);
        endpoint.setSms("264763");
        endpoint.setDeviceId("EDF65A094234211D");
        endpoint.setVersion("1.1.0.0");
        endpoint.setPhone("+40744333650");
        service
                .listTestData(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<String> data) {
                        int success = 0;
                        int error = 0;
                        String decriptat = "something went wrong";
                        Toast.makeText(MainActivity.this, "Date primite se proceseaza>> " + data.size() + "<<date", Toast.LENGTH_LONG).show();
                        for (String as : data) {
                            decriptat = rsa.Decrypt(as);
                            if (decriptat != null) {
                                success++;
                            } else {
                                error++;
                            }
                        }
                        setTextAfterParse(error, success, decriptat);

                        binding.loading.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.loading.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Eroare comunicare server", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }

                });

    }

    private OkHttpClient provideClient() {
//        PhoneInterceptor phoneInterceptor = new PhoneInterceptor();
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

    }

    public void setTextAfterParse(int error, int success, String code) {
        binding.tvSuccessCounter.setText(String.valueOf(success));
        binding.tvErrCounter.setText(String.valueOf(error));
        if (code != null)
            binding.codeDecompilat.setText(code);
        else
            binding.codeDecompilat.setText("o mare eroare");

    }

    public void setPem(String pem) {
        binding.pemKey.setText(pem);
    }

}