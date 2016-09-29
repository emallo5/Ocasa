package com.android.ocasa.api;

import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Table;

import okhttp3.ResponseBody;
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
    Observable<Menu> login(@Body LoginCredentials credentials);

    @GET("Tables.ashx/{table_id}/columns")
    Observable<Table> columns(@Path("table_id") String tableId, @Query("imei") String imei, @Query("lat") double latitude, @Query("lng") double longitude);

    @GET("Tables.ashx/{table_id}/records")
    Observable<TableRecord> records(@Path("table_id") String tableId, @Query("imei") String imei, @Query("lat") double latitude, @Query("lng") double longitude);

    @POST("table/{table_id}/record/{table_json}")
    Observable<ResponseBody> sync(@Path("table_id") String tableId, @Path("table_json") String tableJson);
}
