package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.AuthRepositoryImpl;
import com.example.coursework.domain.repositories.AuthRepository;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.User;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final AuthRepository repository = new AuthRepositoryImpl();

    public MutableLiveData<Boolean> nextScreen = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<Boolean> isActive = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = new MutableLiveData<>();

    public AuthViewModel() {
        getLoggedUser();
    }

    private void setHelpText(String text) {
        helpText.postValue(new Event<>(text));
    }

    private void handleResult(Boolean result) {
        isActive.postValue(true);
        if (result) {
            nextScreen.postValue(true);
        }
    }

    private void handleError(Throwable throwable) {
        isActive.postValue(true);
        setHelpText(throwable.getMessage());
    }

    public void createAccount(String name, String email, String password) {
        isActive.postValue(false);
        disposables.add(repository.register(name, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::handleError)
        );
    }

    public void login(String email, String password) {
        isActive.postValue(false);
        disposables.add(repository.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    isActive.postValue(true);
                    if (res != null) {
                        nextScreen.postValue(true);
                    }
                }, this::handleError)
        );
    }

    public void getLoggedUser() {
        disposables.add(repository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != null) {
                        nextScreen.postValue(true);
                        this.user.postValue(user);
                    }
                }, throwable -> {})
        );
    }

    public void recoverPassword(String email) {
        isActive.postValue(false);
        disposables.add(repository.recoverPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    isActive.postValue(true);
                    setHelpText("Письмо отправлено на почту");
                }, this::handleError)
        );
    }

    public void saveUser(String lastPassword, String newPassword, String name) {
        if (!lastPassword.isEmpty() && !newPassword.isEmpty()) {
            updatePassword(lastPassword, newPassword);
        }
        if (!Objects.equals(Objects.requireNonNull(user.getValue()).getUsername(), name)) {
            updateName(name);
        }
    }

    private void updatePassword(String lastPassword, String newPassword) {
        isActive.postValue(false);
        disposables.add(repository.changePassword(Objects.requireNonNull(user.getValue()).getEmail(), lastPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    isActive.postValue(true);
                    setHelpText("Данные успешно изменены");
                }, this::handleError)
        );
    }

    private void updateName(String name) {
        isActive.postValue(false);
        disposables.add(repository.changeName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    isActive.postValue(true);
                    setHelpText("Данные успешно изменены");
                }, this::handleError)
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
