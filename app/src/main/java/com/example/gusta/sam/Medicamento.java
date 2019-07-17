package com.example.gusta.sam;


public class Medicamento {
    private String nome;
    private int qntd,id,dosagem,intervaloDoses;
    private String horario,dataInicio,dataFim;


    public Medicamento( int id,String nome, int qntd, int dosagem, int intervaloDoses, String horario, String dataInicio, String dataFim) {
        this.id = id;
        this.nome = nome;
        this.qntd = qntd;
        this.dosagem = dosagem;
        this.intervaloDoses = intervaloDoses;
        this.horario = horario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQntd() {
        return qntd;
    }

    public void setQntd(int qntd) {
        this.qntd = qntd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDosagem() {
        return dosagem;
    }

    public void setDosagem(int dosagem) {
        this.dosagem = dosagem;
    }

    public int getIntervaloDoses() {
        return intervaloDoses;
    }

    public void setIntervaloDoses(int intervaloDoses) {
        this.intervaloDoses = intervaloDoses;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        return nome+"\nHorario: "+horario +"\nQuantidade: "+qntd+"\nDosagem"+dosagem;
    }
}
