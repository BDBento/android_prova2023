package br.com.ifms.finalApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class listaDePersonagens extends AppCompatActivity {
    TextView textViewFav;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Personagem> listaPersonagens = new ArrayList<>();
    Handler handler = new Handler();
    MRecyclerAdapter recyclerAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_personagens);

        recyclerView = findViewById(R.id.recyclerView);
        textViewFav = findViewById(R.id.textViewFav);
        searchView = findViewById(R.id.searchView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        textViewFav.setText("Gerenciar favoritos - " + FirebaseAuth.getInstance()
                .getCurrentUser().getEmail());

        //searchView sempre aberto
        searchView.setIconified(false);
        searchView.clearFocus();

        try {
            setInfo("name");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //método para submit
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    setInfo(s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerAdapter.notifyDataSetChanged();

                return true;
            }

            //método ao alterar o texto - similar ao TextWatcher
            @Override
            public boolean onQueryTextChange(String s) {

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // código igual ao submit acima:
                        try {
                            setInfo(s);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }, 400);

                return true;
            }
        });

        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(listaDePersonagens.this, personagensFavoritos.class);
                startActivity(i);
            }
        });

    }

    private void setInfo(String busca) throws ExecutionException, InterruptedException {

        if (busca.trim().equals(""))
            busca = "name";

        listaPersonagens.clear();
        String url = "https://rickandmortyapi.com/api/character";

        listaPersonagens.addAll(new ObjAPI().execute(url).get());

        Log.e("informações baixadas:", String.valueOf(listaPersonagens.size()));
    }

    private void setRecyclerView() {

        //instanciando a classe RecyclerAdapter com o arraylist populado
        recyclerAdapter = new MRecyclerAdapter(listaPersonagens);

        //setando a recyclerView para correta exibição com os itens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}