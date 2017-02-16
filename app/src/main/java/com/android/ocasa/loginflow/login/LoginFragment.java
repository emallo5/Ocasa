package com.android.ocasa.loginflow.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.loginflow.password.ForgotPassWordActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.util.KeyboardUtil;
import com.codika.androidmvp.fragment.BaseMvpFragment;

/**
 * Created by ignacio on 21/01/16.
 */
public class LoginFragment extends BaseMvpFragment<LoginView, LoginPresenter> implements LoginView{

    static final int REQUEST_READ_PHONE_STATE = 1000;
    static final int REQUEST_WRITE_STORAGE = 1001;

    private RelativeLayout container;
    private EditText userText;
    private EditText passwordText;
    private Button loginButton;
    private TextView forgotPasswordButton;
    private ProgressBar progress;

    private LoginCallback callback;

    private String deviceId;

    public interface LoginCallback{
        void onLoginSucces();
        void onLoggedUser();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SessionManager.getInstance().isSigned()){
            callback.onLoggedUser();
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
    public void onStart() {
        super.onStart();
        loadImei();
    }

    private void loadImei() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (mngr.getDeviceId() != null){
                deviceId = mngr.getDeviceId();
            }else{
                deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_READ_PHONE_STATE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImei();
            }
        }

        if(requestCode == REQUEST_WRITE_STORAGE){
            if (grantResults.length > 0
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "No podrá crearse el Log", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public LoginView getMvpView() {
        return this;
    }

    @Override
    public Loader<LoginPresenter> getPresenterLoader() {
        return new LoginLoader(getActivity());
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

        loginButton.setEnabled(false);

        String mail = userText.getText().toString();
        String password = passwordText.getText().toString();

        getPresenter().login(mail, password, deviceId);
    }

    @Override
    public void showEmptyFieldsAlert() {
        userText.setError(getString(R.string.login_empty_user_message));
        passwordText.setError(getString(R.string.login_empty_user_message));

        loginButton.setEnabled(true);
    }

    @Override
    public void showInvalidEmail() {
        userText.setError("Email invalido");

        loginButton.setEnabled(true);
    }

    @Override
    public void showProgress() {
        hideContainer();
    }

    @Override
    public void onLoginSuccess() {
        callback.onLoginSucces();
    }

    @Override
    public void onLoginError(String error) {
        showContainer();

        loginButton.setEnabled(true);

        Snackbar.make(getView(), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkError() {
        showContainer();

        loginButton.setEnabled(true);

        Snackbar.make(getView(), "Error de conexión", Snackbar.LENGTH_INDEFINITE)
                .setAction("Reintentar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doLogin();
                    }
                }).show();
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

}
