package app.com.customviews.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.com.customviews.R;
import app.com.customviews.utils.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {
    private SensorManager sm;
    private TextView textView;
    List<Sensor> list;
    RelativeLayout rlSensorLayout;

    private OnFragmentInteractionListener mListener;

    public SensorFragment() {
        // Required empty public constructor
    }

    SensorEventListener sel = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            textView.setText("x:" + values[0] + "\ny:" + values[1] + "\nz:" + values[2]);
            if(values[0] > 3 && values[0] < 6) {
                rlSensorLayout.setBackgroundColor(getResources().getColor(R.color.green));
            } else if(values[0] > 6 && values[0] < 9) {
                rlSensorLayout.setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (values[0] < 0 && values[0] > -3){
                rlSensorLayout.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (values[0] < -6 && values[0] > -9){
                rlSensorLayout.setBackgroundColor(getResources().getColor(R.color.blue_dark));
            } else {
                rlSensorLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }

            if(values[1] > 3 && values[1] < 6) {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#8a2f46"));
            } else if(values[1] > 6 && values[1] < 9) {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#04ba62"));
            } else if (values[1] < 0 && values[1] > -3){
                rlSensorLayout.setBackgroundColor(Color.parseColor("#c95047"));
            } else if (values[1] < -3 && values[1] > -9){
                rlSensorLayout.setBackgroundColor(Color.parseColor("#bf9334"));
            } else {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#001752"));
            }

            if(values[2] > 3 && values[2] < 6) {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#df03fc"));
            } else if(values[2] > 6 && values[2] < 9) {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#fc038c"));
            } else if (values[2] < 0 && values[2] > -3){
                rlSensorLayout.setBackgroundColor(Color.parseColor("#356e70"));
            } else if (values[2] < -3 && values[2] > -9){
                rlSensorLayout.setBackgroundColor(Color.parseColor("#2c6b0d"));
            } else {
                rlSensorLayout.setBackgroundColor(Color.parseColor("#5c0c06"));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SensorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance() {
        SensorFragment fragment = new SensorFragment();
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
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        textView = view.findViewById(R.id.tv_sensor_values);
        rlSensorLayout = view.findViewById(R.id.rl_sensorlayout);
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        /*sel = new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                textView.setText("x:" + values[0] + "\ny:" + values[1] + "\nz:" + values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };*/

        if(list.size() > 0) {
            sm.registerListener(sel, list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            CommonUtils.showToast(getActivity(), "Error: No Accelerometer");
        }

    }

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

    @Override
    public void onStop() {
        if(list.size() > 0) {
            sm.unregisterListener(sel);
        }
        super.onStop();
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
        void onSensorFragmentInteraction(Uri uri);
    }
}
