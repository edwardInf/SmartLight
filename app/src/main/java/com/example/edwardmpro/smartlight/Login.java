package com.example.edwardmpro.smartlight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText usuario,contrasena;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText)findViewById(R.id.edt_user);
        contrasena = (EditText)findViewById(R.id.edt_password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void clickRegistro(View view){
        Intent i = new Intent(Login.this,Registro.class);
        startActivity(i);
    }
    public void clickIngresar(View view){
        final ProgressDialog progg = ProgressDialog.show(Login.this,"Espere por favor.... ","Procesando",true);
        (firebaseAuth.signInWithEmailAndPassword(usuario.getText().toString(),contrasena.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progg.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,"Registro Completo",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this,ListaDevices.class);
                            startActivity(i);

                        }else {
                            Log.e("Error",task.getException().toString());
                            Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
