package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final UserRepository repository = new UserRepository();

    private final MutableLiveData<User> _loggedUser = new MutableLiveData<>();
    public MutableLiveData<User> loggedUser = _loggedUser;
    private final MutableLiveData<List<User>> _users = new MutableLiveData<>();
    public MutableLiveData<List<User>> users = _users;
    private final MutableLiveData<String> _helpText = new MutableLiveData<>();
    public MutableLiveData<String> helpText = _helpText;

    public AdminViewModel() {
        getLoggedUser();
        getUsers();
    }

    private void setHelpText(String text) {
        _helpText.postValue(null);
        _helpText.postValue(text);
    }

    public void getLoggedUser() {
        disposables.add(repository.checkLoggedUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user == 0) {
                        _loggedUser.postValue(null);
                        return;
                    }
                    Disposable a = repository.getLoggedUser()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(_loggedUser::postValue);
                    disposables.add(a);
                })
        );
    }

    public void getUsers() {
        disposables.add(repository.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_users::postValue)
        );
    }

    public void changeType(User user, UserType role){
        user.setRole(role);
        disposables.add(repository.updateUser(user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe());
    }

    public void changeUser(User user) {
        disposables.add(repository.getUserByEmail(user.getEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user1 -> {
                    if (user1.getEmail().equals(user.getEmail()) && user1.getId() != user.getId()) {
                        setHelpText("Почта уже занята");
                        return;
                    }
                    Disposable a = repository.updateUser(user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                    disposables.add(a);
                })
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
