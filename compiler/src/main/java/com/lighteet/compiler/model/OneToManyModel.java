package com.lighteet.compiler.model;

public class OneToManyModel {


    private String oneToManyModelName;
    private String oneToManyModelCreateName;

    public OneToManyModel(String oneToManyModelName, String oneToManyModelCreateName) {
        this.oneToManyModelName = oneToManyModelName;
        this.oneToManyModelCreateName = oneToManyModelCreateName;
    }

    public String getOneToManyModelCreateName() {
        return oneToManyModelCreateName;
    }

    public void setOneToManyModelCreateName(String oneToManyModelCreateName) {
        this.oneToManyModelCreateName = oneToManyModelCreateName;
    }


    public String getOneToManyModelName() {
        return oneToManyModelName;
    }

    public void setOneToManyModelName(String oneToManyModelName) {
        this.oneToManyModelName = oneToManyModelName;
    }

    @Override
    public String toString() {
        return "OneToManyModel{" +
                "oneToManyModelName='" + oneToManyModelName + '\'' +
                ", oneToManyModelCreateName='" + oneToManyModelCreateName + '\'' +
                '}';
    }
}
