package pl.darsonn.api;

import com.google.gson.Gson;
import okhttp3.*;
import pl.darsonn.api.model.Client;

import java.io.IOException;

public class ClientApi {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl = "http://localhost:10111";

    public ClientApi() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public Client getClient(Integer clientId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/client?clientId=" + clientId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Client.class);
        }
    }

    public Client createClient(Client client) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(client)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/client")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Client.class);
        }
    }

    public Client updateClient(Integer clientId, Client client) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(client)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/client?clientId=" + clientId)
                .patch(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Client.class);
        }
    }

    public Client deleteClient(Integer clientId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/client?clientId=" + clientId)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Client.class);
        }
    }
}

