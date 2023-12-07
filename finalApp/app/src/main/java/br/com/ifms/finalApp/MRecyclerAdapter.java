package br.com.ifms.finalApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//Adapter do Recycler View
public class MRecyclerAdapter extends RecyclerView.Adapter<MRecyclerAdapter.MViewHolder> {

    ArrayList<Personagem> listaPersonagemLocal;
    ArrayList<Personagem> listaPersonagemCopia;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public MRecyclerAdapter(ArrayList<Personagem> listaPersonagemLocal_) {
        this.listaPersonagemLocal = listaPersonagemLocal_;
        listaPersonagemCopia = new ArrayList<>(listaPersonagemLocal);
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        String name = listaPersonagemLocal.get(position).getName();
        String status = listaPersonagemLocal.get(position).getStatus();
        String species = listaPersonagemLocal.get(position).getSpecies();
        String gender = listaPersonagemLocal.get(position).getGender();
        String image = listaPersonagemLocal.get(position).getImage();


        holder.name_.setText(name);
        holder.status_.setText(status);
        holder.species_.setText(species);
        holder.gender_.setText(gender);

        Glide.with(holder.image_.getContext()).load(image).into(holder.image_);
    }

    @Override
    public int getItemCount() {
        return listaPersonagemLocal.size();
    }

    //class ViewHolder de conexão com os elementos da tela e config do Firebase Realtime Database
    public class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name_, status_, species_, gender_;
        ImageView image_;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            name_ = itemView.findViewById(R.id.nameL);
            status_ = itemView.findViewById(R.id.statusL);
            name_ = itemView.findViewById(R.id.nameL);
            species_ = itemView.findViewById(R.id.speciesL);

            image_ = itemView.findViewById(R.id.imageL);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getContext().toString().contains("listaDePersonagens")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar Personagem")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setIcon(R.drawable.ic_baseline_favorite_border_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Personagem salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                                inserirEm(getLayoutPosition());

                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
            else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Remover")
                        .setMessage("Confirma remover dos favoritos?")
                        .setIcon(R.drawable.ic_baseline_delete_outline_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, remover do Firebase, método "removerEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Personagem removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                removerEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
        }
        private void inserirEm(int layoutPosition) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Personagem pers = listaPersonagemLocal.get(layoutPosition);

            databaseReference.child(user.getUid()).
                    child("Personagens").
                    child(pers.getName()).
                    setValue(pers);
        }

    }

    public void removerEm(int layoutPosition) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Personagem per = listaPersonagemLocal.get(layoutPosition);
        listaPersonagemLocal.clear();

        databaseReference.child(user.getUid()).child("Personagens").
                child(per.getName()).
                removeValue();
    }

    public void filtrar(String text) {
        listaPersonagemLocal.clear();
        if (text.isEmpty()) {
            listaPersonagemLocal.addAll(listaPersonagemCopia);
        } else {
            text = text.toLowerCase();
            for (Personagem personagem : listaPersonagemCopia) {
                if (personagem.getName().toLowerCase().contains(text) ||
                        personagem.getStatus().toLowerCase().contains(text) ||
                        personagem.getSpecies().toLowerCase().contains(text) ||
                        personagem.getGender().toLowerCase().contains(text)) {
                    listaPersonagemLocal.add(personagem);
                }
            }
        }
    }
}