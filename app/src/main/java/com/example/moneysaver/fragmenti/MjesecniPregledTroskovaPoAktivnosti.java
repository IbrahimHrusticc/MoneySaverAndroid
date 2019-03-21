package com.example.moneysaver.fragmenti;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.moneysaver.R;
import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyFragmentUtils;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.Global;
import com.example.moneysaver.podaci.TrosakEditVM;
import com.example.moneysaver.podaci.TrosakMjesecniPregledDetaljnoAktivnostiVM;
import com.example.moneysaver.podaci.TrosakMjesecniPregledDetaljnoVM;

import java.text.DecimalFormat;


public class MjesecniPregledTroskovaPoAktivnosti extends Fragment {

    private ListView troskoviListaDetaljno;
    private TrosakMjesecniPregledDetaljnoAktivnostiVM podaci;
    private BaseAdapter adapter;
    private int KorisnikId;
    private int AktivnostId;
    private int Mjesec;
    private int Godina;
    private TextView naslovPregledaTroskaMjesecTxt;
    private TextView naslovAktivnostiTxt;

    public static MjesecniPregledTroskovaPoAktivnosti newInstance(int KorisnikId, int AktivnostId, int Mjesec, int Godina) {
        MjesecniPregledTroskovaPoAktivnosti fragment = new MjesecniPregledTroskovaPoAktivnosti();
        Bundle args = new Bundle();
        args.putInt("Korisnik_key",KorisnikId);
        args.putInt("Aktivnost_key",AktivnostId);
        args.putInt("Mjesec_key",Mjesec);
        args.putInt("Godina_key",Godina);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            KorisnikId=getArguments().getInt("Korisnik_key");
            AktivnostId=getArguments().getInt("Aktivnost_key");
            Mjesec=getArguments().getInt("Mjesec_key");
            Godina=getArguments().getInt("Godina_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_mjesecni_pregled_troskova_po_aktivnosti, container, false);

        troskoviListaDetaljno=view.findViewById(R.id.troskoviListaDetaljno);
        naslovPregledaTroskaMjesecTxt = view.findViewById(R.id.naslovPregledaTroskaMjesecTxt);
        naslovAktivnostiTxt = view.findViewById(R.id.naslovAktivnostiTxt);
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

        MyApiRequest.get(getActivity(), "api/Troskovi/getMjesecniTroskoviDetaljnoByDatumAktivnost/"+ KorisnikId+"/"+AktivnostId+"/"+Mjesec+"/"+Godina, new MyRunnable<TrosakMjesecniPregledDetaljnoAktivnostiVM>() {

            @Override
            public void run(TrosakMjesecniPregledDetaljnoAktivnostiVM x) {
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

                TrosakMjesecniPregledDetaljnoAktivnostiVM x = podaci.lista.get(position);

                textFirst.setText(x.Datum);
                DecimalFormat format = new DecimalFormat("0.00");

                textSecond.setText(String.valueOf(format.format(x.Iznos)+" KM"));

                naslovPregledaTroskaMjesecTxt.setText(Mjesec+"/"+Godina);
                naslovAktivnostiTxt.setText("AKTIVNOST - "+ x.Aktivnost);


                return view;
            }
        };
        troskoviListaDetaljno.setAdapter(adapter);
        troskoviListaDetaljno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrosakMjesecniPregledDetaljnoAktivnostiVM x =podaci.lista.get(position);

                MyFragmentUtils.openAsDialog(getActivity(), UrediTrosak.newInstance(x.TrosakId,AktivnostId,Mjesec,Godina));
            }
        });

        troskoviListaDetaljno.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TrosakMjesecniPregledDetaljnoAktivnostiVM x = podaci.lista.get(position);

                do_onItemClickDelete(x);
                return true;
            }
        });


    }

    private void do_onItemClickDelete(final TrosakMjesecniPregledDetaljnoAktivnostiVM x) {

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage("Da li ste sigurni da želite obrisati trošak?");

        adb.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                MyApiRequest.delete(getActivity(), "api/Troskovi/DeleteTrosak/" + x.TrosakId, new MyRunnable<Integer>(){

                    @Override
                    public void run(Integer o) {
                        podaci.lista.remove(x);
                        adapter.notifyDataSetChanged();
                        MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment,MjesecniPregledTroskova.newInstance(Global.prijavljeniKorisnik.KorisnikId));
                        Toast.makeText(getActivity(), "Trošak uspješno obrisan.", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });

        adb.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.show();
    }

}
