package com.example.design_vicent_sprint1.model;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorData {
    private Date fecha;
    private double temperatura;
    private double distancia;
    private String gas;
    private Boolean ruido;
    private String luz;
    private String foto;

    @Override
    public String toString() {
        return "SensorData{" +
                "fecha=" + fecha +
                ", temperatura=" + temperatura +
                ", distancia=" + distancia +
                ", gas=" + gas +
                ", luz=" + luz +
                ", ruido=" + ruido +
                ", foto='" + foto + '\'' +
                '}';
    }

    public static SensorData fromString(String data) {
        String regex = "fecha=(.*?), temperatura=(.*?), distancia=(.*?), gas=(.*?), luz=(.*?), ruido=(.*?), foto='(.*?)'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            try {
                String fechaStr = matcher.group(1).trim();
                double temperatura = Double.parseDouble(matcher.group(2).trim());
                double distancia = Double.parseDouble(matcher.group(3).trim());
                String gas = matcher.group(4).trim();
                String luz = matcher.group(5).trim();
                Boolean ruido = Boolean.valueOf(matcher.group(7).trim());
                String foto = matcher.group(8).trim();

                Date fecha = new Date(fechaStr);

                return new SensorData(fecha, temperatura, distancia, gas, luz, ruido, foto);

            } catch (Exception e) {
                throw new IllegalArgumentException("Formato de datos inválido: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("El String no coincide con el formato esperado.");
        }
    }

    public SensorData(Date fecha, double temperatura, double distancia, String gas, String luz, Boolean ruido) {
        this.fecha = fecha;
        this.temperatura = temperatura;
        this.distancia = distancia;
        this.gas = gas;
        this.luz = luz;
        this.ruido = ruido;
    }

    public SensorData() {}

    public SensorData(Date fecha, double temperatura, double distancia, String gas, String luz, Boolean ruido, String foto) {
        this.fecha = fecha;
        this.temperatura = temperatura;
        this.distancia = distancia;
        this.gas = gas;
        this.luz = luz;
        this.ruido = ruido;
        this.foto = foto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getLuz() {
        return luz;
    }

    public void setLuz(String luz) {
        this.luz = luz;
    }

    public Boolean getRuido() {
        return ruido;
    }

    public void setRuido(Boolean ruido) {
        this.ruido = ruido;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
