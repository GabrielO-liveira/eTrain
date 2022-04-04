package org.FreeEnglishTrain.database;

public class Frase {

    public int id;
    public String frase;
    public int  idTema;
    public double  nrCondidance;
    public String traducao;

    public Frase(int _id, String _frase, int _idTema, double _nrCondidance, String _traducao ) {
        this.id = _id;
        this.frase = _frase;
        this.idTema = _idTema;
        this.nrCondidance = _nrCondidance;
        this.traducao = _traducao;

    }
}
