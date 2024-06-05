package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.AuthRepositoryImpl;
import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.AuthRepository;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;

import io.reactivex.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final AuthRepository repository = new AuthRepositoryImpl();
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public LiveData<User> user = _user;
    private final MutableLiveData<Boolean> _toLoginScreen = new MutableLiveData<>();
    public LiveData<Boolean> toLoginScreen = _toLoginScreen;

    public HomeViewModel() {
        getUser();
    }

    public void resetToLoginScreen() {
        _toLoginScreen.postValue(false);
    }

    public void getUser() {
        disposables.add(repository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_user::postValue)
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

    public void logout() {
        disposables.add(repository.logout()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    _user.postValue(null);
                    _toLoginScreen.postValue(true);
                })
        );
    }
}