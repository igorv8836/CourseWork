package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final UserRepository repository = new UserRepository();

    private final MutableLiveData<User> _loggedUser = new MutableLiveData<>();
    public MutableLiveData<User> loggedUser = _loggedUser;
    private final MutableLiveData<Event<String>> _helpText = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = _helpText;

    public AuthViewModel() {
        getLoggedUser();
    }

    private void setHelpText(String text) {
        _helpText.postValue(null);
        _helpText.postValue(new Event<>(text));
    }

    public void createAccount(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(repository.getUserCountByEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user == 0) {
                        disposables.add(repository.insertUser(
                                new User(0, name, email, password, UserType.CREATOR, true)
                                ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        );
                    }
                    else
                        setHelpText("Такой пользователь уже существует!");
                },
                        throwable -> {
                            setHelpText("Ошибка при создании пользователя");
                        }
        ));
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(repository.getUserByUsernameAndPassword(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != 0) {
                        disposables.add(
                                repository.loginUser(email, password)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                () -> {},
                                                throwable -> setHelpText("Ошибка при входе")
                                        )
                        );
                    } else {
                        setHelpText("Неверный логин или пароль");
                    }
                })
        );
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


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
