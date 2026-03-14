package com.huerteando.app.api;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    // IMPORTANTE: Cambia esta URL por la dirección de tu servidor API
    // Ejemplo: "http://10.0.2.2:8080/api/" (para emulador)
    // Ejemplo: "http://192.168.1.X:8080/api/" (para dispositivo real en la misma red)
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    private static Retrofit  retrofit = null;

    public static Retrofit getClient(String token) {
        // Interceptor HTTP: añade el token a TODAS las peticiones automáticamente
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();          // Coge la petición original
                    Request.Builder requestBuilder = original.newBuilder(); // Prepara una copia modificable

                    // Si hay token, lo añade en la cabecera Authorization
                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(requestBuilder.build());
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)                          // ← Dónde está el servidor
                .addConverterFactory(GsonConverterFactory.create()) // ← Convierte JSON ↔ Java automáticamente
                .client(httpClient)                         // ← Usa el cartero personalizado con el token
                .build();

        return retrofit;
    }

    // Versión sin token (para login y registro)
    public static Retrofit getClient() {
        return getClient(null);
    }
}
