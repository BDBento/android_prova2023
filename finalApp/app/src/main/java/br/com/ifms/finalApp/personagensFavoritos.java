package br.com.ifms.finalApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Esta tela faz uma consulta ao Firebase, que retorna uma coleção de Personagens adicionados aos favoritos
//que são representados também em um RecyclerView
//O recyclerAdapter utilizado é o mesmo da tela de consulta na API.

//funciona e é composta de maneira similar à tela de consulta à API (UsuarioLogado),
//contudo com dados vindos do Firebase
public class personagensFavoritos extends AppCompatActivity {

    TextView edLogoff;
    SearchView searchView;
    RecyclerView edRecyclerView;
    ArrayList<Personagem> listaPersonagens = new ArrayList<>();
    MRecyclerAdapter recyclerAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personagens_favoritos);

        edRecyclerView = findViewById(R.id.recyclerViewFav);
        edLogoff = findViewById(R.id.textViewLogout);
        searchView = findViewById(R.id.searchViewFav);

        searchView.setIconified(false);
        searchView.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        edLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(personagensFavoritos.this, "Logout", Toast.LENGTH_LONG).show();
                startActivity(new Intent(personagensFavoritos.this, MainActivity.class));
            }
        });

        setInfo();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerAdapter.filtrar(s);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //igual acima, para digitação sem submit
                recyclerAdapter.filtrar(s);
                //notifica o adapter para alterações na lista
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setInfo() {
        Query query;

        //usuário logado no momento
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //limpando o array para a consulta
        listaPersonagens.clear();

        //o caminho da query no Firebase (todos os Personagens)
        query = databaseReference.child(user.getUid()).child("Personagens");

        //execução da query. Caso haja dados, cai no método onDataChange
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //este método é assíncrono, se não houver validação dos dados,
                //a lista será montada incorretamente pois não aguarda a consulta
                //assim, o if seguinte é necessário:
                if (dataSnapshot != null) {
                    for (DataSnapshot objDataSnapshot1 : dataSnapshot.getChildren()) {
                        Personagem f = objDataSnapshot1.getValue(Personagem.class);
                        listaPersonagens.add(f);
                    }
                    //setRecyclerView() para montagem e configuração da RecyclerView mas
                    //neste caso, setRecyclerView() tem que ser chamado aqui (dentro e ao final de onDataChange),
                    //de forma que é executado somente após os dados acima serem baixados do Firebase
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setRecyclerView() {

        recyclerAdapter = new MRecyclerAdapter(listaPersonagens);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        edRecyclerView.setLayoutManager(layoutManager);
        edRecyclerView.setAdapter(recyclerAdapter);
    }
}