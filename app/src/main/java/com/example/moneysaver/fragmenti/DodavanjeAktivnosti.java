package com.example.moneysaver.fragmenti;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneysaver.R;
import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyFragmentUtils;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.AktivnostAddVM;
import com.example.moneysaver.podaci.Global;


public class DodavanjeAktivnosti extends Fragment {

    private EditText nazivAktivnostiUnos;

    public static DodavanjeAktivnosti newInstance() {
        DodavanjeAktivnosti fragment = new DodavanjeAktivnosti();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_dodavanje_aktivnosti, container, false);

        nazivAktivnostiUnos = view.findViewById(R.id.nazivAktivnostiUnos);
        Button spremiAktivnostBtn=view.findViewById(R.id.spremiAktivnostBtn);
        Button odustaniBtn=view.findViewById(R.id.odustaniBtn);


        spremiAktivnostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_spremiAktivnost();
            }
        });

        odustaniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,PregledAktivnosti.newInstance(Global.prijavljeniKorisnik.KorisnikId));
            }
        });



        return view;
    }

    private void do_spremiAktivnost() {
        AktivnostAddVM x=new AktivnostAddVM();
        x.KorisnikId= Global.prijavljeniKorisnik.KorisnikId;
        x.naziv= nazivAktivnostiUnos.getText().toString();

        if(x.naziv==null || x.naziv.length() == 0 || x.naziv.equals("")||x.naziv.trim().equals("")){
            Toast.makeText(getActivity(), "Niste unijeli naziv aktivnosti!", Toast.LENGTH_SHORT).show();
        }

        else{
            MyApiRequest.post(getActivity(), "api/Aktivnosti/PostAktivnost", x, new MyRunnable<AktivnostAddVM>() {
                public void run(AktivnostAddVM x) {
                    Toast.makeText(getActivity(), "Aktivnost uspješno dodata.", Toast.LENGTH_SHORT).show();
                }
            });
            MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,PregledAktivnosti.newInstance(x.KorisnikId));
        }

    }

}
