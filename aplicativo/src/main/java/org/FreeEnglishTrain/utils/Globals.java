package org.FreeEnglishTrain.utils;

import android.app.Application;

import org.FreeEnglishTrain.database.Frase;
import org.FreeEnglishTrain.speak.TextoParaFala;
import org.vosk.Model;

import org.vosk.android.StorageService;

import java.util.ArrayList;
import java.util.List;

public class Globals extends Application {
   private List<Frase> lstFrase;
   private TextoParaFala ttsAndroid;
   public Model model;
   public boolean modeloCarregado ;
   private SimpleCallback callback_loadedmodel;

   @Override
   public void onCreate() {
      super.onCreate();
      ttsAndroid = new TextoParaFala(this);
      modeloCarregado = false;
   }

   @Override
   public void onTerminate() {
      super.onTerminate();
      if (ttsAndroid != null) {
         ttsAndroid.Desliga();
      }
   }

   public void FalaAe(String s, boolean velocidadeNormal) {
      if (ttsAndroid == null) {
         ttsAndroid = new TextoParaFala(this);
      }
      ttsAndroid.FalaAe(s, velocidadeNormal);
   }

   public boolean FalaeIsReady()
   {
      if (ttsAndroid == null)
         return false;
      return ttsAndroid.isReady();
   }

   public void setFrase(List<Frase> _lstFrase) {
      lstFrase = new ArrayList<Frase>();
      for (int i = 0; i < _lstFrase.size(); i++) {
         lstFrase.add(_lstFrase.get(i));
      }
   }

   public List<Frase> getFrase() {
      return lstFrase;
   }

   //----------------Vosk----------------//
   public void initModel() {
      StorageService.unpack(this, "model-en-us", "model",
              (model) -> {
                 this.model = model;
                 modeloCarregado =true;
                 this.executeNow();
              },
              (exception) -> setErrorState("Failed to unpack the model" + exception.getMessage()));
   }


   public void onVoskLoadedModel(SimpleCallback _simplesChamada){
      callback_loadedmodel = _simplesChamada;
   }

   public void executeNow(){
      callback_loadedmodel.method();
   }

   private void setErrorState(String s) {
      modeloCarregado = false;
   }


}
