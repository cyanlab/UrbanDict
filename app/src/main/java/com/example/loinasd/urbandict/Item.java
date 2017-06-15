package com.example.loinasd.urbandict;


public final class Item {

    private final String ribbon;
    private final String name;
    private final String meaning;
    private final String example;

    public Item(String ribbon, String name, String meaning, String example) {
        this.ribbon = ribbon;
        this.name = name;
        this.meaning = meaning;
        this.example = example;
    }

    public String getRibbon() {
        return ribbon;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getExample() {
        return example;
    }
}
