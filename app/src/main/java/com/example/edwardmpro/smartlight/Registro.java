package com.example.edwardmpro.smartlight;

import android.app.ProgressDialog;
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

public class Registro extends AppCompatActivity {
    public EditText edt_Rus,edt_Rpass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edt_Rus = (EditText)findViewById(R.id.edt_Reguser);
        edt_Rpass = (EditText)findViewById(R.id.edt_Regpassword);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegistroFire(View view){
        final ProgressDialog progg = ProgressDialog.show(Registro.this,"Espere por favor.... ","Procesando",true);
        (firebaseAuth.createUserWithEmailAndPassword(edt_Rus.getText().toString(),edt_Rpass.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progg.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(Registro.this,"Registro Completo",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Log.e("Error",task.getException().toString());
                            Toast.makeText(Registro.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
