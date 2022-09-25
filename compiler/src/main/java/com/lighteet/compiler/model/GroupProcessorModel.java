package com.lighteet.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GroupProcessorModel {


    private String group;
    private List<OneToOneModel> oneToOneModels;
    private List<OneToManyModel> oneToManyModels;

    public GroupProcessorModel(String group) {
        this.group = group;
    }

    public List<OneToManyModel> getOneToManyModels() {
        if (oneToManyModels == null) {
            oneToManyModels = new ArrayList<>();
        }
        return oneToManyModels;
    }

    public void setOneToManyModels(List<OneToManyModel> oneToManyModels) {
        this.oneToManyModels = oneToManyModels;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<OneToOneModel> getOneToOneModels() {
        if (oneToOneModels == null) {
            oneToOneModels = new ArrayList<>();
        }
        return oneToOneModels;
    }

    public void setOneToOneModels(List<OneToOneModel> oneToOneModels) {
        this.oneToOneModels = oneToOneModels;
    }

    @Override
    public String toString() {
        List<OneToOneModel> oneToOneModels = getOneToOneModels();
        StringBuilder oneToOneModelStr = new StringBuilder();
        for (int i = 0; i < oneToOneModels.size(); i++) {
            oneToOneModelStr.append(oneToOneModels);
        }
        List<OneToManyModel> oneToManyModels = getOneToManyModels();

        StringBuilder oneToManyModelStr = new StringBuilder();
        for (int i = 0; i < oneToManyModels.size(); i++) {
            oneToManyModelStr.append(oneToManyModels);
        }
        return "GroupProcessorModel{" +
                "group='" + group + '\'' +
                ", oneToOneModels=" + oneToOneModelStr +
                ", oneToManyModels=" + oneToManyModelStr +
                '}';
    }
}
