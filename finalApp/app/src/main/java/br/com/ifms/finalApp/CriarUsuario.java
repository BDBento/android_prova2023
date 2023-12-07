package br.com.ifms.finalApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CriarUsuario extends AppCompatActivity {

    Button bt;
    EditText ed_nome, ed_email, ed_senha;
    FirebaseAuth authF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        bt = findViewById(R.id.button_cria_us);
        ed_nome = findViewById(R.id.edit_cria_nome);
        ed_email = findViewById(R.id.edit_cria_email);
        ed_senha = findViewById(R.id.edit_cria_senha);

        authF = FirebaseAuth.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarU(ed_email.getText().toString(), ed_senha.getText().toString());
            }
        });
    }

    private void criarU(String email, String senha) {

        if (ed_nome.getText().toString().equals("")) {
            ed_nome.setError("Preencha corretamente");
            ed_nome.requestFocus();
            return;
        }

        if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("Preencha corretamente");
            ed_email.requestFocus();
            return;
        }

        if (senha.equals("")) {
            ed_senha.setError("Preencha corretamente");
            ed_senha.requestFocus();
            return;
        }

        authF.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                    usuario.sendEmailVerification();
                    Toast.makeText(CriarUsuario.this, "Usuário criado. Verifique seu e-mail.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CriarUsuario.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao criar usuário", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}