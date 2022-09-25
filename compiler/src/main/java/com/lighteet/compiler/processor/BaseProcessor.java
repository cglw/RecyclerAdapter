package com.lighteet.compiler.processor;

import static com.lighteet.compiler.utils.Constants.KEY_MODULE_NAME;
import static com.lighteet.compiler.utils.Constants.NO_MODULE_NAME_TIPS;

import com.lighteet.compiler.utils.Logger;
import com.lighteet.compiler.utils.StringUtils;
import com.squareup.javapoet.JavaFile;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
public class BaseProcessor extends AbstractProcessor {
    public Logger logger;
    public Filer filer;
    String moduleName = null;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        logger = new Logger(processingEnv.getMessager());
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME);
        }
        if (moduleName != null && moduleName.length() > 0) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");

            logger.info("The user has configuration the module name, it was [" + moduleName + "]");
        } else {
            logger.error(NO_MODULE_NAME_TIPS);
            throw new RuntimeException("AdapterCreate::Compiler >>> No module name, for more information, look at gradle log.");
        }

    }


    public void writeToFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(filer);
        } catch (Exception ee) {
            logger.error(ee);
        }

    }

    public String getModuleName() {
        return StringUtils.getStrUpperFirst(moduleName);
    }


}
