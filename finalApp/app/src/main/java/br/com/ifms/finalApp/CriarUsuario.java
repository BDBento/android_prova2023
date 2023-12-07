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


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();
                String senha = ed_senha.getText().toString();
                // cria usuário
                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                            usuario.sendEmailVerification();

                            Toast.makeText(CriarUsuario.this, "Usuário criado. Verifique seu e-mail.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CriarUsuario.this, "Usuário NÃO foi criado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Usuário NÃO foi criado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    

    }
}