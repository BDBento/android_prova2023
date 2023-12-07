package br.com.ifms.finalApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CriarUsuario extends AppCompatActivity {

    Button bt;

    EditText ed_nome, ed_email, ed_senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        bt = findViewById(R.id.button_cria_us);
        ed_nome = findViewById(R.id.edit_cria_nome);
        ed_email = findViewById(R.id.edit_cria_email);
        ed_senha = findViewById(R.id.edit_cria_senha);


    }
}