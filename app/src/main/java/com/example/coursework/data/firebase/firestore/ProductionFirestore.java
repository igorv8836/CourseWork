package com.example.coursework.data.firebase.firestore;

import com.example.coursework.data.database.entities.ProductionEntity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ProductionFirestore {
    private static final String PRODUCTIONS = "productions";
    private FirebaseFirestore firestore;

    public ProductionFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public Single<String> insertProduction(ProductionEntity production) {
        return Single.<String>create(emitter ->
                firestore.collection(PRODUCTIONS)
                        .add(production)
                        .addOnSuccessListener(documentReference -> emitter.onSuccess(documentReference.getId()))
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable updateProduction(ProductionEntity production) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTIONS)
                        .document(production.getId())
                        .set(production)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteProduction(String id) {
        return Completable.create(emitter ->
                firestore.collection(PRODUCTIONS)
                        .document(id)
                        .delete()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Observable<List<ProductionEntity>> getAllProductions() {
        return Observable.create(emitter ->
                firestore.collection(PRODUCTIONS)
                        .addSnapshotListener((queryDocumentSnapshots, t) -> {
                            List<ProductionEntity> productions = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                ProductionEntity production = document.toObject(ProductionEntity.class);
                                production.setId(document.getId());
                                productions.add(production);
                            }
                            emitter.onNext(productions);
                        })
        );
    }
}
