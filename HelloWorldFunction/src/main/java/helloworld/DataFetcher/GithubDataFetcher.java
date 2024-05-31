package helloworld.DataFetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.FileSingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.UUID;

public class GithubDataFetcher implements DataFetcher {
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    private static Path tempDirectory;

    public GithubDataFetcher() throws IOException {
        String tempDir = System.getenv("LAMBDA_TMP_DIR");
        if (tempDir == null) {
            tempDir = "/tmp/";
        }
        UUID uuid = UUID.randomUUID();
        System.out.println("UUID: " + uuid);
        FileSingleton.getInstance().setUuid(uuid);
        tempDirectory = Files.createTempDirectory(Paths.get(tempDir), "github_fetcher_" + uuid.toString() + "_");
        tempDirectory.toFile().deleteOnExit();
    }

    public void downloadPackage(String repoUrl, String branch, boolean isLambdaEnvironment) throws IOException {
        ArrayList<File> javaFiles = new ArrayList<>();
        String ownerRepo = repoUrl.substring(repoUrl.indexOf("github.com/") + 11);
        String[] ownerRepoParts = ownerRepo.split("/");
        String owner = ownerRepoParts[0];
        String repo = ownerRepoParts[1];
        String treeUrl = String.format("%s/repos/%s/%s/git/trees/%s?recursive=1", GITHUB_API_URL, owner, repo, branch);

        JsonNode tree = fetchJson(treeUrl).get("tree");

        Iterator<JsonNode> elements = tree.elements();
        while (elements.hasNext()) {
            JsonNode entry = elements.next();
            if ("blob".equals(entry.get("type").asText()) && entry.get("path").asText().endsWith(".java")) {
                String rawUrl = String.format("https://raw.githubusercontent.com/%s/%s/%s/%s", owner, repo, branch, entry.get("path").asText());
                File downloadedFile = downloadFile(new URL(rawUrl), entry.get("path").asText());
                javaFiles.add(downloadedFile);
            }
        }
        System.out.println("Downloaded " + javaFiles.size() + " files");
        FileSingleton.getInstance().setFiles(javaFiles);
    }

    private static JsonNode fetchJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (InputStream inputStream = connection.getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        }
    }

    private File downloadFile(URL url, String filePath) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

        String fileName = Paths.get(filePath).getFileName().toString();
        File file = new File(tempDirectory.toFile(), fileName);
        file.getParentFile().mkdirs();
        try (InputStream in = connection.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    public static void clearFiles() {
        try {
            Process p = Runtime.getRuntime().exec("rm -rf /tmp/...?* /tmp/.[!.]* /tmp/*");
            p.waitFor();
            System.out.println("Cleared /tmp directory");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


