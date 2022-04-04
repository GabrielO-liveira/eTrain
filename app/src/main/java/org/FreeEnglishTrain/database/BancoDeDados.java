package org.FreeEnglishTrain.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class BancoDeDados extends SQLiteOpenHelper {
   public static final String NOMEDB = "eTrain.db";
   public static String LOCALDB = "/data/data/org.vosk.demo/databases/"; // Context.getFilesDir().getPath();

   private static final int VERSION = 15;

   private final Context mContext;
   private SQLiteDatabase mSQSqLiteDatabase;


   public boolean copiaBanco(Context context) {
      try {
         InputStream inputStream = context.getAssets().open(BancoDeDados.NOMEDB);
         String outFile = BancoDeDados.LOCALDB + BancoDeDados.NOMEDB;
         OutputStream outputStream = new FileOutputStream(outFile);
         byte[] buff = new byte[1024];
         int legth = 0;
         while ((legth = inputStream.read(buff)) > 0) {
            outputStream.write(buff, 0, legth);
         }
         outputStream.flush();
         outputStream.close();
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   public BancoDeDados(Context context) {
      super(context, NOMEDB, null, VERSION);
      mContext = context;
   }

   public void openDataBase() {
      String dbPath = mContext.getDatabasePath(NOMEDB).getPath();
      if (mSQSqLiteDatabase != null && mSQSqLiteDatabase.isOpen()) {
         return;
      }
      mSQSqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
   }


   public List<Tema> getAllTemas() {
      Log.i("etrain", "call BancoDeDados.getAllTemas");
      try {
         openDataBase();
         mSQSqLiteDatabase = this.getWritableDatabase();
         List<Tema> listTema = new ArrayList<>();
         String sql = "SELECT * FROM Tema ";
         Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, null);
         if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
               do {
                  Tema p = new Tema(cursor.getInt(0), cursor.getString(1));
                  listTema.add(p);
               } while (cursor.moveToNext());
            }
         }
         cursor.close();
         return listTema;
      } finally {
         if (mSQSqLiteDatabase != null)
            mSQSqLiteDatabase.close();
      }
   }


   public List<Frase> getFraseByConfidance(double _nrConfidance) {
      try {
         openDataBase();
         mSQSqLiteDatabase = this.getWritableDatabase();
         List<Frase> listFrase = new ArrayList<>();
         String sql = String.format("SELECT * FROM Frase WHERE nrConfidance < '%.2f'", _nrConfidance);
         Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, null);
         if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
               do {
                  Frase p = new Frase(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getString(4));
                  listFrase.add(p);
               } while (cursor.moveToNext());
            }
         }
         cursor.close();
         return listFrase;
      } finally {
         if (mSQSqLiteDatabase != null)
            mSQSqLiteDatabase.close();
      }
   }


   public List<Frase> getFraseByTema(int _idTema) {
      try {
         openDataBase();
         mSQSqLiteDatabase = this.getWritableDatabase();
         List<Frase> listFrase = new ArrayList<>();
         // String sql = String.format("SELECT * FROM Frase WHERE Idtema  = %d and id = 13 ", _idTema);
         String sql = String.format("SELECT * FROM Frase WHERE Idtema  = %d  ", _idTema);
         Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, null);
         if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
               do {
                  Frase p = new Frase(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getString(4));
                  listFrase.add(p);
               } while (cursor.moveToNext());
            }
         }
         cursor.close();
         return listFrase;
      } finally {
         if (mSQSqLiteDatabase != null)
            mSQSqLiteDatabase.close();
      }

   }


   public void zerarDB() {
      try {
         openDataBase();
         mSQSqLiteDatabase = this.getWritableDatabase();
         String sql = "UPDATE Frase set nrConfidance  = NULL";
         mSQSqLiteDatabase.execSQL(sql);
      } finally {
         if (mSQSqLiteDatabase != null)
            mSQSqLiteDatabase.close();
      }
   }

   public void atualizaConfianca(int _idFrase, double _nrConfidance) {
      try {
         openDataBase();
         mSQSqLiteDatabase = this.getWritableDatabase();
         String sql = String.format("UPDATE Frase set nrConfidance ='%.2f'  WHERE Id  = %d", _nrConfidance, _idFrase);
         mSQSqLiteDatabase.execSQL(sql);
      } finally {
         if (mSQSqLiteDatabase != null)
            mSQSqLiteDatabase.close();
      }
   }


   @Override
   public void onCreate(SQLiteDatabase sqLiteDatabase) {
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
   }
}

