package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.service.model.DownloadFileInput;
import dev.mpalahuta.dirsync.service.model.FileInfo;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpRemoteDirectoryClient implements RemoteDirectoryClient {

    private final JsonMapper jsonMapper;

    public HttpRemoteDirectoryClient(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    private String buildURL(String serverIp, String endpoint) {
        return "http://" + serverIp + endpoint;
    }

    private String extractErrorMessage(String body) {
        try {
            final var message = this.jsonMapper.readTree(body).path("message");
            return message.isString() ? message.asString() : body;
        } catch (Exception e) {
            return body;
        }
    }

    @Override
    public List<FileInfo> viewDirState(String serverIp) {
        final HttpResponse<String> resp;
        try (final var client = HttpClient.newHttpClient()) {
            resp = client.send(HttpRequest.newBuilder()
                            .uri(URI.create(buildURL(serverIp, "/api/directory")))
                            .GET()
                            .build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("failed to send remote view dir state request", e);
        }

        if (resp.statusCode() != 200) {
            throw new RuntimeException("remote view dir state request failed with status "
                    + resp.statusCode() + ": " + extractErrorMessage(resp.body()));
        }

        try {
            final JsonNode fileInfoNodes = this.jsonMapper.readTree(resp.body()).path("fileInfoList");
            final List<FileInfo> fileInfoList = new ArrayList<>();
            for (final JsonNode node : fileInfoNodes) {
                fileInfoList.add(new FileInfo(
                        node.get("filename").asString(),
                        ZonedDateTime.parse(node.get("lastModifiedAt").asString())));
            }
            return fileInfoList;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse remote view dir state response", e);
        }
    }

    @Override
    public void downloadFile(DownloadFileInput input) {
        final var encodedFilename = URLEncoder.encode(input.getRemoteFilename(), StandardCharsets.UTF_8);

        try (final var client = HttpClient.newHttpClient()) {
            final HttpResponse<InputStream> resp;
            try {
                resp = client.send(HttpRequest.newBuilder()
                                .uri(URI.create(buildURL(input.getServerIp(), "/api/directory/" + encodedFilename)))
                                .GET()
                                .build(), HttpResponse.BodyHandlers.ofInputStream());
            } catch (Exception e) {
                throw new RuntimeException("failed to send remote download file request", e);
            }

            try (final var body = resp.body()) {
                if (resp.statusCode() != 200) {
                    throw new RuntimeException("remote download file request failed with status "
                            + resp.statusCode() + ": "
                            + extractErrorMessage(new String(body.readAllBytes(), StandardCharsets.UTF_8)));
                }

                final Path destFilePath = Path.of(input.getLocalFilePath(), input.getRemoteFilename());
                Files.copy(body, destFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("failed to save remote file locally", e);
            }
        }
    }

}
