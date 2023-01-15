package com.example.proyectoev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button logingoogle;

    private EditText mail, contraseña;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logingoogle = findViewById(R.id.logingoogle);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null){
            navigateToSecondActivity();
        }

        logingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mail=findViewById(R.id.mail);
        contrasena =findViewById(R.id.contraseña);

        spinner = findViewById(R.id.idiomas);
        Button pantallaregistro = (Button) findViewById(R.id.pantallaregistro);


        pantallaregistro.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        });
    }

    void signIn() {
        Intent singInIntent = gsc.getSignInIntent();
        startActivityForResult(singInIntent,1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            }catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"F",Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(Login.this,MainUser.class);
        startActivity(intent);
    }




    public void consultaUsuarioPorMailYPassword(View v) {
        RegistroUsuarioSQLite admin = new RegistroUsuarioSQLite(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String eMail = mail.getText().toString();
        //String password = contraseña.getText().toString();
        Cursor fila = bd.rawQuery(
                "select mail,contraseña from UsuarioRegistrado where mail='" + eMail, null);
        if (fila.moveToFirst()) {
            mail.setText(fila.getString(0));
            contrasena.setText(fila.getString(1));
            Toast.makeText(this, "Correo"+mail.toString()+"Pass"+ contrasena.toString(),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainUser.class);
            startActivity(intent);
        } else
            Toast.makeText(this, "Correo o contraseña incorrecta.",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }


}