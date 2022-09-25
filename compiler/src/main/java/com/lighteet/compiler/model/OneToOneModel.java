package com.lighteet.compiler.model;

public class OneToOneModel {

    private String holderClsName;
    private String modelClsName;

    public OneToOneModel(String holderClsName, String modelClsName) {
        this.holderClsName = holderClsName;
        this.modelClsName = modelClsName;
    }

    public String getHolderClsName() {
        return holderClsName;
    }

    public void setHolderClsName(String holderClsName) {
        this.holderClsName = holderClsName;
    }

    public String getModelClsName() {
        return modelClsName;
    }

    public void setModelClsName(String modelClsName) {
        this.modelClsName = modelClsName;
    }

    @Override
    public String toString() {
        return "OneToOneModel{" +
                "holderClsName='" + holderClsName + '\'' +
                ", modelClsName='" + modelClsName + '\'' +
                '}';
    }
}
