package org.FreeEnglishTrain.database;

public class Tema {

    public int id;
    public String Tema;

    public Tema(int _id, String _tema) {
        this.id = _id;
        this.Tema = _tema;
    }

    @Override
    public String toString() {
        return Tema;
    }
}
