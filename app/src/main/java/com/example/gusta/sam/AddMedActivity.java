package com.example.gusta.sam;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMedActivity extends Activity{


    private EditText nomeMedicamento;//NOME DO MEDICAMENTO
    private EditText qtdMed;//QNTD MEDICAMENTO (COMPRIMIDOS)ETC
    private EditText dosagem;//DOSAGEM
    private Spinner spTipo;//TIPO
    private Spinner spUnidade; //UNIDADE
    private Switch tipoIntervalo;//DIARIAMENTE
    private SeekBar intervaloMed;//INTERVALO
    private TextView textoIntervalo;//txt intervalo
    private TextView horario;//HORARIO
    private TextView dataInicio;//DATA INICIO
    private TextView dataFim;//DATA FIM
    private Button botaoAdicionar;
    private ImageView iv;


    private int quantidadeMed = 0;
    private int quantidadeIntervalo = 0;
    private String id;
    private Date data;
    private String tpIntevalo = " hora(s)";

    private String[] tiposNome = new String[]{"Selecione","Cápsula","Comprimido","Gotas", "Sachê"};
    private int[] tiposImgs = {0 ,R.drawable.capsula, R.drawable.comprimido, R.drawable.gotas, R.drawable.sache};
    private String[] unidade = new String[]{"Selecione","mg","g","ml"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adicionar_medicamento);

        //recuperando componentes

        nomeMedicamento = findViewById(R.id.etNomeMedicamento);
        qtdMed = findViewById(R.id.etQtd);
        dosagem = findViewById(R.id.etDosagem);
        tipoIntervalo = findViewById(R.id.stTipoIntervalo);
        intervaloMed = findViewById(R.id.skIntervalo);
        textoIntervalo = findViewById(R.id.tvIntervaloSelecionado);
        horario =findViewById(R.id.tvHorarioDosagemSelecione);
        dataInicio =findViewById(R.id.tvDataInicioSelecione);
        dataFim =findViewById(R.id.tvDataTerminoSelecione);
        spTipo = findViewById(R.id.spinnerTipo);
        spUnidade = findViewById(R.id.spUnidade);
        botaoAdicionar = findViewById(R.id.btAdicionar);
        iv = findViewById(R.id.ivTipo);

        ArrayAdapter <String> adapterTipo = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,tiposNome);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter<String> adapterUnidade = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,unidade);
        adapterUnidade.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spTipo.setAdapter(adapterTipo);
        spUnidade.setAdapter(adapterUnidade);

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iv.setImageResource(tiposImgs[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        tipoIntervalo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!tipoIntervalo.isChecked()){
                    tpIntevalo = " dia(s)";
                }else{
                    tpIntevalo = " hora(s)";
                }
            }
        });

        botaoAdicionar.setEnabled(false);

        //Editar
        Bundle editMed = getIntent().getExtras();
        if(editMed!= null){
            botaoAdicionar.setEnabled(true);
            String nomeMedE = editMed.getString("nomeMed");
            String qtdMedE = editMed.getString("qntdMed");
            String idMed = editMed.getString("idMed");
            String dosagemE = editMed.getString("dosagem");
            String horarioE = editMed.getString("horario");
            // String dataInicioE = editMed.getString("dataInicio");
            //String dataFimE = editMed.getString("dataFim");
            String intervaloE = editMed.getString("intervalo");
            nomeMedicamento.setText(nomeMedE);
            intervaloMed.setProgress(Integer.parseInt(intervaloE));
            qtdMed.setText(qtdMedE);
            botaoAdicionar.setText("EDITAR");
            id = idMed;
            quantidadeIntervalo = intervaloMed.getProgress();
            dosagem.setText(dosagemE);
            horario.setText(horarioE);
            textoIntervalo.setText("Intervalo: "+ quantidadeIntervalo+ "/24");

        }

        intervaloMed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantidadeIntervalo = progress;
                textoIntervalo.setText("Intervalo: "+ progress + " em "+ progress + tpIntevalo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                quantidadeIntervalo = intervaloMed.getProgress();
            }
        });


        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data();
                botaoAdicionar.setEnabled(true);
            }
        });

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedActivity.this, MainMedicamentosActivity.class);
                intent.putExtra("nomeMed", nomeMedicamento.getText().toString());
                intent.putExtra("qntdMed",qtdMed.getText().toString());
                intent.putExtra("dosagem",dosagem.getText().toString());
                intent.putExtra("horario",horario.getText().toString());
                intent.putExtra("dataInicio",dataInicio.getText().toString());
                intent.putExtra("dataFim",dataFim.getText().toString());
                intent.putExtra("intervalo",quantidadeIntervalo+"");
                if(id!=null){
                    intent.putExtra("idMed",id+"");
                }
                startActivity(intent);
            }
        });
    }

    //Data e hora
    private void Data() {

        Calendar c = Calendar.getInstance();
        final int  hora, minutos;
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        //hora
        TimePickerDialog TimePicker;
        TimePicker =new TimePickerDialog(AddMedActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                SimpleDateFormat formatoTime = new SimpleDateFormat("HH:mm");
                Date dataAtual = null;
                try {
                    dataAtual = formatoTime.parse(selectedHour+":"+selectedMinute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                horario.setText(formatoTime.format(dataAtual)+"");
                //data = selectedHour+":"+selectedMinute;
            }
        }, hora, minutos, true);
        TimePicker.show();
    }
}