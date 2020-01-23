package com.ats.gate_sale_monginis.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ats.gate_sale_monginis.interfaces.InterfaceApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maxadmin on 19/12/17.
 */

public class Constants {

    public static final String MY_PREF = "GateSale";
    public static final String PRINTER_PREF = "Printer";

    public static final String IMAGE_PATH = "http://132.148.151.41:8080/uploads/ITEM/";

    public static int nPrintWidth = 384;
    public static boolean bCutter = true;
    public static boolean bDrawer = false;
    public static boolean bBeeper = true;
    public static int nPrintCount = 1;
    public static int nCompressMethod = 0;
    public static boolean bAutoPrint = false;
    public static int nPrintContent = 1;

    public static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);
                    return response;
                }
            })
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build();

    public static Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://192.168.2.16:8091/")
            .baseUrl("http://132.148.151.41:8080/webapi/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build();

    public static InterfaceApi myInterface = retrofit.create(InterfaceApi.class);

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(context, "No Internet Connection ! ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //public static SharedPreferences pref;
    //public static SharedPreferences.Editor editor;

}
