package com.example.moneysaver;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.KorisnikPostVM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registracija extends AppCompatActivity {


    private EditText usernameUnos;
    private EditText passwordUnos;
    private EditText passwordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);


        usernameUnos = findViewById(R.id.usernameUnos);
        passwordUnos = findViewById(R.id.passwordUnos);
        passwordConfirm=findViewById(R.id.passwordConfirm);
        TextView loginTxt=findViewById(R.id.loginTxt);

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_prebaciLogin();
            }
        });

        Button registracijaBtn = findViewById(R.id.registracijaBtn);
        registracijaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnRegistracijaClick();
            }
        });
    }

    private void do_prebaciLogin() {
        startActivity(new Intent(this, Login.class));

    }

    private void do_btnRegistracijaClick() {
        if(validacija())
        {
            KorisnikPostVM model = new KorisnikPostVM();
            model.KorisnickoIme = usernameUnos.getText().toString();
            model.LozinkaSalt = passwordUnos.getText().toString();

            MyApiRequest.post(this, "api/Korisnici", model, new MyRunnable<KorisnikPostVM>(){
                @Override
                public void run(KorisnikPostVM x)
                {
                    Toast.makeText(Registracija.this, "Uspješna registracija.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private boolean validacija() {

        AlertDialog adb = new AlertDialog.Builder(Registracija.this).create();
        adb.setTitle("Greška");
        adb.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        if(usernameUnos.getText().toString().length() < 4)
        {
            adb.setMessage("Korisnicko ime treba da sadrzi minimalno 4 karaktera.");
            adb.show();
            return false;
        }

        if(true)//lozinka
        {
            Pattern pattern;
            Matcher matcher;

            final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,}$";

            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(passwordUnos.getText().toString());

            if(matcher.matches() == false)
            {
                adb.setMessage("Password treba sadrzavat minimalno 6 karaktera, najmanje jedan broj i kombinaciju malih/velikih slova.");
                adb.show();
                return false;
            }
            if(!(passwordUnos.getText().toString().equals(passwordConfirm.getText().toString()))){
                adb.setMessage("Lozinke se ne poklapaju.");
                adb.show();
                return false;
            }
        }

        return true;
    }
}
