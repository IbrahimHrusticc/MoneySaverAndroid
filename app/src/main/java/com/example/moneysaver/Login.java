package com.example.moneysaver;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.AutentifikacijaLoginPostVM;
import com.example.moneysaver.podaci.AutentifikacijaResultVM;
import com.example.moneysaver.podaci.Global;


public class Login extends AppCompatActivity {

    private EditText usernameUnos;
    private EditText passwordUnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameUnos = findViewById(R.id.usernameUnos);
        passwordUnos = findViewById(R.id.passwordUnos);

        findViewById(R.id.prijavaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnPrijavaClick();
            }
        });

        findViewById(R.id.registracijaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Registracija.class));
            }
        });
    }

    private void do_btnPrijavaClick() {
        if(validacija()){
            String username= usernameUnos.getText().toString();
            String password= passwordUnos.getText().toString();

            AutentifikacijaLoginPostVM model = new AutentifikacijaLoginPostVM(username,password);

            MyApiRequest.get(this, "api/Autentifikacija/Login/" + username + "/" + password, new MyRunnable<AutentifikacijaResultVM>() {
                @Override
                public void run(AutentifikacijaResultVM x) {
                    provjeriLogin(x);
                }
            });
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
            alertDialog.setTitle("Greška");
            alertDialog.setMessage("Unesite korisničko ime i lozinku.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }



    }

    private void provjeriLogin(AutentifikacijaResultVM x) {
        if (("pogresanLogin".equals(x.KorisnickoIme))||("pogesanLogin".equals(x.LozinkaSalt))) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Pogrešno korisničko ime/lozinka.", Snackbar.LENGTH_LONG).show();
        }

        else {
            Global.prijavljeniKorisnik = x;

            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean validacija() {
        if (usernameUnos.getText().toString().isEmpty())
            return false;
        if (passwordUnos.getText().toString().isEmpty())
            return false;
        return true;
    }
    public void onBackPressed() {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }
}
