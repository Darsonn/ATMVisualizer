package pl.darsonn.api;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiConnectionChecker {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;

    public ApiConnectionChecker(String configFilePath) throws IOException {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();

        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            try (Writer writer = Files.newBufferedWriter(Paths.get(configFilePath))) {
                Config defaultConfig = new Config();
                defaultConfig.setServerHost("http://localhost");
                defaultConfig.setServerPort(10111);
                gson.toJson(defaultConfig, writer);
            }
        }

        try (Reader reader = Files.newBufferedReader(Paths.get(configFilePath))) {
            Config config = gson.fromJson(reader, Config.class);
            this.baseUrl = config.serverHost + ":" + config.serverPort; // Corrected URL format
        }
    }


    public boolean checkConnection() {
        Request request = new Request.Builder()
                .url(baseUrl + "/health") // Use the /health endpoint
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    @Getter
    @Setter
    private static class Config {
        private String serverHost;
        private int serverPort;
    }
}
