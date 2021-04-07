package com.damian.cookbookonline.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.damian.cookbookonline.data.LoginRepository;
import com.damian.cookbookonline.data.Result;
import com.damian.cookbookonline.data.model.LoggedInUser;
import com.damian.cookbookonline.R;

public class LogInViewModel extends ViewModel {

    private MutableLiveData<LogInFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LogInResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LogInViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LogInFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LogInResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if(result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LogInResult(new LoggedInUserView(data.getDisplayName())));
        }
        else {
            loginResult.setValue(new LogInResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if(!isUserNameValid(username))
            loginFormState.setValue(new LogInFormState(R.string.invalid_username, null));
        else if(!isPasswordValid(password))
                loginFormState.setValue(new LogInFormState(null, R.string.invalid_password));
            else loginFormState.setValue(new LogInFormState(true));
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if(username == null)
            return false;

        if(username.contains("@"))
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        else return !username.trim().isEmpty();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}