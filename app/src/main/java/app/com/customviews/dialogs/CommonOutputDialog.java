package app.com.customviews.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.concurrent.CopyOnWriteArrayList;

import app.com.customviews.R;
import app.com.customviews.interfaces.ResultListener;
import app.com.customviews.views.AutofitTextView;
import app.com.customviews.views.CustomFontTextView;
import app.com.customviews.views.CustomImageView;

public class CommonOutputDialog extends DialogFragment implements View.OnClickListener{

    private AutofitTextView tvTitle;
    private CustomImageView closeBtn;
    private CustomImageView imvCross;
    private CustomFontTextView tvFinalMessageCredit;
    private CustomFontTextView tvOk;
    private CustomFontTextView tvGotIt;
    private ResultListener listener;
    private String outputMessage;

    public static CommonOutputDialog newInstance(String outputMessage, ResultListener listener) {
        CommonOutputDialog CODialog = new CommonOutputDialog();
        CODialog.outputMessage = outputMessage;
        CODialog.listener = listener;
        return CODialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppCompatAlertDialogStyle);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_common_output, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        closeBtn = view.findViewById(R.id.close_btn);
        imvCross = view.findViewById(R.id.imv_cross);
        tvFinalMessageCredit = view.findViewById(R.id.tv_final_message_credit);
        tvOk = view.findViewById(R.id.tv_ok);
        tvGotIt = view.findViewById(R.id.tv_got_it);
        setClickListeners();
        setValuesToViews();


    }

    void setClickListeners() {
        closeBtn.setOnClickListener(this);
        imvCross.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        tvGotIt.setOnClickListener(this);
    }

    void setValuesToViews() {
        tvTitle.setText("Output Dialog");
        if(outputMessage != null) {
            tvFinalMessageCredit.setText(outputMessage);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == closeBtn || view == imvCross) {
            dismiss();
        } else if(view == tvOk) {
            dismiss();
            listener.getResult(true, true);
        } else if(view == tvGotIt) {
            dismiss();
        } else {
            dismiss();
        }
    }
}
