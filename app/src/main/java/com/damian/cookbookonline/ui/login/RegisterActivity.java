package com.damian.cookbookonline.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.damian.cookbookonline.R;

public class RegisterActivity extends AppCompatActivity {

    private LogInViewModel loginViewModel;
    TextView textViewLogInRecommendation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginViewModel = new ViewModelProvider(this, new LogInViewModelFactory()).get(LogInViewModel.class);

        final EditText editTextUsername = findViewById(R.id.username);
        final EditText editTextPassword = findViewById(R.id.password);
        final Button buttonRegister = findViewById(R.id.buttonRegister);
        final ProgressBar progressBarLoading = findViewById(R.id.loading);
        textViewLogInRecommendation = findViewById(R.id.textViewLogInRecommendation);

        textViewLogInRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        loginViewModel.getLoginFormState().observe(this, new Observer<LogInFormState>() {
            @Override
            public void onChanged(@Nullable LogInFormState loginFormState) {
                if(loginFormState == null) {
                    return;
                }
                buttonRegister.setEnabled(loginFormState.isDataValid());
                if(loginFormState.getUsernameError() != null) {
                    editTextUsername.setError(getString(loginFormState.getUsernameError()));
                }
                if(loginFormState.getPasswordError() != null) {
                    editTextPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LogInResult>() {
            @Override
            public void onChanged(@Nullable LogInResult loginResult) {
                if(loginResult == null) {
                    return;
                }
                progressBarLoading.setVisibility(View.GONE);
                if(loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if(loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            }
        };
        editTextUsername.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                }
                return false;
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLoading.setVisibility(View.VISIBLE);
                loginViewModel.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}