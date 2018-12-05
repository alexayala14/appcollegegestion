package com.arz.chech.collegegestion.fragments;

import com.arz.chech.collegegestion.notifications.MyResponse;
import com.arz.chech.collegegestion.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface ApiServicee {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAG_t3pN4:APA91bEnb7t6rzbwXEOZhnwjHq0u9JlnoMccdqH3xx-ujB2b-rGvTnaQd3YtLDcmAac5C7qtsCaw2Lz-9csC5z2GSugt1p0TZQB7rP7AN2etr9UIs5eM0NWV9fpw8-eOYK52is1kaW8g"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotificationp(@Body Sender body);
}
