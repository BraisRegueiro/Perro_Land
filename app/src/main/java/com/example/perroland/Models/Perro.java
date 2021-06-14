package com.example.perroland.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Perro implements Serializable {
    @Expose
    @SerializedName("Perro")
    private String nombrePerro;
    @Expose
    @SerializedName("RazaPerro")
    private String razaPerro;
    @Expose
    @SerializedName("description")
    private String description;

    public String getNombrePerro() {
        return nombrePerro;
    }

    public void setNombrePerro(String nombrePerro) {
        this.nombrePerro = nombrePerro;
    }

    public String getRazaPerro() {
        return razaPerro;
    }

    public void setRazaPerro(String razaPerro) {
        this.razaPerro = razaPerro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map toMap() {

        HashMap map = new HashMap();

        map.put("Perro", this.nombrePerro);
        map.put("Raza", this.razaPerro);
        map.put("description", this.description);

        return map;

    }


}
