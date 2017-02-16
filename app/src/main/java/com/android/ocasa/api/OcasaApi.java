package com.android.ocasa.api;

import com.android.ocasa.httpmodel.ControlBody;
import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.httpmodel.GenericResponse;
import com.android.ocasa.httpmodel.LogOutBody;
import com.android.ocasa.httpmodel.MediaBody;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.ResponseImage;
import com.android.ocasa.httpmodel.ResponseReceipt;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Receipt;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ignacio on 21/06/16.
 */
public interface OcasaApi {

    @POST("Login.ashx")
    Observable<Menu> login(@Body LoginCredentials credentials, @Query("imei") String imei);
    @POST("/Logout.ashx")
    Observable<GenericResponse> logout (@Body LogOutBody body);

    @POST("/control.ashx")
    Observable<ControlResponse> controlSync (@Body ControlBody body);


    @GET("Tables.ashx/{table_id}/columns")
    Observable<Layout> columns(@Path("table_id") String tableId, @Query("imei") String imei, @Query("lat") double latitude, @Query("lng") double longitude);
    @GET("Tables.ashx/{table_id}/records")
    Observable<TableRecord> records(@Path("table_id") String tableId, @Query("imei") String imei, @Query("lat") double latitude, @Query("lng") double longitude);


    @POST("Actions.ashx")
    Observable<ResponseReceipt> upload(@Body TableRecord records, @Query("id") String actionId, @Query("imei") String imei, @Query("lat") double latitude, @Query("lng") double longitude);
    @POST("Images.ashx/{table_id}/file")
    Observable<ResponseImage> uploadImage(@Body MediaBody media, @Path("table_id") String tableId, @Query("imei") String imei);
}
