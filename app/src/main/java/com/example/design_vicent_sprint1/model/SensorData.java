package com.example.design_vicent_sprint1.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorData {
    private String fecha;
    private double temperatura;
    private Boolean distancia;
    private String gas;
    private String luz;
    private Boolean ruido;
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
    public void fromJson(String jsonString) throws ParseException, JSONException {
        JSONObject json = new JSONObject(jsonString);

        // Parsear la fecha
//        String fechaString = json.getString("fecha");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        this.fecha = json.getString("fecha");

        // Asignar los demás valores
        this.temperatura = json.getDouble("temperatura");
        this.distancia = json.getBoolean("distancia");
        this.gas = json.getString("gas");
        this.luz = json.getString("luz");
        this.ruido = json.getBoolean("ruido");
    }


//    public static SensorData fromString(String data) {
//        String regex = "fecha=(.*?), temperatura=(.*?), distancia=(.*?), gas=(.*?), luz=(.*?), ruido=(.*?)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(data);
//
//        if (matcher.find()) {
//            try {
//                String fechaStr = matcher.group(1).trim();
//                double temperatura = Double.parseDouble(matcher.group(2).trim());
//                Boolean distancia = Boolean.valueOf(matcher.group(3).trim());
//                String gas = matcher.group(4).trim();
//                String luz = matcher.group(5).trim();
//                Boolean ruido = Boolean.valueOf(matcher.group(7).trim());
////                String foto = matcher.group(8).trim();
//
//                Date fecha = new Date(fechaStr);
//
//                return new SensorData(fecha, temperatura, distancia, gas, luz, ruido);
//
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Formato de datos inválido: " + e.getMessage());
//            }
//        } else {
//            throw new IllegalArgumentException("El String no coincide con el formato esperado.");
//        }
//    }

    public SensorData(String fecha, double temperatura, Boolean distancia, String gas, String luz, Boolean ruido) {
        this.fecha = fecha;
        this.temperatura = temperatura;
        this.distancia = distancia;
        this.gas = gas;
        this.luz = luz;
        this.ruido = ruido;
    }

    public SensorData() {}

    public SensorData(String fecha, double temperatura, Boolean distancia, String gas, String luz, Boolean ruido, String foto) {
        this.fecha = fecha;
        this.temperatura = temperatura;
        this.distancia = distancia;
        this.gas = gas;
        this.luz = luz;
        this.ruido = ruido;
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public Boolean getDistancia() {
        return distancia;
    }

    public void setDistancia(Boolean distancia) {
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
