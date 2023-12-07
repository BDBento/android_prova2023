package br.com.ifms.finalApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MRecyclerAdapter extends RecyclerView.Adapter<MRecyclerAdapter.MyViewHolder> {

    ArrayList<Personagem> personagemArrayList = new ArrayList<>();

    public MRecyclerAdapter(ArrayList<Personagem> personagemArrayList) {
        this.personagemArrayList = personagemArrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_;
        TextView status_;
        TextView species_;
        TextView gender_;
        ImageView image_;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name_ = itemView.findViewById(R.id.nameL);
            this.status_ = itemView.findViewById(R.id.statusL);
            this.species_ = itemView.findViewById(R.id.speciesL);
            this.gender_ = itemView.findViewById(R.id.genderL);
            this.image_ = itemView.findViewById(R.id.imageL);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(itemView.getContext(), "Removido", Toast.LENGTH_SHORT).show();
            personagemArrayList.remove(getLayoutPosition());
            notifyItemRemoved(getLayoutPosition());
            notifyItemRangeChanged(getLayoutPosition(), personagemArrayList.size());
        }
    }

    @NonNull
    @Override
    public MRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MRecyclerAdapter.MyViewHolder holder, int position) {
        String name = personagemArrayList.get(position).getName();
        String status = personagemArrayList.get(position).getStatus();
        String species = personagemArrayList.get(position).getSpecies();
        String gender = personagemArrayList.get(position).getGender();
        int image = personagemArrayList.get(position).getImage();

        holder.name_.setText(name);
        holder.status_.setText(status);
        holder.species_.setText(species);
        holder.gender_.setText(gender);
        holder.image_.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return personagemArrayList.size();
    }
}