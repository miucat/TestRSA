package ro.anagrama.testrsa;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("test1000")
    Observable<List<String>> listTestData(@Body Endpoint endpoint);
}
