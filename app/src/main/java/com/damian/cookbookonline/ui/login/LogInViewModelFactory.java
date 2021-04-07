package com.damian.cookbookonline.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.damian.cookbookonline.data.LoginDataSource;
import com.damian.cookbookonline.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LogInViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(LoginRepository.getInstance(new LoginDataSource()));
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}