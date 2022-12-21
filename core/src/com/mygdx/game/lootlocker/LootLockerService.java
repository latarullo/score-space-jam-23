package com.mygdx.game.lootlocker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mygdx.game.lootlocker.domain.GameSessionPayload;
import com.mygdx.game.lootlocker.domain.GameSessionResponse;
import com.mygdx.game.lootlocker.domain.HighScoreItem;
import com.mygdx.game.lootlocker.domain.HighScorePayload;
import com.mygdx.game.lootlocker.domain.HighScoreResponse;
import com.mygdx.game.lootlocker.domain.PlayerNamePayload;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LootLockerService implements Disposable {
    private static final LootLockerService instance = new LootLockerService();

    public static LootLockerService getInstance() {
        return instance;
    }

    private LootLockerService() {

    }

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private final static String CREATE_SESSION_URL = "https://api.lootlocker.io/game/v2/session/guest";
    private final static String SUBMIT_HIGH_SCORE_URL = "https://api.lootlocker.io/game/leaderboards/%s/submit";
    private final static String GET_HIGH_SCORE_URL = "https://api.lootlocker.io/game/leaderboards/%s/list?count=20";
    private final static String DELETE_SESSION_URL = "https://api.lootlocker.io/game/v1/session";
    private final static String PLAYER_NAME_URL = "https://api.lootlocker.io/game/player/name";
    private final static String GAME_KEY = "dev_2d538e2023554e8db0e76ed84f89c0e0";
    private final static String GAME_VERSION = "0.0.0.0";
    private final static String HEADER_CONTENT_TYPE = "Content-Type";
    private final static String HEADER_CONTENT_TYPE_VALUE = "application/json";
    private final static String HEADER_X_SESSION_TOKEN = "x-session-token";
    private final static String HEADER_LL_VERSION = "LL-Version";
    private final static String HEADER_LL_VERSION_VALUE = "2021-03-01";
    private final static int LEADERBOARD_ID = 9518;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private String sessionToken;
    private String playerIdentifier;
    private String playerName;
    private Long highScore;

    private boolean testing = false;

    public void createSession() {
        if (testing) return;

        RequestBody sessionPostBody = createSessionPayloadBody(playerIdentifier);

        Request request = new Request.Builder()
                .url(CREATE_SESSION_URL)
                .addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
                .post(sessionPostBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unable to create LootLocker game session!");
            }

            String responseString = response.body().string();
            GameSessionResponse gameSessionResponse = GSON.fromJson(responseString, GameSessionResponse.class);
            this.sessionToken = gameSessionResponse.getSessionToken();
            this.playerIdentifier = gameSessionResponse.getPlayerIdentifier();
        } catch (IOException e) {
            throw new RuntimeException("Problems creating LootLocker game session!", e);
        }
    }

    public void changePlayerName(String name) {
        if (testing) return;

        RequestBody changeNamePayloadBody = createChangeNamePayloadBody(name);

        Request request = new Request.Builder()
                .url(PLAYER_NAME_URL)
                .addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
                .addHeader(HEADER_X_SESSION_TOKEN, sessionToken)
                .addHeader(HEADER_LL_VERSION, HEADER_LL_VERSION_VALUE)
                .patch(changeNamePayloadBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unable to update player name in LootLocker!");
            }

            String responseString = response.body().string();
            PlayerNamePayload playerNamePayload = GSON.fromJson(responseString, PlayerNamePayload.class);
            this.playerName = playerNamePayload.getName();
        } catch (IOException e) {
            throw new RuntimeException("Problems updating player name in LootLocker!", e);
        }
    }

    public HighScoreResponse getHighScores() {
        if (testing) return null;

        Request request = new Request.Builder()
                .url(String.format(GET_HIGH_SCORE_URL, LEADERBOARD_ID))
                .addHeader(HEADER_X_SESSION_TOKEN, sessionToken)
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unable to get high scores in LootLocker!");
            }

            String responseString = response.body().string();
            HighScoreResponse highScoreResponse = GSON.fromJson(responseString, HighScoreResponse.class);
            return highScoreResponse;
        } catch (IOException e) {
            throw new RuntimeException("Problems getting high scores in LootLocker!", e);
        }
    }

    private void updateHighScore() {
        if (testing) return;

        HighScoreResponse highScoreResponse = getHighScores();
        List<HighScoreItem> items = highScoreResponse.getItems();
        for (HighScoreItem item : items) {
            if (item.getRank() == 1) {
                this.highScore = item.getScore();
            }
        }
    }

    public void sendHighScore(long score) {
        if (testing) return;

        String submitHighScoreUrl = String.format(LootLockerService.SUBMIT_HIGH_SCORE_URL, LEADERBOARD_ID);

        RequestBody highScorePostBody = createHighScorePayloadBody(score);

        Request request = new Request.Builder()
                .url(submitHighScoreUrl)
                .addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
                .addHeader(HEADER_X_SESSION_TOKEN, sessionToken)
                .post(highScorePostBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unable to submit highscore to LootLocker!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Problems submiting highscore to LootLocker!", e);
        }
    }

    public void deleteSession() {
        if (testing) return;

        Request request = new Request.Builder()
                .url(DELETE_SESSION_URL)
                .addHeader(HEADER_X_SESSION_TOKEN, sessionToken)
                .delete()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unable to delete LootLocker game session!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Problems deleting LootLocker game session!", e);
        }
    }

    private RequestBody createSessionPayloadBody(String playerIdentifier) {
        GameSessionPayload gameSessionPayload = new GameSessionPayload();
        gameSessionPayload.setGameKey(GAME_KEY);
        gameSessionPayload.setGameVersion(GAME_VERSION);
        gameSessionPayload.setPlayerIdentifier(playerIdentifier);
        return createRequestBody(gameSessionPayload);
    }

    private RequestBody createHighScorePayloadBody(long score) {
        HighScorePayload highScorePayload = new HighScorePayload();
        highScorePayload.setScore(score);
        highScorePayload.setMetadata(null);
        return createRequestBody(highScorePayload);
    }

    private RequestBody createChangeNamePayloadBody(String name) {
        PlayerNamePayload playerNamePayload = new PlayerNamePayload();
        playerNamePayload.setName(name);
        return createRequestBody(playerNamePayload);
    }

    private RequestBody createRequestBody(Object jsonObject) {
        return RequestBody.create(JSON, GSON.toJson(jsonObject));
    }

    public String getPlayerName() {
        return playerName;
    }

    public Long getHighScore() {
        if (highScore == null) {
            createSession();
            updateHighScore();
            deleteSession();
            Gdx.app.log("TRACE", "HIGH SCORE UPDATED: " + highScore);
        }

        return highScore;
    }

    @Override
    public void dispose() {
        okHttpClient.connectionPool().evictAll();
        okHttpClient.dispatcher().executorService().shutdown();
    }
}
