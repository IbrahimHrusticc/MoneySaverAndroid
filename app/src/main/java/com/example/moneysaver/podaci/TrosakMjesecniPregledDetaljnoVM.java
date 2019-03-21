package com.example.moneysaver.podaci;

import java.io.Serializable;
import java.util.List;

public class TrosakMjesecniPregledDetaljnoVM implements Serializable {
    public Integer KorisnikId;
    public Double Trosak;
    public Integer AktivnostId;
    public String Aktivnost;
    public Double Ukupno;
    public String MjesecGodina;
    public Integer Mjesec;
    public Integer Godina;
    public List<TrosakMjesecniPregledDetaljnoVM> lista;
}
