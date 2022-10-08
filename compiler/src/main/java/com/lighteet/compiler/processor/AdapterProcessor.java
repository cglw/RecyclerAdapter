package com.lighteet.compiler.processor;

import static com.lighteet.compiler.utils.Constants.ANNOTATION_TYPE_ADAPTER_CREATE;
import static com.lighteet.compiler.utils.Constants.ANNOTATION_TYPE_ONE_TO_MANY_CREATE;
import static com.lighteet.compiler.utils.Constants.ANNOTATION_TYPE_PARAMS_CREATE;

import com.google.auto.service.AutoService;
import com.lighteet.annotations.AdapterCreate;
import com.lighteet.annotations.AnnotationsConstant;
import com.lighteet.annotations.OneToManyFactoryCreate;
import com.lighteet.compiler.model.GroupProcessorModel;
import com.lighteet.compiler.model.OneToManyModel;
import com.lighteet.compiler.model.OneToOneModel;
import com.lighteet.compiler.utils.Constants;
import com.lighteet.compiler.utils.StringUtils;
import com.lighteet.compiler.utils.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


@AutoService(Processor.class)
//支持的解析的注解
@SupportedAnnotationTypes({ANNOTATION_TYPE_ONE_TO_MANY_CREATE, ANNOTATION_TYPE_ADAPTER_CREATE, ANNOTATION_TYPE_PARAMS_CREATE})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AdapterProcessor extends BaseProcessor {
    private static final String RECYCLE_VIEW_PACKAGE = "com.lighteet.adapter";
    private static final String ADAPTER = "Adapter";
    private static final String BASE_ADAPTER_HELPER_NAME = "AdapterHelper";
    private static final String BASE_ADAPTER_OVERRIDE_METHOD = "createStrategy";
    private static final ClassName BASE_ONE_TO_MANY_CREATE = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".simple.OneToManyHolderCreate");
    private static final ClassName BASE_HOLDER = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".BaseViewHolder");
    private static final ClassName BASE_ADAPTER = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".MultiTypeAdapter");
    private static final ClassName BASE_STRATEGY = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".simple.SimpleStrategy");
    private static final ClassName BASE_NO_INFLECT_STRATEGY = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".simple.NoReflectStrategy");
    private static final ClassName STRATEGY = ClassName.bestGuess(RECYCLE_VIEW_PACKAGE + ".Strategy");
    private static final String PACKAGE_NAME = Constants.PACKAGE;
    private final Map<String, GroupProcessorModel> groupMap = new HashMap<>();
    //默认开启非反射
    private static final boolean USE_NO_INFLECT=true;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        logger.info(">>> process, start... <<<");

        if (CollectionUtils.isNotEmpty(annotations)) {
            logger.info(">>> CollectionUtils.isNotEmpty, start... <<<");
            Set<? extends Element> oneToManyElements = roundEnvironment.getElementsAnnotatedWith(OneToManyFactoryCreate.class);
            Set<? extends Element> holderElements = roundEnvironment.getElementsAnnotatedWith(AdapterCreate.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                this.initProcess();
                this.parseAdapterCreate(holderElements);
                this.parseOnToManyCreate(oneToManyElements);
                this.createClass();
            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }
        return false;
    }

    private void initProcess() {
        groupMap.clear();
    }

    private void parseAdapterCreate(Set<? extends Element> holderElements) {
        logger.info("parseAdapterCreate");
        Map<String, GroupProcessorModel> groupMap = this.groupMap;
        for (Element e : holderElements) {
            //获取了当前的类名
            TypeElement te = (TypeElement) e;
            //获取了当前的类名
            if (!TypeUtils.isSuperClassContains(te, BASE_HOLDER)) {
                logger.info("super class is not right:" + te + ",should be " + BASE_HOLDER);
                continue;
            }
            AdapterCreate annotation = e.getAnnotation(AdapterCreate.class);
            String[] groups = annotation.group();
            groups=handleGroup(groups,annotation.excludeDefault());
            for (String group : groups) {
                GroupProcessorModel groupModel = groupMap.get(group);
                if (groupModel == null) {
                    groupModel = new GroupProcessorModel(group);
                }
                groupMap.put(group, groupModel);
                OneToOneModel oneToOneModel = new OneToOneModel(((TypeElement) e).getQualifiedName().toString(), TypeUtils.getTopSuperClsType(te, BASE_HOLDER));
                groupModel.getOneToOneModels().add(oneToOneModel);
                logger.info("group is:" + group);
                logger.info(oneToOneModel.toString());
            }
        }
    }

    /**
     * 这里主要为了存储了所有的多类型的单model
     */
    private void parseOnToManyCreate(Set<? extends Element> modelMultiTypeElements) {
        logger.info("parseOnToManyCreate");

        Map<String, GroupProcessorModel> groupMap = this.groupMap;
        for (Element e : modelMultiTypeElements) {
            TypeElement te = (TypeElement) e;
            //获取了当前的类名
            if (!TypeUtils.isSuperClassContains(te, BASE_ONE_TO_MANY_CREATE)) {
                logger.info("super class is not right:" + te + ",should be " + BASE_ONE_TO_MANY_CREATE);
                continue;
            }
            String realClassType = TypeUtils.getTopSuperClsType(te, BASE_ONE_TO_MANY_CREATE);
            OneToManyFactoryCreate annotation = e.getAnnotation(OneToManyFactoryCreate.class);
            String[] groups = annotation.group();
            groups=handleGroup(groups,annotation.excludeDefault());
            for (String group : groups) {
                OneToManyModel oneToManyModel = new OneToManyModel(realClassType, te.getQualifiedName().toString());
                GroupProcessorModel groupModel = groupMap.get(group);
                if (groupModel == null) {
                    groupModel = new GroupProcessorModel(group);
                }
                groupMap.put(group, groupModel);
                groupModel.getOneToManyModels().add(oneToManyModel);
                logger.info("group is:" + group);
                logger.info(oneToManyModel.toString());

            }

        }

    }
    private String[]handleGroup(String[] groups,boolean excludeDefault){
        Set<String>set=new HashSet<>();
        set.add(AnnotationsConstant.DEFAULT_GROUP);
        Collections.addAll(set, groups);
        if(excludeDefault){
            set.remove(AnnotationsConstant.DEFAULT_GROUP);
        }
        String[] res=new String[set.size()];
        int i=0;
        for (String group:set) {
            res[i++]=group;
        }
        return res;

    }


    private void createClass() {
        logger.info("createClassGroupMap" + groupMap.size());
        Map<String, String> adapterGroupMap = new HashMap<>();
        for (String key : groupMap.keySet()) {
            GroupProcessorModel groupModel = groupMap.get(key);
            //创建strategy class,返回类名
            String strategyFactoryCls = createStrategyFactoryCls(groupModel);
            //创建adapter Class
            String adapterClsName = createAdapterCls(strategyFactoryCls, groupModel.getGroup());
            adapterGroupMap.put(groupModel.getGroup(), adapterClsName);
        }
        //创建helper 类
        createAdapterHelper(adapterGroupMap);
    }


    private String createAdapterCls(String factoryName, String group) {
        logger.info("createAdapterCls start-->" + group);

        String clsName = getClsNameByGroup(group, ADAPTER);
        //创建构造方法
        MethodSpec constructMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess("android.content.Context"), "context")
                .addStatement("super($L)", "context").build();
        //重写一个方法 BASE_ADAPTER_OVERRIDE_METHOD
        MethodSpec createTypeFactoryMethod = MethodSpec.methodBuilder(BASE_ADAPTER_OVERRIDE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(STRATEGY)
                .addAnnotation(Override.class)
                .addStatement("return new $T()", ClassName.bestGuess(factoryName)).build();
        //构建这个类对象
        TypeSpec adapterClass = TypeSpec.classBuilder(clsName)
                .addModifiers(Modifier.PUBLIC, Modifier.PUBLIC)
                .superclass(BASE_ADAPTER)
                .addMethod(constructMethod)
                .addMethod(createTypeFactoryMethod)
                .build();
        try {
            logger.info("writeTo:"+adapterClass);
            writeToFile(JavaFile.builder(PACKAGE_NAME, adapterClass)
                    .build());
        } catch (Exception ee) {
            logger.error(ee);
            logger.info("createAdapterCls error-->"+ee.getMessage());

        }
        logger.info("createAdapterCls end-->"+PACKAGE_NAME + "." + clsName);
        return PACKAGE_NAME + "." + clsName;


    }

    private String createStrategyFactoryCls(GroupProcessorModel groupProcessorModel) {
        String createFactoryCls = getClsNameByGroup(groupProcessorModel.getGroup(), "Strategy");
        //创建类
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(createFactoryCls)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL).superclass(USE_NO_INFLECT?BASE_NO_INFLECT_STRATEGY:BASE_STRATEGY);
        //构造申明
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        //拼接一对一注册部分
        List<OneToOneModel> oneToOneModels = groupProcessorModel.getOneToOneModels();
        for (int i = 0; i < oneToOneModels.size(); i++) {
            constructorBuilder.addStatement("registerOneToOne($T.class,$T.class)"
                    , ClassName.bestGuess(oneToOneModels.get(i).getModelClsName())
                    , ClassName.bestGuess(oneToOneModels.get(i).getHolderClsName()));
        }
        //拼接一对多部分
        List<OneToManyModel> oneToManyModels = groupProcessorModel.getOneToManyModels();
        for (int i = 0; i < oneToManyModels.size(); i++) {
            constructorBuilder.addStatement("registerOneToMany($T.class,new $T())"
                    , ClassName.bestGuess(oneToManyModels.get(i).getOneToManyModelName())
                    , ClassName.bestGuess(oneToManyModels.get(i).getOneToManyModelCreateName()));
        }

        if(USE_NO_INFLECT){
            //拼接注册创建holder的
            List<OneToOneModel> oneToOneHolderRegister = groupProcessorModel.getOneToOneModels();
            for (int i = 0; i < oneToOneHolderRegister.size(); i++) {
                constructorBuilder.addStatement("registerHolderCreate($T.class,parent -> new $T(parent))"
                        , ClassName.bestGuess(oneToOneHolderRegister.get(i).getHolderClsName())
                        , ClassName.bestGuess(oneToOneHolderRegister.get(i).getHolderClsName()));
            }
        }

        MethodSpec constructMethod = constructorBuilder.build();
        classBuilder.addMethod(constructMethod);
        writeToFile(JavaFile.builder(PACKAGE_NAME, classBuilder.build()).build());
        return PACKAGE_NAME + "." + createFactoryCls;

    }

    /**
     * 生成一个静态工厂类 返回adapter
     *
     * @param adapterGroupMap
     */
    private void createAdapterHelper(Map<String, String> adapterGroupMap) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getClsName(BASE_ADAPTER_HELPER_NAME));
        for (String key : adapterGroupMap.keySet()) {
            MethodSpec getAdapterNoGroup = MethodSpec.methodBuilder(MessageFormat.format("get{0}Adapter", StringUtils.getStrUpperFirst(key)))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(BASE_ADAPTER)
                    .addParameter(ClassName.bestGuess("android.content.Context"), "context")
                    .addStatement("return new $T(context)", ClassName.bestGuess(adapterGroupMap.get(key)))
                    .build();
            classBuilder.addMethod(getAdapterNoGroup);

        }
        TypeSpec adapterHelper = classBuilder
                .addModifiers(Modifier.PUBLIC)
                .build();
        try {
            writeToFile(JavaFile.builder(PACKAGE_NAME, adapterHelper)
                    .build());
        } catch (Exception ee) {
            logger.error(ee);
        }

    }


    private String getClsNameByGroup(String group, String name) {
        return getModuleName() + StringUtils.getStrUpperFirst(group) + name;
    }

    private String getClsName(String name) {
        return getModuleName() + name;
    }


}
