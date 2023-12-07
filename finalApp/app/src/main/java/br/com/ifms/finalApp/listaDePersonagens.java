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

        //recuperando dados do usuário logado para exibir na tela
        textViewFav.setText("Gerenciar favoritos - " + FirebaseAuth.getInstance()
                .getCurrentUser().getEmail());

        //searchView sempre aberto
        searchView.setIconified(false);
        //retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView.clearFocus();

        try {
            //setInfo() = método que popula o arraylist com dados vindos da API;
            //Obs:
            //- throws/try/catch exigido pelo get(), que é assíncrono.
            //- envio da string "movie" para trazer alguma coleção de dados por padrão.
            setInfo("movie");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //ao término do método setInto, com o arraylist populado, montar RecyclerView e Adapter
        setRecyclerView();

        //após a primeira execução do método que popula e monta a lista (feito automaticamente)
        //o usuário pode querer buscar por um termo no SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //método para submit
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    //executamos novamente o método setInfo(), agora com o termo da busca
                    //isto vai gerar uma nova consulta com uma nova URL e
                    //um novo conjunto de dados no arraylist
                    setInfo(s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //"avisar" o adapter para atualização da RecyclerView
                recyclerAdapter.notifyDataSetChanged();

                return true;
            }

            //método ao alterar o texto - similar ao TextWatcher
            @Override
            public boolean onQueryTextChange(String s) {
                //considerar que aqui cada caractere digitado = uma requisição na API
                //a rotina abaixo espera a digitação do usuário para as requisições (400 milisegundos)

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

        //ir para a tela com os favoritos do usuário:
        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(listaDePersonagens.this, personagensFavoritos.class);
                startActivity(i);
            }
        });

    }

    private void setInfo(String busca) throws ExecutionException, InterruptedException {

        //Obs: busca = 3 caracteres no mínimo. Regra da API.

        //se o campo de busca for alterado e limpo na sequência, considerar valor padrão "movies"
        if (busca.trim().equals(""))
            busca = "movies";

        //limpa o array
        listaPersonagens.clear();

        //monta a url de busca com a key
        String url = "https://rickandmortyapi.com/api/character";

        //executa a classe DownloadDados, que retorna o array populado e popula o array local
        //get() = executando de forma assíncrona, ou seja, aguarda a consulta e os dados
        //para popular o array
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