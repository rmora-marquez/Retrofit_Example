package edu.ieu.retrofitexample.interfaces;

import java.util.List;

import edu.ieu.retrofitexample.modelo.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostService {
    String API_ROUTE = "/posts";

    @GET(API_ROUTE)
    Call<List<Post>> getPost();

    @Headers({"Content-type: application/json"})
    @POST(API_ROUTE)
    Call<Post> sendPost(@Body Post post);
}
