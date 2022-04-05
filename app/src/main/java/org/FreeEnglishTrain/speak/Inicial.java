package org.FreeEnglishTrain.speak;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.FreeEnglishTrain.database.BancoDeDados;
import org.FreeEnglishTrain.database.Frase;
import org.FreeEnglishTrain.utils.Globals;
import org.FreeEnglishTrain.utils.SimpleCallback;

import java.io.File;
import java.util.List;

public class Inicial extends Activity {

   Globals globals;
   private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
   private BancoDeDados mBancoDeDados;

   String url = "https://github.com/GabrielO-liveira/e-Train";


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_inicial);

      mBancoDeDados = new BancoDeDados(this);
      File database = getApplicationContext().getDatabasePath(BancoDeDados.NOMEDB);
      if (!database.exists()) {
         mBancoDeDados.getReadableDatabase();
         if (mBancoDeDados.copiaBanco(this)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Banco copiado com sucesso", Toast.LENGTH_SHORT);
            toast.show();
         } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Erro ao copiar o banco de dados", Toast.LENGTH_SHORT);
            toast.show();
         }
      }

   Button btnConfigurar = findViewById(R.id.btnConfigurar);
      Button btnReforco = findViewById(R.id.btnReforco);
      Button btnTema = findViewById(R.id.btnTemas);
      globals = (Globals) getApplicationContext();
      globals.initModel();
      mBancoDeDados = new BancoDeDados(this);
      btnConfigurar.setOnClickListener(v -> {
         Intent in = new Intent(Inicial.this, Configuracao.class);
         startActivity(in);
      });
      int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
      if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
      }

      //-- Aqui usamos um callback para fazer o modelo carregado nos avisar que terminou
      //TODO: Deixar o botao iniciar dsabilitado e só habilitar dentro dessa funcao
      globals.onVoskLoadedModel(new SimpleCallback() {
         @Override
         public void method() {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Modelo Carregado", Toast.LENGTH_SHORT);
            toast.show();

            btnReforco.setTextColor(Color.GREEN);
            btnTema.setTextColor(Color.GREEN);
         }
      });

      //-- Fim Callback --//
      btnTema.setOnClickListener(v -> {
         if (globals.modeloCarregado) {
            Intent in = new Intent(Inicial.this, SelecionarTema.class);
            startActivity(in);
         } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Modelo não carregado, por favor aguarde",
                    Toast.LENGTH_SHORT);
            toast.show();
         }
      });

      btnReforco.setOnClickListener(v -> {

         if (globals.modeloCarregado) {
            List<Frase> lstFrase;
            lstFrase = mBancoDeDados.getFraseByConfidance(0.99);
            if (lstFrase.size() == 0) {
               Toast toast = Toast.makeText(getApplicationContext(),
                       "Parabéns! Não encontramos nada para reforçar.", Toast.LENGTH_SHORT);
               toast.show();
            } else {
               Globals globals = (Globals) getApplicationContext();
               globals.setFrase(lstFrase);
               Intent in = new Intent(Inicial.this, Treino.class);
               startActivity(in);
            }
         } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Modelo não carregado, por favor aguarde", Toast.LENGTH_SHORT);
            toast.show();
         }
      });

      Button sobre = (Button)findViewById(R.id.btnSobre);
      sobre.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
         }
      });
   }
}


