package com.example.moneysaver.fragmenti;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.moneysaver.podaci.AktivnostiResultVM;


public class PregledAktivnosti extends Fragment {


    private ListView aktivnostiLista;
    private AktivnostiResultVM podaci;
    private Integer korisnikID;
    private  BaseAdapter adapter;

    public static PregledAktivnosti newInstance(Integer korisnikID) {
        PregledAktivnosti fragment = new PregledAktivnosti();
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
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_pregled_aktivnosti, container, false);

        aktivnostiLista = view.findViewById(R.id.aktivnostiLista);
        FloatingActionButton fab=view.findViewById(R.id.fab);

        popuniPodacima();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, DodavanjeAktivnosti.newInstance());
            }
        });


        return view;
    }

    private void do_onItemClickDelete(final AktivnostiResultVM.Row x) {

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage("Da li ste sigurni da želite obrisati aktivnost, ali ujedno i SVE TROŠKOVE evidentirane na ovoj aktivnosti?");

        adb.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                MyApiRequest.delete(getActivity(), "api/Aktivnosti/DeleteAktivnost/" + x.AktivnostId, new MyRunnable<Integer>(){

                    @Override
                    public void run(Integer o) {
                        podaci.rows.remove(x);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Aktivnost i svi troškovi na njoj uspješno obrisani.", Toast.LENGTH_SHORT).show();


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

    private void popuniPodacima() {
        MyApiRequest.get(getActivity(), "api/Aktivnosti/getAktivnostiByKorisnikID/"+ korisnikID, new MyRunnable<AktivnostiResultVM>() {

            @Override
            public void run(AktivnostiResultVM x) {

                if(("NemaPodataka").equals(x.imaPodataka))
                {
                    Toast.makeText(getActivity(), "Niste dodali nijednu aktivnost.",Toast.LENGTH_LONG).show();
                }
                else{
                    podaci = x;
                    popuniListu();
                }

            }
        });
    }

    private void popuniListu() {

             adapter=new BaseAdapter() {
                 @Override
                 public int getCount() {
                     return podaci.rows.size();
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

                         view = inflater.inflate(R.layout.lista_stavka_aktivnosti, parent, false);
                     }
                     TextView nazivAktivnosti = view.findViewById(R.id.nazivAktivnosti);

                     AktivnostiResultVM.Row x = podaci.rows.get(position);

                     nazivAktivnosti.setText(x.Naziv);

                     return view;
                 }
             };
             aktivnostiLista.setAdapter(adapter);


        aktivnostiLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AktivnostiResultVM.Row x = podaci.rows.get(position);
                do_onItemClickDelete(x);
                return true;
            }
        });
        aktivnostiLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AktivnostiResultVM.Row x=podaci.rows.get(position);
                MyFragmentUtils.openAsDialog(getActivity(),UrediAktivnost.newInstance(x.AktivnostId.intValue()));
            }
        });


    }

}
