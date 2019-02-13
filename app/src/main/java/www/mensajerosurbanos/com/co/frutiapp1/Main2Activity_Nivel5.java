package www.mensajerosurbanos.com.co.frutiapp1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Nivel5 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView signomenos, imag_uno, imag_dos, imag_tres;
    private EditText et_respuesta;
    private MediaPlayer media, media_great,media_bad;

    int score, numAleatorio_uno, numAleatorio_dos, resultado, vidas =3;
    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__nivel5);



        tv_nombre = findViewById(R.id.text_nombre);
        tv_score = findViewById(R.id.text_score);
        imag_uno = findViewById(R.id.imag_vidas);
        imag_dos = findViewById(R.id.num_1);
        imag_tres = findViewById(R.id.num_2);
        et_respuesta = findViewById(R.id.edit_rest);
        signomenos = findViewById(R.id.signomenos);

        Toast.makeText(this, R.string.nivel5,Toast.LENGTH_SHORT).show();

        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);


        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);

        if(vidas == 3){
            imag_uno.setImageResource(R.drawable.tresvidas);
        } if(vidas == 2){
            imag_uno.setImageResource(R.drawable.dosvidas);
        } if(vidas == 1){
            imag_uno.setImageResource(R.drawable.unavida);
        }


        //agregar icono

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        media = MediaPlayer.create(this, R.raw.goats);
        media.start();
        media.setLooping(true);

        media_great = MediaPlayer.create(this, R.raw.wonderful);
        media_bad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();
    }

    public void Comparar(View view){
        String respuesta = et_respuesta.getText().toString();

        if(!respuesta.equals("")){

            int respuesta_jugador = Integer.parseInt(respuesta);
            if(resultado == respuesta_jugador){
                media_great.start();
                score++;
                tv_score.setText("Score: " + score);

                et_respuesta.setText("");
                BaseDeDatos();

            }else{
                media_bad.start();
                vidas--;
                BaseDeDatos();

                switch (vidas){
                    case 3:
                        imag_uno.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, R.string.dos_manzanas,Toast.LENGTH_SHORT).show();
                        imag_uno.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this,R.string.una_manzana,Toast.LENGTH_SHORT).show();
                        imag_uno.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, R.string.perdio,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        media.stop();
                        media.release();
                        break;
                }

                et_respuesta.setText("");

            }

            NumAleatorio();

        }else{
            Toast.makeText(this, R.string.mensaje,Toast.LENGTH_SHORT).show();
        }
    }

    public void NumAleatorio(){
        if(score <= 49){

            numAleatorio_uno =  (int) (Math.random() * 10);
            numAleatorio_dos =  (int) (Math.random() * 10);

            resultado = numAleatorio_uno * numAleatorio_dos;

                for(int i = 0; i < numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if(numAleatorio_uno == i){
                        imag_dos.setImageResource(id);
                    }if(numAleatorio_dos == i){
                        imag_tres.setImageResource(id);
                    }
                }



        }else{
            Intent intent = new Intent(this, Main2Activity_Nivel6.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            startActivity(intent);
            finish();
            media.stop();
            media.release();
        }
    }

    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin =new AdminSQLiteOpenHelper(this, "BD",null,1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from  puntaje where score = (select max(score) from puntaje)", null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);

            if(score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score= " +bestScore,null);
            }

            BD.close();

        }else{
            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }

    @Override
    public void onBackPressed(){

    }

}



