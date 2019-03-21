package com.example.moneysaver.podaci;

import java.io.Serializable;
import java.util.List;

public class AktivnostiResultVM implements Serializable {

    public static class Row implements Serializable {
        public Integer AktivnostId;
        public String Naziv;
    }
    public String imaPodataka;
    public List<Row> rows;

}
