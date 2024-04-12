package pl.darsonn.api;

import com.google.gson.Gson;
import okhttp3.*;
import pl.darsonn.api.model.Account;

import java.io.IOException;

public class AccountApi {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl = "http://localhost:10111";

    public AccountApi() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public Account getAccount(Integer accountId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/account?accountId=" + accountId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Account.class);
        }
    }

    public Account getAccountByAccountNumber(String accountNumber) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/accountByNumber?accountNumber=" + accountNumber)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Account.class);
        }
    }

    public Account createAccount(Account account) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(account)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/account")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Account.class);
        }
    }

    public Account updateAccount(Integer accountId, Account account) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(account)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/account?accountId=" + accountId)
                .patch(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Account.class);
        }
    }

    public Account deleteAccount(Integer accountId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/account?accountId=" + accountId)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Account.class);
        }
    }
}

