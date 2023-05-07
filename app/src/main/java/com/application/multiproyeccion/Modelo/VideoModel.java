package com.application.multiproyeccion.Modelo;

public class VideoModel {

    String videoFechaInicial;
    String videoFechaFinal;
    String videoDescripcion;
    String dataVideo;
    String key;

    public VideoModel(String videoFechaInicial, String videoFechaFinal, String videoDescripcion, String dataVideo) {
        this.videoFechaInicial = videoFechaInicial;
        this.videoFechaFinal = videoFechaFinal;
        this.videoDescripcion = videoDescripcion;
        this.dataVideo = dataVideo;
    }
    public VideoModel() {
        // Constructor sin argumentos
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVideoFechaInicial() {
        return videoFechaInicial;
    }

    public void setVideoFechaInicial(String videoFechaInicial) {
        this.videoFechaInicial = videoFechaInicial;
    }

    public String getVideoFechaFinal() {
        return videoFechaFinal;
    }

    public void setVideoFechaFinal(String videoFechaFinal) {
        this.videoFechaFinal = videoFechaFinal;
    }

    public String getVideoDescripcion() {
        return videoDescripcion;
    }

    public void setVideoDescripcion(String videoDescripcion) {
        this.videoDescripcion = videoDescripcion;
    }

    public String getDataVideo() {
        return dataVideo;
    }

    public void setDataVideo(String dataVideo) {
        this.dataVideo = dataVideo;
    }
}
