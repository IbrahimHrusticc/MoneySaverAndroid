package com.example.moneysaver.fragmenti;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import com.example.moneysaver.podaci.TrosakMjesecniPregledVM;


public class MjesecniPregledTroskova extends Fragment {

    private ListView listaMjeseci;
    private Integer korisnikID;
    private TrosakMjesecniPregledVM podaci;
    private BaseAdapter adapter;

    public static MjesecniPregledTroskova newInstance(Integer korisnikID) {
        MjesecniPregledTroskova fragment = new MjesecniPregledTroskova();
        Bundle args = new Bundle();
        args.putInt("neki_key",korisnikID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            korisnikID=getArguments().getInt("neki_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_mjesecni_pregled_troskova, container, false);

        listaMjeseci = view.findViewById(R.id.mjeseciLista);
        FloatingActionButton fab=view.findViewById(R.id.fab);
        popuniPodatke();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, DodavanjeTroskova.newInstance());
            }
        });

        return view;
    }

    private void popuniPodatke() {

        MyApiRequest.get(getActivity(), "api/Troskovi/getMjesecniTroskoviByKorisnikID/"+ korisnikID, new MyRunnable<TrosakMjesecniPregledVM>() {

            @Override
            public void run(TrosakMjesecniPregledVM x) {
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

                TrosakMjesecniPregledVM x = podaci.lista.get(position);

                textFirst.setText(x.MjesecGodina);
                textSecond.setText(String.valueOf(x.Trosak)+" KM");

                return view;
            }
        };
        listaMjeseci.setAdapter(adapter);

        listaMjeseci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrosakMjesecniPregledVM x =podaci.lista.get(position);
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,MjesecniPregledTroskovaDetaljno.newInstance(Global.prijavljeniKorisnik.KorisnikId,x.TrosakId));
            }
        });



    }

}