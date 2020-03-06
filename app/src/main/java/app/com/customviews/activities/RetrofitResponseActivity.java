package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.com.customviews.R;
import app.com.customviews.adapters.CommentsAdapter;
import app.com.customviews.interfaces.ResultListener;
import app.com.customviews.models.Comment;
import app.com.customviews.utils.CommonOperations;
import app.com.customviews.views.CustomFontTextView;

public class RetrofitResponseActivity extends AppCompatActivity {

    Button btnGetResponse;
    CustomFontTextView tvResponse;
    private List<Comment> commentsList;
    private RecyclerView rcvComments;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_response);

        btnGetResponse = findViewById(R.id.btn_get_response);
//        tvResponse = findViewById(R.id.tv_response);
        rcvComments = findViewById(R.id.rcv_comments);

        btnGetResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOperations.newInstance().getCommentsResponse(RetrofitResponseActivity.this, new ResultListener() {
                    @Override
                    public void getResult(Object object, boolean isSuccess) {
                        if (isSuccess) {
                            commentsList = (ArrayList<Comment>) object;
                            Log.d("CommentsList-->", commentsList.toString());
                            commentsAdapter = new CommentsAdapter(RetrofitResponseActivity.this, commentsList);
                            rcvComments.setLayoutManager(new LinearLayoutManager(RetrofitResponseActivity.this));
                            rcvComments.setAdapter(commentsAdapter);
                        }
                    }
                });

                /*CommonOperations.newInstance().getPostsResponse(RetrofitResponseActivity.this, new ResultListener() {
                    @Override
                    public void getResult(Object object, boolean isSuccess) {
                        Log.d("Activity-->ResObj1-->", object.toString());
                    }
                });*/

                /*CommonOperations.newInstance().getAllEmployees(RetrofitResponseActivity.this, new ResultListener() {
                    @Override
                    public void getResult(Object object, boolean isSuccess) {
                        if (isSuccess) {
                            Log.d("Activity-->ResObj1-->", object.toString());
                        }
                    }
                });*/
            }
        });

    }

    private void setRecyclerView(Context context, ArrayList<Comment> commentsList) {
        commentsAdapter = new CommentsAdapter(context, commentsList);
        rcvComments.setLayoutManager(new LinearLayoutManager(context));
        rcvComments.setAdapter(commentsAdapter);
    }
}
