package com.example.coursework.data.firebase.firestore;

import com.example.coursework.data.database.entities.SaleEntity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SaleFirestore {
    private static final String SALES = "sales";
    private FirebaseFirestore firestore;

    public SaleFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public Single<String> insertSale(SaleEntity sale) {
        return Single.<String>create(emitter ->
                firestore.collection(SALES)
                        .add(sale)
                        .addOnSuccessListener(documentReference -> emitter.onSuccess(documentReference.getId()))
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable updateSale(SaleEntity sale) {
        return Completable.create(emitter ->
                firestore.collection(SALES)
                        .document(sale.getId())
                        .set(sale)
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable deleteSale(String id) {
        return Completable.create(emitter ->
                firestore.collection(SALES)
                        .document(id)
                        .delete()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Observable<List<SaleEntity>> getSales() {
        return Observable.create(emitter ->
                firestore.collection(SALES)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                emitter.onError(e);
                                return;
                            }
                            List<SaleEntity> sales = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                SaleEntity sale = document.toObject(SaleEntity.class);
                                sale.setId(document.getId());
                                sales.add(sale);
                            }
                            emitter.onNext(sales);
                        })
        );
    }
}
