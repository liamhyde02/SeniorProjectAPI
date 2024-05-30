package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import helloworld.DataFetcher.GithubDataFetcher;
import helloworld.GraphBuilder.Functionality;
import helloworld.Parser.JavaEntity;
import helloworld.Parser.JavaParserFunctionality;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        // Clean up /tmp directory at the beginning
        cleanupTmpDirectory();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            JSONObject body = new JSONObject(input.getBody());
            String message = body.getString("type");
            String branch = body.getString("branch");
            JSONObject fetcherRequestBody = new JSONObject();

            fetcherRequestBody.put("type", message);
            switch (message) {
                case "github":
                    System.out.println("Github");
                    fetcherRequestBody.put("url", body.getString("url"));
                    break;
                default:
                    return response
                            .withBody("{\"error\": \"Invalid fetching type: " + message + "\"}")
                            .withStatusCode(500);
            }
            ArrayList<File> files = new ArrayList<>();
            try {
                files.addAll(new GithubDataFetcher().downloadPackage(body.getString("url"), branch, true));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return response
                        .withBody("{\"error\": \"Failed to fetch files: " + e.getMessage() + "\"}")
                        .withStatusCode(500);
            }

            JavaParserFunctionality parser = new JavaParserFunctionality();
            ArrayList<JavaEntity> JavaEntities = parser.parse(files);
            Functionality.toGraphData(JavaEntities);
            S3Handler s3Handler = new S3Handler("lihydeseniorprojectactionbucket");
            UMLBuilder umlBuilder = new UMLBuilder(s3Handler);
            String S3Link = umlBuilder.buildUMLDiagram();
            return response
                    .withStatusCode(200)
                    .withBody(S3Link);  // Return only the link
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage())
                    .put("stackTrace", e.getStackTrace())
                    .put("cause", e.getCause())
                    .put("Body", input.getBody());
            return response
                    .withBody(error.toString())
                    .withStatusCode(500);
        }
    }

    private void cleanupTmpDirectory() {
        File tmpDir = new File("/tmp");
        if (tmpDir.isDirectory()) {
            for (File file : tmpDir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
