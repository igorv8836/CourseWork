package com.example.coursework.data.firebase.firestore;

import android.net.Uri;
import android.util.Pair;

import com.example.coursework.App;
import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductFirestore {
    private static final String INGREDIENTS_COLLECTION = "ingredients";
    private static final String PRODUCTS_COLLECTION = "products";
    private final FirebaseFirestore firestore;
    private final FirebaseStorage storage;

    public ProductFirestore() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public Single<String> insertIngredient(IngredientEntity ingredient) {
        return Single.<String>create(emitter ->
                        firestore.collection(INGREDIENTS_COLLECTION)
                                .add(ingredient)
                                .addOnSuccessListener(documentReference -> {
                                    String newDocumentId = documentReference.getId();
                                    emitter.onSuccess(newDocumentId);
                                })
                                .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateIngredient(IngredientEntity ingredient) {
        return Completable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .document(ingredient.getId())
                        .set(ingredient)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteIngredient(String id) {
        return Completable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .document(id)
                        .delete()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }


    public Observable<List<IngredientEntity>> getAllIngredients() {
        return Observable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                emitter.onError(e);
                                return;
                            }
                            if (queryDocumentSnapshots != null) {
                                List<IngredientEntity> ingredients = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    IngredientEntity ingredient = new IngredientEntity(
                                            document.getId(),
                                            document.getString("name"),
                                            document.getString("measurementText"),
                                            document.getDouble("price")
                                    );
                                    ingredients.add(ingredient);
                                }
                                emitter.onNext(ingredients);
                            }
                        })
        );
    }

    public Single<Pair<String, String>> insertProduct(ProductEntity product, Uri uri) {
        return Single.<Pair<String, String>>create(emitter -> {
                    if (uri != null) {
                        String imageName = product.getName() + ".jpg";
                        StorageReference storageRef = storage.getReference().child("images/" + imageName);

                        try {
                            InputStream stream = App.getInstance().getContentResolver().openInputStream(uri);
                            UploadTask uploadTask = storageRef.putStream(Objects.requireNonNull(stream));

                            uploadTask.addOnSuccessListener(taskSnapshot ->
                                    storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                        product.setImageUri(downloadUri.toString());
                                        firestore.collection(PRODUCTS_COLLECTION)
                                                .add(product)
                                                .addOnSuccessListener(documentReference -> emitter.onSuccess(new Pair<>(documentReference.getId(), downloadUri.toString())))
                                                .addOnFailureListener(emitter::onError);
                                    }).addOnFailureListener(emitter::onError)
                            ).addOnFailureListener(emitter::onError);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    } else {
                        firestore.collection(PRODUCTS_COLLECTION)
                                .add(product)
                                .addOnSuccessListener(documentReference -> emitter.onSuccess(new Pair<>(documentReference.getId(), null)))
                                .addOnFailureListener(emitter::onError);
                    }

                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateProduct(ProductEntity product) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .document(product.getId())
                        .set(product)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteProduct(String id) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .document(id)
                        .delete()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Observable<List<ProductEntity>> getAllProducts() {
        return Observable.create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                emitter.onError(e);
                                return;
                            }
                            if (queryDocumentSnapshots != null) {
                                List<ProductEntity> products = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    ProductEntity product = document.toObject(ProductEntity.class);
                                    product.setId(document.getId());
                                    products.add(product);
                                }
                                emitter.onNext(products);
                            }
                        })
        );
    }
}
