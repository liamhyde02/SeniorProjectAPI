package helloworld.DataFetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class GithubDataFetcher implements DataFetcher {
    private static final String LAMBDA_TMP_DIR = "/tmp/";
    private static String downloadDirectory = LAMBDA_TMP_DIR; // default to Lambda tmp directory
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN"); // Replace with your GitHub token

    public ArrayList<File> downloadPackage(String repoUrl, String branch, boolean isLambdaEnvironment) throws IOException {
        if (!isLambdaEnvironment) {
            downloadDirectory = System.getProperty("user.dir") + File.separator + "downloaded_files"; // or any other directory
            new File(downloadDirectory).mkdirs(); // create the directory if it doesn't exist
        }

        ArrayList<File> javaFiles = new ArrayList<>();
        String ownerRepo = repoUrl.substring(repoUrl.indexOf("github.com/") + 11);
        String[] ownerRepoParts = ownerRepo.split("/");
        String owner = ownerRepoParts[0];
        String repo = ownerRepoParts[1];
        String treeUrl = String.format("%s/repos/%s/%s/git/trees/%s?recursive=1", GITHUB_API_URL, owner, repo, branch);

        // Fetch the repository tree
        JsonNode tree = fetchJson(treeUrl).get("tree");

        // Iterate over the tree entries and filter for .java files
        Iterator<JsonNode> elements = tree.elements();
        while (elements.hasNext()) {
            JsonNode entry = elements.next();
            if ("blob".equals(entry.get("type").asText()) && entry.get("path").asText().endsWith(".java")) {
                String rawUrl = String.format("https://raw.githubusercontent.com/%s/%s/%s/%s", owner, repo, branch, entry.get("path").asText());
                File downloadedFile = downloadFile(new URL(rawUrl), entry.get("path").asText());
                javaFiles.add(downloadedFile);
            }
        }

        return javaFiles;
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

    private static File downloadFile(URL url, String filePath) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

        File file = new File(downloadDirectory + File.separator + filePath);
        file.getParentFile().mkdirs(); // Ensure the parent directories are created
        try (InputStream in = connection.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }
}
