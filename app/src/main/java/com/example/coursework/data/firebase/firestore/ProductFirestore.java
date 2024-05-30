package com.example.coursework.data.firebase.firestore;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class ProductFirestore {
    private static final String INGREDIENTS_COLLECTION = "ingredients";
    private static final String PRODUCTS_COLLECTION = "products";
    private final FirebaseFirestore firestore;

    public ProductFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public Completable insertIngredient(IngredientEntity ingredient) {
        return Completable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .add(ingredient)
                        .addOnSuccessListener(documentReference -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable updateIngredient(String id, IngredientEntity ingredient) {
        return Completable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .document(id)
                        .set(ingredient)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteIngredient(Integer id) {
        return Completable.create(emitter ->
                firestore.collection(INGREDIENTS_COLLECTION)
                        .whereEqualTo("id", id)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    firestore.collection(INGREDIENTS_COLLECTION)
                                            .document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> emitter.onComplete())
                                            .addOnFailureListener(emitter::onError);
                                }
                            } else if (task.getResult().isEmpty()) {
                                emitter.onComplete();
                            } else if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            }
                        })
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
                                    IngredientEntity ingredient = document.toObject(IngredientEntity.class);
                                    ingredients.add(ingredient);
                                }
                                emitter.onNext(ingredients);
                            }
                        })
        );
    }

    public Completable insertProduct(ProductEntity product) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .add(product)
                        .addOnSuccessListener(documentReference -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable updateProduct(String id, ProductEntity product) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTS_COLLECTION)
                        .document(id)
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
                                }
                                emitter.onNext(products);
                            }
                        })
        );
    }
}
