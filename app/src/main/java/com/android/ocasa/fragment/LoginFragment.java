package com.android.ocasa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.activity.ForgotPassWordActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.service.UserService;
import com.android.ocasa.sync.LoginService;
import com.android.ocasa.util.KeyboardUtil;

/**
 * Created by ignacio on 21/01/16.
 */
public class LoginFragment extends BaseFragment {

    private RelativeLayout container;
    private EditText userText;
    private EditText passwordText;
    private Button loginButton;
    private TextView forgotPasswordButton;
    private ProgressBar progress;

    private LoginCallback callback;

    private LoginReceiver receiver;
    private IntentFilter filter;

    public interface LoginCallback{
        public void onLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (LoginCallback) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement LoginCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        receiver = new LoginReceiver();
        filter = new IntentFilter(UserService.USER_LOGIN_FINISHED_ACTION);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void initControls(View view){
        container = (RelativeLayout) view.findViewById(R.id.login_container);
        userText = (EditText) view.findViewById(R.id.login_user);
        passwordText = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.login_button);
        forgotPasswordButton = (TextView) view.findViewById(R.id.forgot_password_button);
        progress = (ProgressBar) view.findViewById(R.id.progress);
    }

    private void setListeners(){

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    doLogin();

                    return true;
                }

                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).startNewActivity(new Intent(getActivity(), ForgotPassWordActivity.class));
            }
        });
    }

    private void doLogin(){
        KeyboardUtil.hideKeyboard(getActivity());

        if(validateFields())
            login();
    }

    private boolean validateFields(){

        if(userText.getText().toString().trim().length() == 0){
            userText.setError(getString(R.string.login_empty_user_message));
            return false;
        }

        if(passwordText.getText().toString().trim().length() == 0){
            passwordText.setError(getString(R.string.login_empty_password_message));
            return false;
        }

        return true;
    }

    private void login(){

        hideContainer();

        getActivity().startService(new Intent(getActivity(), LoginService.class));
    }

    private void hideContainer(){
        progress.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_in));
        container.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_out));

        progress.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void showContainer(){
        progress.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_out));
        container.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_in));

        progress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    public class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase(UserService.USER_LOGIN_FINISHED_ACTION))
                callback.onLogin();
        }
    }

}
