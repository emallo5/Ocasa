package com.android.ocasa.api;

import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ignacio on 21/06/16.
 */
public interface OcasaApi {

    @GET("android/")
    Observable<Menu> menu();

    @GET("android/table/{table_id}/column/{table_json}")
    Observable<HttpTable> columns(@Path("table_id") String tableId, @Path("table_json") String tableJson);

    @GET("android/table/{table_id}/record/{table_json}")
    Observable<TableRecord> records(@Path("table_id") String tableId, @Path("table_json") String tableJson);
}
