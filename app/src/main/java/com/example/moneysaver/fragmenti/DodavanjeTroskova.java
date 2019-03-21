package com.example.moneysaver.fragmenti;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneysaver.R;
import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyFragmentUtils;
import com.example.moneysaver.helper.MyRunnable;
import com.example.moneysaver.podaci.AktivnostiResultVM;
import com.example.moneysaver.podaci.Global;
import com.example.moneysaver.podaci.TrosakAddVM;

import java.util.Calendar;


public class DodavanjeTroskova extends Fragment {

    private Spinner spinner;
    private String[] test ;
    private AktivnostiResultVM podaci;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "MainActivity";
    private int godina=0;
    private int mjesec=0;
    private int dan=0;
    private Integer selektovanaAktivnostId;
    private EditText trosakUnos;


    public static DodavanjeTroskova newInstance() {
        DodavanjeTroskova fragment = new DodavanjeTroskova();
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

        View view=inflater.inflate(R.layout.fragment_dodavanje_troskova, container, false);

        spinner = (Spinner) view.findViewById(R.id.odabirAktivnostiUnos);

        trosakUnos = view.findViewById(R.id.trosakUnos);
        Button spremiTrosakBtn=view.findViewById(R.id.spremiTrosakBtn);
        Button odustaniBtn=view.findViewById(R.id.odustaniBtn);
        final TextView datumPicker=view.findViewById(R.id.datumPicker);

        datumPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                 int year = cal.get(Calendar.YEAR);
                 int month = cal.get(Calendar.MONTH);
                 int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                godina=year;mjesec=month;dan=day;
                datumPicker.setText(date);
            }
        };
        popuniPodatkeUSelectListi();

        spremiTrosakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               spremiTrosak();

            }
        });
        odustaniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, MjesecniPregledTroskova.newInstance(Global.prijavljeniKorisnik.KorisnikId));
            }
        });

        return view;
    }

    private void spremiTrosak() {
        Integer indexValue = spinner.getSelectedItemPosition();

        TrosakAddVM x=new TrosakAddVM();
        x.Dan=dan;
        x.Mjesec=mjesec;
        x.Godina=godina;
        boolean imaGreska=false;


        if(("nema").equals(podaci.imaPodataka)){
            Toast.makeText(getActivity(), "Niste dodali nijednu aktivnost.",Toast.LENGTH_LONG).show();
            imaGreska=true;
        }

        if(dan==0||mjesec==0||godina==0){
            Toast.makeText(getActivity(), "Niste odabrali datum!", Toast.LENGTH_SHORT).show();
            imaGreska=true;
        }

        if(trosakUnos==null || trosakUnos.length() == 0 || trosakUnos.equals("")||trosakUnos.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Pogrešno unesena vrijednost troška!", Toast.LENGTH_SHORT).show();
            imaGreska=true;
        }

        if(imaGreska==false)
        {
            Double d = Double.parseDouble(trosakUnos.getText().toString());
            x.Iznos= d;
            selektovanaAktivnostId=podaci.rows.get(indexValue).AktivnostId;
            x.AktivnostId=selektovanaAktivnostId;
            MyApiRequest.post(getActivity(), "api/Troskovi/PostTrosak", x, new MyRunnable<TrosakAddVM>() {
                public void run(TrosakAddVM x) {
                    Toast.makeText(getActivity(), "Trošak uspješno dodat.", Toast.LENGTH_SHORT).show();
                }
            });
            MyFragmentUtils.openAsReplace(getActivity(),R.id.mjestoFragment, MjesecniPregledTroskova.newInstance(Global.prijavljeniKorisnik.KorisnikId));
        }
    }

    private void popuniPodatkeUSelectListi() {
        Integer pr=Global.prijavljeniKorisnik.KorisnikId;
        MyApiRequest.get(getActivity(), "api/Aktivnosti/getAktivnostiByKorisnikID/"+pr, new MyRunnable<AktivnostiResultVM>() {

            @Override
            public void run(AktivnostiResultVM x) {

                if(("NemaPodataka").equals(x.imaPodataka))
                {
                    podaci=new AktivnostiResultVM();
                    podaci.imaPodataka="nema";
                }
                else{
                    podaci = x;
                    popuniSelectListu();
                }

            }
        });

    }

    private void popuniSelectListu() {


        int listaSize=podaci.rows.size();
        test=new String[listaSize];
        for(int i=0;i<listaSize;i++){

            test[i]=podaci.rows.get(i).Naziv;
        }

        ArrayAdapter<CharSequence> mSortAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, test);
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mSortAdapter);


    }

}
