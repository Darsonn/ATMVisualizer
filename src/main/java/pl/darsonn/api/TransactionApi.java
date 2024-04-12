package pl.darsonn.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import pl.darsonn.api.model.Transaction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TransactionApi {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl = "http://localhost:10111";

    public TransactionApi() {
        this.httpClient = new OkHttpClient();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public Transaction getTransaction(Integer transactionId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/transaction?transactionId=" + transactionId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Transaction.class);
        }
    }

    public List<Transaction> getAllClientsTransactions(int accountID) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/transactionsByAccountID?accountID=" + accountID)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Type transactionListType = new TypeToken<List<Transaction>>(){}.getType();
            return gson.fromJson(response.body().string(), transactionListType);
        }
    }


    public Transaction createTransaction(Transaction transaction) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(transaction)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/transaction")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Transaction.class);
        }
    }

    public Transaction updateTransaction(Integer transactionId, Transaction transaction) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(transaction)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/transaction?transactionId=" + transactionId)
                .patch(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Transaction.class);
        }
    }

    public Transaction deleteTransaction(Integer transactionId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/transaction?transactionId=" + transactionId)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Transaction.class);
        }
    }
}

