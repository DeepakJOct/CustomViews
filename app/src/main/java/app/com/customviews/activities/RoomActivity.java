package app.com.customviews.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import app.com.customviews.R;
import app.com.customviews.adapters.WordListAdapter;
import app.com.customviews.classes.Word;
import app.com.customviews.models.WordViewModel;

public class RoomActivity extends AppCompatActivity {

    private WordViewModel mWordViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        mWordViewModel.getAllWords().observe(this, words -> adapter.setWords(words));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RoomActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
    *
    * The components of the app are:


    * RoomActivity: displays words in a list using a RecyclerView and the WordListAdapter. In
    * RoomActivity, there is an Observer that observes the words LiveData from the database and is notified when they change.

    * NewWordActivity: adds a new word to the list.

    * WordViewModel: provides methods for accessing the data layer, and it returns LiveData so
    * that MainActivity can set up the observer relationship.*

    * LiveData<List<Word>>: Makes possible the automatic updates in the UI components.
    * In the MainActivity, there is an Observer that observes the words LiveData from the database and is notified when they change.

    * Repository: manages one or more data sources. The Repository exposes methods for the
    * ViewModel to interact with the underlying data provider. In this app, that backend is a Room database.

    * Room: is a wrapper around and implements a SQLite database. Room does a lot of work for you that you used to have to do yourself.

    * DAO: maps method calls to database queries, so that when the Repository calls a method
    * such as getAlphabetizedWords(), Room can execute SELECT * from word_table ORDER BY word ASC.

    * Word: is the entity class that contains a single word.
    *
    * */
}
