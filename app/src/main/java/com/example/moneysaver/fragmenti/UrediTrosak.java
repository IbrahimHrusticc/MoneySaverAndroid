package com.example.moneysaver.fragmenti;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.moneysaver.podaci.TrosakEditVM;
import com.example.moneysaver.podaci.TrosakMjesecniPregledVM;


public class UrediTrosak extends DialogFragment {

    private Integer TrosakID;
    private EditText nazivTrosakUnos;
    private Integer AktivnostID;
    private Integer Mjesec;
    private Integer Godina;

    public static UrediTrosak newInstance(Integer TrosakID,Integer AktivnostID, Integer Mjesec, Integer Godina) {
        UrediTrosak fragment = new UrediTrosak();
        Bundle args = new Bundle();
        args.putInt("trosak_id",TrosakID);
        args.putInt("aktivnost_id",AktivnostID);
        args.putInt("mjesec",Mjesec);
        args.putInt("godina",Godina);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            TrosakID =getArguments().getInt("trosak_id");
            AktivnostID=getArguments().getInt("aktivnost_id");
            Mjesec=getArguments().getInt("mjesec");
            Godina=getArguments().getInt("godina");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_uredi_trosak, container, false);

        nazivTrosakUnos = view.findViewById(R.id.nazivTrosakUnos);

        Button spremiTrosakBtn=view.findViewById(R.id.spremiTrosakBtn);
        Button odustaniBtn=view.findViewById(R.id.odustaniBtn);

        spremiTrosakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiUredjeniTrosak();
            }
        });

        odustaniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }

    private void spremiUredjeniTrosak() {
        TrosakEditVM x=new TrosakEditVM();
        x.TrosakId=TrosakID;
        if(nazivTrosakUnos==null || nazivTrosakUnos.length() == 0 || nazivTrosakUnos.equals("")||nazivTrosakUnos.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Neispravno unesena vrijednost!", Toast.LENGTH_SHORT).show();
        }
        else{
            Double d = Double.parseDouble(nazivTrosakUnos.getText().toString());
            x.Iznos=d;
            MyApiRequest.put(getActivity(), "api/Troskovi/PutTrosak/",x, new MyRunnable<TrosakEditVM>() {
            public void run(TrosakEditVM x){
                getDialog().dismiss();
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,MjesecniPregledTroskovaPoAktivnosti.newInstance(Global.prijavljeniKorisnik.KorisnikId,AktivnostID,Mjesec,Godina));
                Toast.makeText(getActivity(), "Trošak uspješno uređen.", Toast.LENGTH_SHORT).show();

            }
        });
        }
    }

}
