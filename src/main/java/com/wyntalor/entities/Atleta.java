package com.wyntalor.entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Atleta {
    private String continent;
    private String country;
    private double height;
    private double weight;
    private double BMI;
    private double age;
    private String gender;
    private String sport;

    // Getters and setters


    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBMI() {
        return BMI;
    }
    @JsonProperty("BMI")
    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atleta)) return false;
        Atleta atleta = (Atleta) o;
        return Double.compare(atleta.getHeight(), getHeight()) == 0 && Double.compare(atleta.getWeight(), getWeight()) == 0 && Double.compare(atleta.getBMI(), getBMI()) == 0 && Double.compare(atleta.getAge(), getAge()) == 0 && Objects.equals(getContinent(), atleta.getContinent()) && Objects.equals(getCountry(), atleta.getCountry()) && Objects.equals(getGender(), atleta.getGender()) && Objects.equals(getSport(), atleta.getSport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContinent(), getCountry(), getHeight(), getWeight(), getBMI(), getAge(), getGender(), getSport());
    }
}