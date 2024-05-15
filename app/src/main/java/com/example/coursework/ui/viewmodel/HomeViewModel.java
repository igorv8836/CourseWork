package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.ui.entities.User;

import io.reactivex.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final UserRepository repository = new UserRepository();

    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public LiveData<User> user = _user;
    private final MutableLiveData<Boolean> _isCreator = new MutableLiveData<>();
    public LiveData<Boolean> isCreator = _isCreator;

    public HomeViewModel() {
        getUser();
        checkUserRole();
    }

    public void getUser() {
        disposables.add(repository.checkLoggedUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user == 0) {
                        _user.postValue(null);
                        return;
                    }
                    Disposable a = repository.getLoggedUser()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(_user::postValue);
                    disposables.add(a);
                })
        );
    }

    public void checkUserRole() {
        disposables.add(repository.getLoggedUserRole()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(role -> {
                    _isCreator.postValue(role.getValue() == 0);
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

    public void logout() {
        disposables.add(repository.logoutUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    _user.postValue(null);
                })
        );
    }
}