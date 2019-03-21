package com.example.moneysaver.fragmenti;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moneysaver.R;
import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyFragmentUtils;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.Global;
import com.example.moneysaver.podaci.TrosakMjesecniPregledDetaljnoVM;

import java.text.DecimalFormat;


public class MjesecniPregledTroskovaDetaljno extends Fragment {

    private ListView mjesecneAktivnostiTroskoviListaDetaljno;
    private TrosakMjesecniPregledDetaljnoVM podaci;
    private BaseAdapter adapter;
    private int KorisnikId;
    private int TrosakId;
    private TextView naslovPregledaTroskaMjesecTxt;
    private TextView ukupnoTroskoviUnos;

    public static MjesecniPregledTroskovaDetaljno newInstance(int KorisnikId,int TrosakId) {
        MjesecniPregledTroskovaDetaljno fragment = new MjesecniPregledTroskovaDetaljno();
        Bundle args = new Bundle();
        args.putInt("novi_key",KorisnikId);
        args.putInt("stari_key",TrosakId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            KorisnikId=getArguments().getInt("novi_key");
            TrosakId=getArguments().getInt("stari_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_mjesecni_pregled_troskova_detaljno, container, false);

        mjesecneAktivnostiTroskoviListaDetaljno = view.findViewById(R.id.mjeseciListaDetaljno);
        naslovPregledaTroskaMjesecTxt = view.findViewById(R.id.naslovPregledaTroskaMjesecTxt);
        ukupnoTroskoviUnos = view.findViewById(R.id.ukupnoTroskoviUnos);

        FloatingActionButton fab=view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, DodavanjeTroskova.newInstance());
            }
        });

        popuniPodacima();

        return view;
    }

    private void popuniPodacima() {

        MyApiRequest.get(getActivity(), "api/Troskovi/getMjesecniTroskoviDetaljnoByTrosakAktivnost/"+ KorisnikId+"/"+TrosakId, new MyRunnable<TrosakMjesecniPregledDetaljnoVM>() {

            @Override
            public void run(TrosakMjesecniPregledDetaljnoVM x) {
                podaci=x;

                popuniListu();


            }
        });

    }

    private void popuniListu() {

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return podaci.lista.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    view = inflater.inflate(R.layout.lista_stavka_troskovi, parent, false);
                }
                TextView textFirst = view.findViewById(R.id.textFirst);
                TextView textSecond = view.findViewById(R.id.textSecond);

                TrosakMjesecniPregledDetaljnoVM x = podaci.lista.get(position);

                textFirst.setText(x.Aktivnost);
                DecimalFormat formmat = new DecimalFormat("0.00");
                textSecond.setText(String.valueOf(formmat.format(x.Trosak))+" KM");

                naslovPregledaTroskaMjesecTxt.setText(podaci.MjesecGodina.toString());

                DecimalFormat format = new DecimalFormat("0.00");

                ukupnoTroskoviUnos.setText("Ukupno: "+format.format(podaci.Ukupno)+" KM");
                return view;
            }
        };
        mjesecneAktivnostiTroskoviListaDetaljno.setAdapter(adapter);
        mjesecneAktivnostiTroskoviListaDetaljno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrosakMjesecniPregledDetaljnoVM x =podaci.lista.get(position);

                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, MjesecniPregledTroskovaPoAktivnosti.newInstance(Global.prijavljeniKorisnik.KorisnikId,x.AktivnostId,x.Mjesec,x.Godina));
            }
        });



    }

}
