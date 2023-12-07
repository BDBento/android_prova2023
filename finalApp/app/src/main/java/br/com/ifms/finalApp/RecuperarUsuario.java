package br.com.ifms.finalApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RecuperarUsuario extends AppCompatActivity {

    Button bt;

    EditText ed_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);

        bt = findViewById(R.id.button_rec_senha);

        ed_email = findViewById(R.id.editTex_email_valido);

    }
}