package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.AuthRepositoryImpl;
import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.AuthRepository;
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
    private final AuthRepository repository = new AuthRepositoryImpl();

    private final MutableLiveData<Boolean> _nextScreen = new MutableLiveData<>();
    public MutableLiveData<Boolean> nextScreen = _nextScreen;
    private final MutableLiveData<Boolean> _isActive = new MutableLiveData<>();
    public MutableLiveData<Boolean> isActive = _isActive;
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
        _isActive.postValue(false);
        disposables.add(repository.register(name, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            _isActive.postValue(true);
                            if (res) {
                                _nextScreen.postValue(true);
                            }

                        },
                        throwable -> {
                            _isActive.postValue(true);
                            setHelpText(throwable.getMessage());
                        }
                ));
    }

    public void login(String email, String password) {
        _isActive.postValue(false);
        disposables.add(repository.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            _isActive.postValue(true);
                            if (res != null) {
                                _nextScreen.postValue(true);
                            }
                        },
                        throwable -> {
                            _isActive.postValue(true);
                            setHelpText(throwable.getMessage());
                        })
        );
    }

    public void getLoggedUser() {
        disposables.add(repository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != null)
                        _nextScreen.postValue(true);
                }, throwable -> {
                })
        );
    }

    public void recoverPassword(String email) {
        _isActive.postValue(false);
        disposables.add(
                repository.recoverPassword(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    _isActive.postValue(true);
                                    setHelpText("Письмо отправлено на почту");
                                },
                                throwable -> {
                                    _isActive.postValue(true);
                                    setHelpText(throwable.getMessage());
                                }
                        )
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
