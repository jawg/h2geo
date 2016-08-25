package io.jawg.h2geo.dto;

public class DescriptionDto {

    private String en;

    private String fr;

    public DescriptionDto(String en, String fr) {
        this.en = en;
        this.fr = fr;
    }

    public String getEn() {
        return en;
    }

    public String getFr() {
        return fr;
    }
}
