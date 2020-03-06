package app.com.customviews.fragments;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.customviews.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawableTransitionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawableTransitionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawableTransitionFragment extends Fragment {

    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;
    private int counter = 0;
    ImageView iv;
    Animatable animatable;
    TextView tv;
    Button btnStart;
    boolean isStart = true;

    public DrawableTransitionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DrawableTransitionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawableTransitionFragment newInstance() {
        DrawableTransitionFragment fragment = new DrawableTransitionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawable_transition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv = view.findViewById(R.id.animated_drawable);
        tv = view.findViewById(R.id.tv_times);
        tv.setText("Times: " + counter);
        animatable = (Animatable) iv.getDrawable();

        btnStart = view.findViewById(R.id.start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStart) {
                    animationRepeator.run();
                    btnStart.setText("Stop");
                } else {
                    animatable.stop();
                    handler.removeCallbacks(animationRepeator);
                    btnStart.setText("Start");
                }
                isStart = !isStart;
            }
        });
    }

    Handler handler = new Handler();
    Runnable animationRepeator = new Runnable() {
        @Override
        public void run() {
            try {
                animatable.start();
            } finally {
                handler.postDelayed(animationRepeator, 0200);
                counter+=1;
                tv.setText("Times: " + counter);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDrawableAnimationFragInteraction(Uri uri);
    }
}
