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
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarUsuario extends AppCompatActivity {

    Button bt;
    EditText ed_email;
    FirebaseAuth authF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);

        bt = findViewById(R.id.button_rec_senha);
        ed_email = findViewById(R.id.editTex_email_valido);

        authF = FirebaseAuth.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.equals("")){
                    ed_email.setError("Preencha corretamente");
                    return;
                }

                authF.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RecuperarUsuario.this, "E-mail enviado.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RecuperarUsuario.this, MainActivity.class);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(RecuperarUsuario.this, "Erro.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}