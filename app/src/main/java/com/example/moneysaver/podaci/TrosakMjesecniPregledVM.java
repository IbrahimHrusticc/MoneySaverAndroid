package com.example.moneysaver.podaci;

import java.io.Serializable;
import java.util.List;

public class TrosakMjesecniPregledVM implements Serializable
{
        public Integer KorisnikId;
        public Double Trosak;
        public String MjesecGodina;
        public Integer TrosakId;
        public List<TrosakMjesecniPregledVM> lista;
}

