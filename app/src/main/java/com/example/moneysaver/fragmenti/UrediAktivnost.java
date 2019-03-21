package com.example.moneysaver.fragmenti;


import android.app.DialogFragment;
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
import com.example.moneysaver.podaci.AktivnostEditVM;
import com.example.moneysaver.podaci.Global;


public class UrediAktivnost extends DialogFragment {

    private int AktivnostId;
    private EditText nazivAktivnostiUnos;

    public static UrediAktivnost newInstance(int AktivnostId) {
        UrediAktivnost fragment = new UrediAktivnost();
        Bundle args = new Bundle();
        args.putInt("neki_key",AktivnostId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AktivnostId=getArguments().getInt("neki_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uredi_aktivnost, container, false);

        nazivAktivnostiUnos = view.findViewById(R.id.nazivAktivnostiUnos);
        Button spremiAktivnostBtn=view.findViewById(R.id.spremiAktivnostBtn);
        Button odustaniBtn=view.findViewById(R.id.odustaniBtn);

        odustaniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        spremiAktivnostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promjeniNaziv();

            }
        });

        return view;


    }

    private void promjeniNaziv() {
        AktivnostEditVM aktivnost=new AktivnostEditVM();
        aktivnost.AktivnostId=AktivnostId;
        aktivnost.Naziv=nazivAktivnostiUnos.getText().toString();
        if(aktivnost.Naziv==null || aktivnost.Naziv.length() == 0 || aktivnost.Naziv.equals("")||aktivnost.Naziv.trim().equals("")){
            Toast.makeText(getActivity(), "Niste unijeli naziv aktivnosti!", Toast.LENGTH_SHORT).show();
        }
        else{
        MyApiRequest.put(getActivity(), "api/Aktivnosti/PutAktivnost/",aktivnost, new MyRunnable<AktivnostEditVM>() {
                public void run(AktivnostEditVM aktivnost){
                    getDialog().dismiss();
                    MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,PregledAktivnosti.newInstance(Global.prijavljeniKorisnik.KorisnikId));
                    Toast.makeText(getActivity(), "Aktivnost uspješno uređena.", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}
