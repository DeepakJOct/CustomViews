package app.com.customviews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.customviews.R;
import app.com.customviews.classes.Word;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Word> mWords;

    public WordListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
        } else {
            holder.wordItemView.setText("No Word");
        }

    }

    @Override
    public int getItemCount() {
        if (mWords != null) {
            return mWords.size();
        } else {
            return 0;
        }
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        private final TextView wordItemView;


        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            this.wordItemView = itemView.findViewById(R.id.textView);
        }

    }

    public void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }
}
