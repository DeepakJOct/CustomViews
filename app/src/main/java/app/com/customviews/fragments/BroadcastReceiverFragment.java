package app.com.customviews.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import app.com.customviews.R;
import app.com.customviews.classes.NetworkChangeReceiver;
import app.com.customviews.interfaces.DataListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BroadcastReceiverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BroadcastReceiverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BroadcastReceiverFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    static TextView tvConnectivity;
    private NetworkChangeReceiver networkChangeReceiver;
    static TextView tvMsg;
    static boolean isPermissionGranted;

    public BroadcastReceiverFragment() {
        // Required empty public constructor
    }

    public static BroadcastReceiverFragment newInstance() {
        BroadcastReceiverFragment fragment = new BroadcastReceiverFragment();
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
        return inflater.inflate(R.layout.fragment_broadcast_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvConnectivity = view.findViewById(R.id.tv_connectivity);
        tvMsg = view.findViewById(R.id.tv_sms);
        networkChangeReceiver = new NetworkChangeReceiver();
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.RECEIVE_SMS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
        registerNetworkBroadcastForNoughat();

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @SuppressLint("SetTextI18n")
    public static void dialog(boolean value) {
        if (value) {
            tvConnectivity.setText("Yay! We are back online.");
            tvConnectivity.setBackgroundColor(Color.GREEN);
            tvConnectivity.setTextColor(Color.WHITE);

            Handler h = new Handler();
            Runnable delayRunnable = new Runnable() {
                @Override
                public void run() {
                    tvConnectivity.setVisibility(View.GONE);
                }
            };
            h.postDelayed(delayRunnable, 3000);
        } else {
            tvConnectivity.setVisibility(View.VISIBLE);
            tvConnectivity.setText("Couldn't connect to internet");
            tvConnectivity.setBackgroundColor(Color.RED);
            tvConnectivity.setTextColor(Color.WHITE);
        }
    }

    public static void getDataFromReceiver(String data) {
        if(isPermissionGranted) {
            if(data != null) {
                tvMsg.setText(data + "");
            }
        }
    }

    private void registerNetworkBroadcastForNoughat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(networkChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
