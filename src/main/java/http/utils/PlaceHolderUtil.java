package http.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.placeholderdata.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceHolderUtil {
    private static final String USERS_URL = "https://jsonplaceholder.typicode.com/users";
    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final Gson GSON = new Gson();

    public static User createTestUser() {
        User newUser = new User();

        newUser.setId(100);
        newUser.setName("Test");
        newUser.setUsername("Test username");
        newUser.setEmail("test@test.com");

        Address newAddress = new Address();
        newAddress.setStreet("test park");
        newAddress.setSuite("test 567");
        newAddress.setCity("Test city");
        newAddress.setZipcode("12345-6789");
        Geo newGeo = new Geo();
        newGeo.setLat("35.6789");
        newGeo.setLng("23.4567");
        newAddress.setGeo(newGeo);

        newUser.setAddress(newAddress);
        newUser.setPhone("1-234-567-8999 x12345");
        newUser.setWebsite("test.org");

        Company newCompany = new Company();
        newCompany.setName("test cpmpany");
        newCompany.setCatchPhrase("test, only test, that`s all");
        newCompany.setBs("test, again test");

        newUser.setCompany(newCompany);

        return newUser;
    }

    public static User createUserApache(User user) throws IOException {
        String requestBody = new Gson().toJson(user);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(USERS_URL);
            httpPost.setEntity(new StringEntity(requestBody));
            System.out.println("Executing POST request...");

            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseBody);
            return GSON.fromJson(responseBody, User.class);
        }
    }

    public static void changeUserApache(User user, int userIdChange) throws IOException {
        String urlChangeById = USERS_URL + "/" + userIdChange;
        String requestBody = new Gson().toJson(user);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(urlChangeById);
            httpPut.setEntity(new StringEntity(requestBody));

            System.out.println("Executing PUT request user id = [" + userIdChange + "] ...");

            CloseableHttpResponse response = httpClient.execute(httpPut);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseBody);
        }
    }

    public static void deleteUserApache(int userIdDelete) throws IOException {
        String urlDeleteById = USERS_URL + "/" + userIdDelete;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(urlDeleteById);
            System.out.println("Executing DELETE request user id = [" + userIdDelete + "]...");

            CloseableHttpResponse response = httpClient.execute(httpDelete);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
        }
    }

    public static void getAllUsersApache() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(USERS_URL);
            System.out.println("Executing GET ALL USERS request...");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseBody);
        }
    }

    public static User getUserByIdApache(int userIdGet) throws IOException {
        String urlGetById = USERS_URL + "/" + userIdGet;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlGetById);
            System.out.println("Executing GET request...");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseBody);
            return GSON.fromJson(responseBody, User.class);
        }
    }

    public static void getUserByUserNameApache(String userName) throws IOException {
        String urlByName = String.format("%s?username=%s", USERS_URL, userName);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlByName);
            System.out.println("Executing GET request...");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseBody);
        }
    }

    public static int getLastPostIdByUser(int userId) throws IOException {
        String urlIdPosts = USERS_URL + "/" + userId + "/posts";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlIdPosts);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseBody = new BasicResponseHandler().handleResponse(response);

            Type typeToken = TypeToken.getParameterized(List.class, Post.class).getType();
            List<Post> posts = new Gson().fromJson(responseBody, typeToken);
            Post lastPost = posts.stream()
                    .max(Post::compare)
                    .get();

            return lastPost.getId();
        }
    }

    public static void getPostsById(int userId, int postId) throws IOException {
        String urlIdPosts = POSTS_URL + "/" + postId + "/comments";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlIdPosts);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseBody = new BasicResponseHandler().handleResponse(response);

            String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
            FileWriter filePosts = new FileWriter(fileName);

            Type typeToken = TypeToken.getParameterized(List.class, Comment.class).getType();
            List<Comment> comments = new Gson().fromJson(responseBody, typeToken);
            int count = 1;
            for (Comment comment : comments) {
                System.out.println(comment.getBody());
                filePosts.write("post #" + count + ": ");
                filePosts.write(comment.getBody());
                filePosts.write(System.lineSeparator());
                count++;
            }
            filePosts.close();
        }
    }

    public static void getOpenedTasksByUser(int userId) throws IOException {
        String urlIdPosts = USERS_URL + "/" + userId + "/todos";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlIdPosts);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("Status code = " + response.getStatusLine().getStatusCode());
            String responseBody = new BasicResponseHandler().handleResponse(response);

            Type typeToken = TypeToken.getParameterized(List.class, Todos.class).getType();
            List<Todos> tasks = new Gson().fromJson(responseBody, typeToken);
            List<Todos> openedTasks = tasks.stream()
                    .filter(c -> !c.isCompleted())
                    .collect(Collectors.toList());
            for (Todos openedTask : openedTasks) {
                System.out.println("opened task = " + openedTask);
            }
        }
    }
}
