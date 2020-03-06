package app.com.customviews.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.customviews.classes.MyApplication;
import app.com.customviews.interfaces.ResultListener;
import app.com.customviews.models.Comment;
import app.com.customviews.retrofit.Listener;
import app.com.customviews.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CommonOperations {


    private static final CommonOperations ourInstance = new CommonOperations();

    public static CommonOperations newInstance() {return ourInstance;}

    //required empty public constructor
    private CommonOperations() {

    }

    public void getCommentsResponse(Context context, ResultListener resultListener) {
        if(CommonUtils.checkNetworkConnection(context)) {
            //Create an object of Call<ResponseBody>
            Call<ResponseBody> responseBodyCall;
            responseBodyCall = MyApplication.create().getCommentsResponse();

            //require a callback
            responseBodyCall.enqueue(new Listener(new RetrofitService() {
                @Override
                public void onSuccess(String result, int pos, Throwable t) {
                    Log.d("Response--> Comments-->", "Result--> " + result);
                    parseCommentsResponse(context, result, resultListener);
                }
            }, null, true, context));
        } else {
            Toast.makeText(context, "We couldn't get response from the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void parseCommentsResponse(final Context context, String result, ResultListener resultListener) {
        try {
            JSONArray objectList = new JSONArray(result);
            List<Comment> resultList = new ArrayList<>();
            for (int i = 0; i < objectList.length(); i++) {
                JSONObject object = objectList.getJSONObject(i);
                Comment c = new Comment();
                c.setId(object.getInt("id"));
                c.setName(object.getString("name"));
                c.setEmail(object.getString("email"));
                c.setBody(object.getString("body"));
                resultList.add(c);
            }
            resultListener.getResult(resultList, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPostsResponse(Context context, ResultListener resultListener) {
        if(CommonUtils.checkNetworkConnection(context)) {
            //Create an object of Call<ResponseBody>
            Call<ResponseBody> responseBodyCall;
            responseBodyCall = MyApplication.create().getPostsResponse();

            //require a callback
            responseBodyCall.enqueue(new Listener(new RetrofitService() {
                @Override
                public void onSuccess(String result, int pos, Throwable t) {
                    Log.d("Response--> Comments-->", "Result--> " + result);
                    resultListener.getResult(result, true);
                }
            }, null, true, context));

        }
    }

    public void getAllEmployees(Context context, ResultListener resultListener) {
        if(CommonUtils.checkNetworkConnection(context)) {
            //Create an object of Call<ResponseBody>
            Call<ResponseBody> responseBodyCall;
            responseBodyCall = MyApplication.create().getEmployees();

            //require a callback
            responseBodyCall.enqueue(new Listener(new RetrofitService() {
                @Override
                public void onSuccess(String result, int pos, Throwable t) {
                    Log.d("Response--> Comments-->", "Result--> " + result);
                    resultListener.getResult(result, true);
                }
            }, null, true, context));

        }
    }
}
