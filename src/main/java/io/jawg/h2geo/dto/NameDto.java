package io.jawg.h2geo.dto;


public class NameDto {

    private String en;

    private String fr;

    public NameDto(String en, String fr) {
        this.en = en;
        this.fr = fr;
    }

    public String getEn() {
        return en;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
