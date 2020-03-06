package app.com.customviews.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import app.com.customviews.R;
import app.com.customviews.classes.ObservableCharacter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObserverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ObserverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObserverFragment extends Fragment {

    public static final String PARAM =  "param";
    private ObservableCharacter value;
    private Observer valueChanged = new Observer() {
        @Override
        public void update(Observable observable, Object newValue) {
            Toast.makeText(getContext(), ObserverFragment.class.getSimpleName() + "value has changed, new value: " + newValue, Toast.LENGTH_SHORT).show();
        }
    };


    public ObserverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            value = (ObservableCharacter) getArguments().getSerializable(PARAM);
            value.addObserver(valueChanged);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_observer, container, false);
    }

}
