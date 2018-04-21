package com.zippyttech.navigation_and_fragment;

/**
 * Created by zippyttech on 28/03/18.
 */

public class Noticia {

    private int id;
    private String codigo;
    private String titulo;
    private String contenido;
    private  String fecha;
    private String imagen;

    public Noticia(int i, String codigo, String titulo, String contenido, String fecha, String imagen) {
        this.id = i;
        this.codigo = codigo;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.imagen = imagen;

    }

    public Noticia() {

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public  String getFecha(){return  fecha;}

    public void setFecha(String fecha) { this.fecha = fecha;}
}
