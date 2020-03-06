package app.com.customviews.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.customviews.R;
import app.com.customviews.models.Comment;
import app.com.customviews.views.CustomFontTextView;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentsList;

    public CommentsAdapter(Context context,
                           List<Comment> commentList) {
        this.context = context;
        this.commentsList = commentList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(commentsList != null) {
            Log.d("CommentsListAdapter-->", commentsList.toArray() + "");
            final Comment comment = commentsList.get(position);
            Log.d("CommentOne-->", comment.toString());
            holder.tvId.setText(comment.getId() + "");
            holder.tvName.setText(comment.getName());
            holder.tvEmail.setText(comment.getEmail());
            holder.tvComment.setText(comment.getBody());
        } else {
            Toast.makeText(context, "List is null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomFontTextView tvId;
        CustomFontTextView tvName;
        CustomFontTextView tvEmail;
        CustomFontTextView tvComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.id_email);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
