package pl.darsonn.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import pl.darsonn.api.model.Card;

import java.io.IOException;

public class CardApi {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl = "http://localhost:10111";

    public CardApi() {
        this.httpClient = new OkHttpClient();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public Card getCard(Long cardId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/card?cardId=" + cardId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Card.class);
        }
    }

    public Card getCardByNumber(String cardNumber) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/card/number?cardNumber=" + cardNumber)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Card.class);
        }
    }

    public Card createCard(Card card) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(card)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/card")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Card.class);
        }
    }

    public Card updateCard(Integer cardID, Card card) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(card)
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/card?cardId=" + cardID)
                .patch(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Card.class);
        }
    }

    public Card deleteCard(Integer cardID) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/card?cardId=" + cardID)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return gson.fromJson(response.body().string(), Card.class);
        }
    }
}
