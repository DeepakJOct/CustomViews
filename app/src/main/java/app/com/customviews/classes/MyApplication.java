package app.com.customviews.classes;

import android.util.Log;
import android.webkit.URLUtil;

import androidx.multidex.MultiDexApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.com.customviews.interfaces.CustomViewsAppService;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends MultiDexApplication {

    private static Retrofit retrofit;
    private static CustomViewsAppService customViewsAppService;

    public static final String URL = "https://jsonplaceholder.typicode.com/";
    public static final String URL1 = "http://dummy.restapiexample.com/api/v1/";

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public static CustomViewsAppService create() {
        if(customViewsAppService == null) {
            customViewsAppService = getClient(URL).create(CustomViewsAppService.class);
        }
        return customViewsAppService;
    }

    //creating the interceptor to form a http client and http request is then built.
    //creating a retrofit object with the formed client and the return the retrofit object.
    public static Retrofit getClient(final String url) {
        if(retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();

                    builder.header("Language", "en");
                    builder.method(original.method(), original.body());
                    Request request = builder.build();
                    return chain.proceed(request);
                }
            }).readTimeout(240, TimeUnit.SECONDS)
                    .connectTimeout(240, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Log.d("MyApplication-->URL-->", url);

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //after request is processed, we get the json data present in the body using RequestBody
    public static RequestBody getRequestBody(Map request) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(request).toString()));
    }
}
