package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_FONE2 = "fone2";
    static final String KEY_EMAIL = "email";
    static final String KEY_ANIVER = "aniver";
    static final String KEY_ISFAVORITO = "isfavorito";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_UPGRADE1 = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_ISFAVORITO + " TEXT";
    private static final String DATABASE_UPGRADE2 = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_FONE2 + " TEXT";
    private static final String DATABASE_UPGRADE3 = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_ANIVER + " TEXT";

    private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, " +
            KEY_FONE2 + " TEXT, " +
            KEY_EMAIL + " TEXT, " +
            KEY_ANIVER + " TEXT, " +
            KEY_ISFAVORITO + " INTEGER);";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int    newVersion) {
        switch (oldVersion) {
            case 1: {
                if (newVersion == 2){
                    database.execSQL(DATABASE_UPGRADE1);
                }
                if (newVersion == 3) {
                    database.execSQL(DATABASE_UPGRADE1);
                    database.execSQL(DATABASE_UPGRADE2);
                }
                if (newVersion == 4){
                    database.execSQL(DATABASE_UPGRADE1); //atualiza da versão 1 para a 3;
                    database.execSQL(DATABASE_UPGRADE2);
                    database.execSQL(DATABASE_UPGRADE3);

                }

                return;
                }

            case 2: {
                if (newVersion == 3) {
                    database.execSQL(DATABASE_UPGRADE2);
                }
                if (newVersion == 4){
                    database.execSQL(DATABASE_UPGRADE2);  //atualiza da versão 1 para a 3;
                    database.execSQL(DATABASE_UPGRADE3);

                }//atualiza da versão 2 para 3;
            }

            case 3:{
                if (newVersion==4) {
                    database.execSQL(DATABASE_UPGRADE3);  //atualiza da versão 3 para 4;
                }


            }
        }

    }
}

