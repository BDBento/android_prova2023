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

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();
                // enviar email para recuperacao de senha
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(RecuperarUsuario.this, "Email de recuperacao de senha enviado.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}