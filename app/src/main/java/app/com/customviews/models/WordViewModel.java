package app.com.customviews.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.com.customviews.classes.Word;
import app.com.customviews.classes.WordRepository;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mWordRepository;

    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mWordRepository = new WordRepository(application);
        mAllWords = mWordRepository.getAllWords();

    }

    public LiveData<List<Word>> getAllWords() {return mAllWords;}

    public void insert(Word word) {mWordRepository.insert(word);}

}
