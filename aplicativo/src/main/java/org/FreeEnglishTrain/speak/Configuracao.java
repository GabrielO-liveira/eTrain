package org.FreeEnglishTrain.speak;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.FreeEnglishTrain.database.BancoDeDados;
import org.FreeEnglishTrain.speak.R;


public class Configuracao extends Activity {

    private BancoDeDados mBancoDeDados;
    Button zerarDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBancoDeDados = new BancoDeDados(this);
        setContentView(R.layout.activity_configuracao);
        zerarDb = findViewById(R.id.btnZerarDb);
        zerarDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View Button) {
                mBancoDeDados.zerarDB();
            }
        });
    }
}