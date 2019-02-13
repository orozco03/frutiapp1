package www.mensajerosurbanos.com.co.frutiapp1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edit_name;
    private ImageView imag_personaje;
    private TextView text_Score;
    private MediaPlayer media;

    int num_aleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_name = findViewById(R.id.txt_nombre);
        imag_personaje = findViewById(R.id.ImageView_personaje);
        text_Score = findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;

        if(num_aleatorio == 0 || num_aleatorio == 10){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            imag_personaje.setImageResource(id);
        } else if(num_aleatorio == 1 || num_aleatorio == 9) {
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            imag_personaje.setImageResource(id);
        }else if(num_aleatorio == 2 || num_aleatorio == 8) {
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            imag_personaje.setImageResource(id);
        }else if(num_aleatorio == 3 || num_aleatorio == 7) {
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            imag_personaje.setImageResource(id);
        }else if(num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6) {
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            imag_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD", null,1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor contulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null);
        if (contulta.moveToFirst()){
            String temp_nombre = contulta.getString(0);
            String temp_score = contulta.getString(1);
            text_Score.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        }else{
            BD.close();
        }

        media = MediaPlayer.create(this, R.raw.alphabet);
        media.start();
        media.setLooping(true);
    }

    public void Jugar (View view){
        String name = edit_name.getText().toString();

        if(!name.equals("")){
            media.stop();
            media.release();

            Intent intent = new Intent(this,Main2Activity_Nivel1.class);
            intent.putExtra("jugador", name);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.primero_nombre,Toast.LENGTH_SHORT).show();

            edit_name.requestFocus();
            InputMethodManager input = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            input.showSoftInput(edit_name, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){

    }
}

