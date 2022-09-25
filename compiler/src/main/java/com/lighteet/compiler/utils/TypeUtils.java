package com.lighteet.compiler.utils;

import com.squareup.javapoet.ClassName;
import com.sun.tools.javac.code.Type;
//import com.sun.tools.javac.code.Type;

import javax.lang.model.element.TypeElement;

public class TypeUtils {
    /**
     * 获取父类是制定基类的范型
     * @param te
     * @param className
     * @return
     */
    public static String getTopSuperClsType(TypeElement te, ClassName className) {
        Type.ClassType classType = (Type.ClassType) te.getSuperclass();

        while (classType != null) {
            if (classType.toString().contains(className.toString())) {
                return classType.getTypeArguments().get(0).toString();
            }
            classType = (Type.ClassType) classType.supertype_field;
        }
        return null;

    }

    public static boolean isSuperClassContains(TypeElement te, ClassName className) {
        Type.ClassType classType = (Type.ClassType) te.getSuperclass();


        while (classType != null) {
            if (classType.toString().contains(className.toString())) {
                return true;
            }
            classType = (Type.ClassType) classType.supertype_field;

        }
        return false;

    }

    public static ClassName getOverride() {
        return ClassName.get("java.lang", "Override");
    }



}
