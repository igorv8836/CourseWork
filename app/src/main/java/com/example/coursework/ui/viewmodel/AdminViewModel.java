package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.User;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final UserRepository repository = new UserRepositoryImpl();
    private final MutableLiveData<List<User>> _users = new MutableLiveData<>();
    public MutableLiveData<List<User>> users = _users;
    private final MutableLiveData<Event<String>> _helpText = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = _helpText;

    public AdminViewModel() {
        getUsers();
    }

    private void setHelpText(String text) {
        _helpText.postValue(null);
        _helpText.postValue(new Event<>(text));
    }

    public void getUsers() {
        disposables.add(repository.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_users::postValue)
        );
    }

    public void changeFullUser(User user) {
        disposables.add(repository.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        getUsers();
    }

    public void changeType(User user, UserType role){
        user.setRole(role);
        changeFullUser(user);
    }

    public void changeUser(User user) {
        changeFullUser(user);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
