package controladores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import modelos.Conciertos;

public class ClaseParaBBDD extends SQLiteOpenHelper implements IConciertosBD{

    private static final String DATABASE_NAME = "bbdd_aplicacion.db";
    private ArrayList<Conciertos> listaConciertos;
    private static final int DATABASE_VERSION = 1;
    private Context contextoBBDD;
    public ClaseParaBBDD(@Nullable Context context,
                         @Nullable String name,
                         @Nullable SQLiteDatabase.CursorFactory factory,
                         int version) {
        super(context, name, factory, version);
        this.contextoBBDD = context;
        copiarBaseDeDatos();
    }


    private void copiarBaseDeDatos() {
        AssetManager assetManager = contextoBBDD.getAssets();

        // Ruta de destino de la base de datos en el sistema de archivos de la aplicación
        String outFileName = contextoBBDD.getDatabasePath(DATABASE_NAME).getPath();

        try {
            InputStream inputStream = assetManager.open(DATABASE_NAME);

            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    @Override
    public Conciertos elemento(String nombre) {
        return null;
    }

    @SuppressLint("Range")
    @Override
    public List<Conciertos> lista() {
        listaConciertos = new ArrayList<>();

        String query = "SELECT * FROM conciertos";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Conciertos conciertos = new Conciertos();
                conciertos.setNombre(cursor.getString(cursor.getColumnIndex("nombreConcierto")));
                conciertos.setFecha(cursor.getString(cursor.getColumnIndex("fecha")));
                conciertos.setLugar(cursor.getString(cursor.getColumnIndex("lugar")));
                conciertos.setCiudad(cursor.getString(cursor.getColumnIndex("ciudad")));
                conciertos.setGenero(cursor.getString(cursor.getColumnIndex("genero")));
                conciertos.setPrecio(cursor.getString(cursor.getColumnIndex("precio")));

                //int idImagen = cursor.getInt(cursor.getColumnIndex("imagenId"));
                //conciertos.setImagen(idImagen);
                conciertos.setCompraEntrada(cursor.getString(cursor.getColumnIndex("comprarEntrada")));

                listaConciertos.add(conciertos);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Ordenar la lista manualmente por fecha de conciertos
        Collections.sort(listaConciertos, new Comparator<Conciertos>() {
            @Override
            public int compare(Conciertos c1, Conciertos c2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date fecha1 = dateFormat.parse(c1.getFecha());
                    Date fecha2 = dateFormat.parse(c2.getFecha());
                    return fecha1.compareTo(fecha2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Si hay un error al analizar la fecha, dejar los elementos en su posición original
                    return 0;
                }
            }
        });

        return listaConciertos;
    }


}
