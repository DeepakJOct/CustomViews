package app.com.customviews.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CustomViewsAppService {
    /**
     * Gets login response.
     *
     *
     * @return the login response
     */
    @GET("comments")
    Call<ResponseBody> getCommentsResponse();

    @GET("posts")
    Call<ResponseBody> getPostsResponse();

    @POST("employees")
    Call<ResponseBody> getEmployees();
}
