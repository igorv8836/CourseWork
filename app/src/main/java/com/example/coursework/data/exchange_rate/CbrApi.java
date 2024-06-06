package com.example.coursework.data.exchange_rate;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CbrApi {
    @GET("daily_json.js")
    Single<CurrencyResponse> getDailyRates();
}
