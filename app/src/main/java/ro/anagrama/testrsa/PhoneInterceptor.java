package ro.anagrama.testrsa;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneInterceptor implements Interceptor {
    private static final String TAG = PhoneInterceptor.class.getSimpleName();

    public PhoneInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        if (url.pathSegments().size() != 0) {

        }


        return chain.proceed(request);

    }


}
