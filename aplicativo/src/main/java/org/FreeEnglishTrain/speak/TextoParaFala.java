package org.FreeEnglishTrain.speak;

import java.util.Locale;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class TextoParaFala implements OnInitListener {

   public TextToSpeech t1;
   Context context;
   private Boolean readytoplay = false;


   public TextoParaFala(Context _context) {
      readytoplay = false;
      context = _context;
      t1 = new TextToSpeech(_context, this::onInit);
   }

   public boolean isReady()
   {
      return !t1.isSpeaking();
   }

   public void FalaAe(String textoToRead, Boolean _normalVelocity) {
      if (readytoplay) {
         if (_normalVelocity) {
            t1.setSpeechRate(1.0f);
         } else {
            t1.setSpeechRate(0.200f);
         }
         // Toast.makeText(context, textoToRead, Toast.LENGTH_SHORT).show();
         t1.stop();

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t1.speak(textoToRead, TextToSpeech.QUEUE_ADD, null, null);
         } else {
            t1.speak(textoToRead, TextToSpeech.QUEUE_ADD, null);
         }
      }

   }

   public void Desliga() {
      if (t1 != null) {
         t1.stop();
         t1.shutdown();
         readytoplay = false;
      }
   }

   @Override
   public void onInit(int status) {
      if (status != TextToSpeech.ERROR) {
         readytoplay = true;
         t1.setLanguage(Locale.ENGLISH);
         Toast.makeText(context, " tts inicializado", Toast.LENGTH_SHORT).show();
      }
   }
}