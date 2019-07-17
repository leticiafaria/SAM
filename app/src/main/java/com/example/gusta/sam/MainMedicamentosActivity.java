package com.example.gusta.sam;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class MainMedicamentosActivity extends Activity {

    private SQLiteDatabase bancoDados;
    private ArrayAdapter<Medicamento> medAdaptador;
    private ArrayList<Medicamento> arrayListaMed;
    private ListView listaMed;
    private AlertDialog.Builder dialog;
    private Date dataAtual;
    private SimpleDateFormat formatoTime;
    private SimpleDateFormat formatoData;
    private ImageView infoDeletar;
    private ImageView infoAlterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_medicamentos);

        infoAlterar = findViewById(R.id.ivAlterar);
        infoDeletar = findViewById(R.id.ivDeletar);

        infoAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMedicamentosActivity.this, "Clique no medicamento para alterar",Toast.LENGTH_LONG).show();
            }
        });

        infoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMedicamentosActivity.this, "Pressione em cima do medicamento para deletar",Toast.LENGTH_LONG).show();
            }
        });

        //
        //dataAtual = Calendar.getInstance().getTime();
        formatoData = new SimpleDateFormat("dd-mm-yyyy");
        formatoTime = new SimpleDateFormat("HH:mm");
        try{

            //recuperando botao adicionar
            FloatingActionButton botao =  findViewById(R.id.btnAdicionar);

            //banco de dados
            bancoDados = openOrCreateDatabase("SAM" ,MODE_PRIVATE,null);

            //tabela
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS MEDICAMENTO( " +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOME_MEDICAMENTO VARCHAR(100), " +
                    "QUANTIDADE INT," +
                    "DOSAGEM INT," +
                    //  "TIPO_MEDICAMENTO VARCHAR(30)," +
                    "INTERVALO_DOSES INT," +
                    "HORARIO TIME," +
                    "DATAINICIO DATE," +
                    "DATAFIM DATE)"
            );


            visualizarMed();

            //Vai para AddMedActivity
            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainMedicamentosActivity.this,AddMedActivity.class));
                }
            });




            listaMed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editarMed(position);
                }
            });
            listaMed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    removerMed(position);
                    return true;
                }
            });
            
            //recebendo dados da AddMedActivity e EditActivity
            Bundle extra = getIntent().getExtras();
            if(extra!= null){
                String nomeMed = extra.getString("nomeMed");
                String qtdMed = extra.getString("qntdMed");
                String idMed = extra.getString("idMed");
                String dosagem = extra.getString("dosagem");
                String horario = extra.getString("horario");
                String dataInicio = extra.getString("dataInicio");
                String dataFim = extra.getString("dataFim");
                String intervalo = extra.getString("intervalo");
                dataAtual = formatoTime.parse(horario);

                if(idMed==null){
                    inserirMedicamento(nomeMed,qtdMed,dosagem,horario,dataInicio,dataFim,intervalo);//inserindoMed no banco
                }else{
                    bancoDados.execSQL("UPDATE MEDICAMENTO SET NOME_MEDICAMENTO = '"+nomeMed+"' , QUANTIDADE = "+ qtdMed+", HORARIO = '"+formatoTime.format(dataAtual)+"' WHERE ID = "+idMed);
                    Toast.makeText(MainMedicamentosActivity.this, "Medicamento editado com sucesso!", Toast.LENGTH_SHORT).show();
                    visualizarMed();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void inserirMedicamento(String textoNome, String textoQuantidade, String dosage,String horario, String dataInicio, String dataFim,String intervalo){

        try {
            dataAtual = formatoTime.parse(horario);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            if (textoNome.isEmpty()){
                Toast.makeText(MainMedicamentosActivity.this, "Digite um nome", Toast.LENGTH_SHORT).show();
            }else{

                bancoDados.execSQL("INSERT INTO MEDICAMENTO VALUES(NULL,'"+textoNome+"',"+ Integer.parseInt(textoQuantidade)+","+Integer.parseInt(dosage)+","
                        +Integer.parseInt(intervalo)+",'" +formatoTime.format(dataAtual) +"',NULL,NULL)");
                Toast.makeText(MainMedicamentosActivity.this, "Medicamento salvo com sucesso!", Toast.LENGTH_SHORT).show();
                visualizarMed();
            }

        }catch (Exception e){
            Toast.makeText(MainMedicamentosActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void visualizarMed(){
        try{

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM MEDICAMENTO ORDER BY ID DESC",null);

            //indices 1 para cada coluna
            int indiceColunaId = cursor.getColumnIndex("ID");
            int indiceColunaNome = cursor.getColumnIndex("NOME_MEDICAMENTO");
            int indiceColunaQuantidade = cursor.getColumnIndex("QUANTIDADE");
            int indiceColunaDosagem = cursor.getColumnIndex("DOSAGEM");
            int indiceColunaIntervalo = cursor.getColumnIndex("INTERVALO_DOSES");
            int indiceColunaHorario = cursor.getColumnIndex("HORARIO");
            int indiceColunaDataI = cursor.getColumnIndex("DATAINICIO");
            int indiceColunaDataF = cursor.getColumnIndex("DATAFIM");


            //cria adaptador
            listaMed = findViewById(R.id.ListViewId);


            arrayListaMed = new ArrayList<Medicamento>();
            medAdaptador = new ArrayAdapter<Medicamento>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    arrayListaMed
            );
            listaMed.setAdapter(medAdaptador);

            //move o cursor para inicio
            cursor.moveToFirst();
            for(int a=0;a<cursor.getCount();a++){
                arrayListaMed.add(new Medicamento(cursor.getInt(indiceColunaId),cursor.getString(indiceColunaNome),cursor.getInt(indiceColunaQuantidade),
                        cursor.getInt(indiceColunaDosagem),cursor.getInt(indiceColunaIntervalo),cursor.getString(indiceColunaHorario),
                        cursor.getString(indiceColunaDataI),cursor.getString(indiceColunaDataF)));
                cursor.moveToNext();
            }
           // notificar(18,0,1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editarMed(int posicao){
        try{
            Intent intent = new Intent(MainMedicamentosActivity.this, AddMedActivity.class);
            intent.putExtra("idMed",arrayListaMed.get(posicao).getId()+"" );
            intent.putExtra("nomeMed",arrayListaMed.get(posicao).getNome());
            intent.putExtra("qntdMed",arrayListaMed.get(posicao).getQntd()+"" );
            intent.putExtra("dosagem",arrayListaMed.get(posicao).getDosagem()+"" );
            intent.putExtra("horario",arrayListaMed.get(posicao).getHorario()+"" );
            intent.putExtra("dataInicio",arrayListaMed.get(posicao).getDataInicio()+"" );
            intent.putExtra("dataFim",arrayListaMed.get(posicao).getDataFim()+"" );
            intent.putExtra("intervalo",arrayListaMed.get(posicao).getIntervaloDoses()+"" );
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removerMed(final int posicao){
        dialog = new AlertDialog.Builder(MainMedicamentosActivity.this);
        dialog.setTitle("Remover Medicamento");
        dialog.setMessage("Tem certeza que deseja remover o medicamento?");
        dialog.setCancelable(false);

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                visualizarMed();
            }
        });

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    bancoDados.execSQL("DELETE FROM MEDICAMENTO WHERE ID = "+arrayListaMed.get(posicao).getId());
                    Toast.makeText(MainMedicamentosActivity.this, "Medicamento removido com sucesso!", Toast.LENGTH_SHORT).show();
                    visualizarMed();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    public void notificar(int hora, int minuto , int periodo) {
        SimpleDateFormat formatoTime = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        String data = formatoTime.format(calendar.getTime())+"";
        data.substring(0,2);
        long dataInMilis = Integer.parseInt(data.substring(0,2))*60*60*1000;
        dataInMilis+= Integer.parseInt(data.substring(3))*60*1000;
        long intervalo = periodo * 60 * 60 * 1000;

        Intent tarefaIntent = new Intent(getBaseContext(), NotificacaoAvtivity.class);
        PendingIntent tarefaPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1234, tarefaIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(getBaseContext().ALARM_SERVICE);

        Toast.makeText(MainMedicamentosActivity.this, dataInMilis+"", Toast.LENGTH_SHORT).show();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dataInMilis,
                intervalo, tarefaPendingIntent);

    }

}
