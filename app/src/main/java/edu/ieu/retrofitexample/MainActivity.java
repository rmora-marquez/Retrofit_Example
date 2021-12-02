package edu.ieu.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.ieu.retrofitexample.interfaces.PostService;
import edu.ieu.retrofitexample.modelo.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private EditText etTitle;
    private EditText etBody;
    private Button btnSend;
    private ListView listPost;
    private ArrayAdapter<String> adapter;
    private List<String> titles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTitle = findViewById(R.id.et_title);
        etBody = findViewById(R.id.et_body);
        btnSend = findViewById(R.id.btn_send);
        listPost = findViewById(R.id.listPost);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listPost.setAdapter(adapter);
        btnSend.setOnClickListener(view -> {
            sendPost();
        });
        getPost();
    }

    private void sendPost() {
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();
        if(title.isEmpty() || body.isEmpty()){
            Toast.makeText(this, "Title y Body son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService postService = retrofit.create(PostService.class);

        Post nuevoPost = new Post();
        nuevoPost.setBody(body);
        nuevoPost.setTitle(title);
        nuevoPost.setUserId(1);

        Call<Post> call = postService.sendPost(nuevoPost);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                Log.d(TAG, "onResponse: post" + post.toString());
                titles.add(post.getTitle());
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Nuevo post creado " + post.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error en crear un nuevo post",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPost(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService postService = retrofit.create(PostService.class);
        //AQUI LLAMAMOS AL SERVICIO WEB - REST
        Call<List<Post>> call = postService.getPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                //AQUI RECUPERAMOS LOS DATOS DEL SERVIDOR (OBJETOS POST)
                for(Post post: response.body()){
                    Log.d(TAG, "onResponse: post " + post.toString());
                    //AGREGAMOS A LA LISTA
                    titles.add(post.getTitle());
                }
                //NOTIFICAMOS CAMBIOS EN LA LISTA
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //FALLO LA CONEXION O ALGO
                Toast.makeText(getApplicationContext(), "Error en recuperar datos del servidor",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}