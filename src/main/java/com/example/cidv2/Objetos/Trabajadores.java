package com.example.cidv2.Objetos;

import java.io.Serializable;

public class Trabajadores implements Serializable {

    private int id;
    private String nombre;
    private String area_trabajo;
    private String celular;
    private int disponibilidad;
    private String idMovil;

    public Trabajadores(){
        this.setNombre("");
        this.setArea_trabajo("");
        this.setCelular("");
        this.setDisponibilidad(0);
        this.setIdMovil("");
    }

    public Trabajadores(String nombre, String area_trabajo, String celular, int disponibilidad, String idMovil){
        this.setNombre(nombre);
        this.setArea_trabajo(area_trabajo);
        this.setCelular(celular);
        this.setDisponibilidad(disponibilidad);
        this.setIdMovil(idMovil);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea_trabajo() {
        return area_trabajo;
    }

    public void setArea_trabajo(String area_trabajo) {
        this.area_trabajo = area_trabajo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getIdMovil() {
        return idMovil;
    }

    public void setIdMovil(String idMovil) { this.idMovil = idMovil; }
}