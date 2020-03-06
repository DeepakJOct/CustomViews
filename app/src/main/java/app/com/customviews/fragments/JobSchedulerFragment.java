package app.com.customviews.fragments;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Objects;

import app.com.customviews.R;
import app.com.customviews.classes.NotificationJobService;
import app.com.customviews.utils.CommonUtils;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobSchedulerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobSchedulerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobSchedulerFragment extends Fragment implements View.OnClickListener {

    private RadioButton rbNone, rbAny, rbWifi;
    private RadioGroup networkOptions;
    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;
    private SeekBar mSeekBar;
    private TextView progress;
    private Switch chargingSwitch;
    private Switch idleSwitch;
    private Button btnStartJob, btnStopJob;

    private OnFragmentInteractionListener mListener;
    private Context context;

    public JobSchedulerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JobSchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobSchedulerFragment newInstance(Context context) {
        JobSchedulerFragment fragment = new JobSchedulerFragment();
        fragment.context = context;
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
        return inflater.inflate(R.layout.fragment_job_scheduler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rbNone = view.findViewById(R.id.rb_none);
        rbAny = view.findViewById(R.id.rb_any);
        rbWifi = view.findViewById(R.id.rb_wifi);
        networkOptions = view.findViewById(R.id.rg_network_options);
        Context context;
        mScheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
        mSeekBar = view.findViewById(R.id.seekbar);
        progress = view.findViewById(R.id.tv_deadline);
        chargingSwitch = view.findViewById(R.id.sw_charging);
        idleSwitch = view.findViewById(R.id.sw_idle);
        btnStartJob = view.findViewById(R.id.btn_schedule);
        btnStopJob = view.findViewById(R.id.btn_cancel);
        btnStartJob.setOnClickListener(this);
        btnStopJob.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i > 0) {
                    progress.setText("Override Deadline: " + i + "s");
                } else {
                    progress.setText("Override Deadline: " + "Not set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void scheduleJob() {
        int selectedNetworkId = networkOptions.getCheckedRadioButtonId();
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;

        switch (selectedNetworkId) {
            case R.id.rb_none:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
            case R.id.rb_any:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
            case R.id.rb_wifi:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
        }

        ComponentName serviceName = new ComponentName(context.getPackageName(), NotificationJobService.class.getName());
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID, serviceName);
        jobBuilder.setRequiredNetworkType(selectedNetworkOption);

        int seekBarIngteger = mSeekBar.getProgress();

        boolean seekbarSet = seekBarIngteger > 0;

        if(seekbarSet) {
            jobBuilder.setOverrideDeadline(seekBarIngteger * 1000);
        }

        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
                || chargingSwitch.isChecked() || idleSwitch.isChecked()
                || seekbarSet;

        if(constraintSet) {
            //schedule the job and notify the user
            JobInfo myJobInfo = jobBuilder.build();
            mScheduler.schedule(myJobInfo);

            CommonUtils.showToast(getActivity(), "Job scheduled, job will run when " + "the constraints are met.");
        } else {
            CommonUtils.showToast(getActivity(), "Please set at least one consent for the purpose of the please set at least on consent when the constraints are met.");
        }



    }

    private void cancelJobs() {
        if(mScheduler != null) {
            mScheduler.cancelAll();
            mScheduler = null;
            CommonUtils.showToast(getActivity(), "Jobs cancelled");
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
    public void onClick(View view) {
        if(view == btnStartJob) {
            scheduleJob();
        } else if(view == btnStopJob) {
            cancelJobs();
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
        void onJobSchedulerFragmentInteraction();
    }
}
