package com.example.coursework.data.firebase.firestore;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductFirestore {
    private static final String INGREDIENTS_COLLECTION = "ingredients";
    private static final String PRODUCTS_COLLECTION = "products";
    private final FirebaseFirestore firestore;

    public ProductFirestore() {
        firestore = FirebaseFirestore.getInstance();
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

    public Single<String> insertProduct(ProductEntity product) {
        return Single.<String>create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .add(product)
                        .addOnSuccessListener(documentReference -> emitter.onSuccess(documentReference.getId()))
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
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
                                    products.add(document.toObject(ProductEntity.class));
                                    // TODO: check if this works
                                }
                                emitter.onNext(products);
                            }
                        })
        );
    }
}
