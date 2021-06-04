package com.example.amal.gstock;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static  com.example.amal.gstock.data.SharedHelper.sha256;


public class MainActivity extends AppCompatActivity {
    EditText _txtLogin,_txtPassword;
    Button _btnConnection;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _txtLogin = (EditText) findViewById(R.id.txtLogin);
        _txtPassword = (EditText) findViewById(R.id.txtPassword);
        _btnConnection= (Button) findViewById(R.id.btnConnection);

        //création de la base de données ou ouverture de connexion
        db = openOrCreateDatabase("product",MODE_PRIVATE,null);

        //création de la table user
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS(login varchar primary key, password varchar);");

        //si la table users est vide alors ajouter l'utilisateur admin avec mot de passe "111"
        SQLiteStatement s = db.compileStatement("select count(*) from users;");
        long c = s.simpleQueryForLong();
        if(c==0){
            db.execSQL("insert into users(login, password) values (?,?)",new String[]{"admin", sha256("111")});
        }
        _btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLogin = _txtLogin.getText().toString();
                String strPwd = _txtPassword.getText().toString();

                //créer un curseur pour récupérer le résultat de la requéte select
                Cursor cur =db.rawQuery("select password from users where login =?", new String[]{strLogin});
                try {
                    cur.moveToFirst();
                    String p = cur.getString(cur.getColumnIndex("password"));
                    if(p.equals(sha256(strPwd))){
                        Toast.makeText(getApplicationContext(),"Bienvenue" + strLogin, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),CatalogActivity.class);
                        startActivity(i);
                    }else {
                        _txtLogin.setText("");
                        _txtPassword.setText("");
                        Toast.makeText(getApplicationContext(),"Echec de connexion", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    _txtLogin.setText("");
                    _txtPassword.setText("");
                    Toast.makeText(getApplicationContext(),"Utilisateur Inexistant", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

    }


}

