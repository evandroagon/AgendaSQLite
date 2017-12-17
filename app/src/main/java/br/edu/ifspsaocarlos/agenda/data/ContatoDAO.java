package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.model.Contato;


public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String ordenar;  //testar

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public  List<Contato> buscaTodosContatos()
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_ANIVER, SQLiteHelper.KEY_ISFAVORITO};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setFone2(cursor.getString(3));
            contato.setEmail(cursor.getString(4));
            contato.setAniver(cursor.getString(5));
            contato.setFavorito(cursor.getInt(6)==1);  //convertendo valores de inteiro para boolean
            contatos.add(contato);


        }
        cursor.close();
       database.close();
        return contatos;
    }

    public  List<Contato> buscaContato(String nome)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        //Eu usei uma string passada pelo nome para identificar a necessidade de filtar os favoritos e então mudo a clausula were
        //eu usei strings bem grandes para que não ocorra coincidencia com algum nome em uma busca o que poderia causar erros
        //para filtar pelo campo favoritos que está armazenado como inteiro zero e um (que depois vira facilmente boolean  objeto contato)

        if (nome == "FILTRO_FAVORITO" || nome == "FILTRA_NAO_FAVORITOS") {
            String where = SQLiteHelper.KEY_ISFAVORITO;
            String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_EMAIL,
                    SQLiteHelper.KEY_ANIVER, SQLiteHelper.KEY_ISFAVORITO};
            if (nome == "FILTRO_FAVORITO") {
                where = where + " = 1";
            }
            else {
                where = where + " = 0";
            }

            //String[] argWhere= new String[]{"1"};
            cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where, null,
                    null, null, SQLiteHelper.KEY_NAME);

        }
        else {

            String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_FONE2, SQLiteHelper.KEY_EMAIL,
                    SQLiteHelper.KEY_ANIVER, SQLiteHelper.KEY_ISFAVORITO};
            String where = SQLiteHelper.KEY_NAME + " like ? or " + SQLiteHelper.KEY_EMAIL + " like ?";
            String[] argWhere = new String[]{"%" + nome + "%", "%" + nome + "%"};

            cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where, argWhere,
                    null, null, SQLiteHelper.KEY_NAME);
        }


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setFone2(cursor.getString(3));
            contato.setEmail(cursor.getString(4));
            contato.setAniver(cursor.getString(5));
            contato.setFavorito(cursor.getInt(6) == 1);
            contatos.add(contato);


        }
        cursor.close();

        database.close();
        return contatos;
    }

    public void salvaContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE2, c.getFone2());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_ANIVER, c.getAniver());
        values.put(SQLiteHelper.KEY_ISFAVORITO, c.getFavorito());

       if (c.getId()>0)
          database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        else
           database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        database.close();
    }



    public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }
}
