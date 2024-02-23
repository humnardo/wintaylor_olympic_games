package com.wyntalor.entities;

public class AtletaEstatisticas {
    private int quantidadeHomens;
    private int quantidadeMulheres;
    private double mediaIdadeHomens;
    private double mediaIdadeMulheres;

    public AtletaEstatisticas(int quantidadeHomens, int quantidadeMulheres, double mediaIdadeHomens, double mediaIdadeMulheres) {
        this.quantidadeHomens = quantidadeHomens;
        this.quantidadeMulheres = quantidadeMulheres;
        this.mediaIdadeHomens = mediaIdadeHomens;
        this.mediaIdadeMulheres = mediaIdadeMulheres;
    }

    public int getQuantidadeHomens() {
        return quantidadeHomens;
    }

    public void setQuantidadeHomens(int quantidadeHomens) {
        this.quantidadeHomens = quantidadeHomens;
    }

    public int getQuantidadeMulheres() {
        return quantidadeMulheres;
    }

    public void setQuantidadeMulheres(int quantidadeMulheres) {
        this.quantidadeMulheres = quantidadeMulheres;
    }

    public double getMediaIdadeHomens() {
        return mediaIdadeHomens;
    }

    public void setMediaIdadeHomens(double mediaIdadeHomens) {
        this.mediaIdadeHomens = mediaIdadeHomens;
    }

    public double getMediaIdadeMulheres() {
        return mediaIdadeMulheres;
    }

    public void setMediaIdadeMulheres(double mediaIdadeMulheres) {
        this.mediaIdadeMulheres = mediaIdadeMulheres;
    }
}