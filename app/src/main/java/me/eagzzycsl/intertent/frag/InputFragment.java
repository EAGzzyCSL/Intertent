package me.eagzzycsl.intertent.frag;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.List;

import me.eagzzycsl.intertent.R;


public class InputFragment extends Fragment {

    public InputFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.myFindView(view);
        this.myCreate();
        this.mySetView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private Button button_enable;
    private Button button_pick;

    private AlertDialog.Builder dialogBuilder;
    private int requestEnableInputMethodCode = 0;


    private void myFindView(View mView) {
        button_enable = (Button)mView.findViewById(R.id.button_enable);
        button_pick = (Button) mView.findViewById(R.id.button_pick);

    }


    private void mySetView() {
        button_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS),
                        requestEnableInputMethodCode);
            }
        });
        button_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imeManager =
                        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imeManager != null) {
                    imeManager.showInputMethodPicker();
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void myCreate() {
        dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.visitLinkInNoteBook))
                .setMessage(getString(R.string.webLink))
                .setPositiveButton(R.string.ok, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestEnableInputMethodCode) {
            if (isMyInputMethodEnabled()) {
                button_pick.setEnabled(true);

                button_pick.setBackgroundTintList(
                        ContextCompat.getColorStateList(
                                getContext(), R.color.button_tint));
            } else {
                button_pick.setEnabled(false);
            }

        }

    }

    private boolean isMyInputMethodEnabled() {
        InputMethodManager imeManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list = imeManager.getEnabledInputMethodList();
        for (InputMethodInfo i : list) {
            if (i.getPackageName().equals(getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
