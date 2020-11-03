package com.example.pokedex.config;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.MapSchema;
//import io.swagger.v3.oas.models.media.NumberSchema;
//import io.swagger.v3.oas.models.media.StringSchema;
//import jdk.internal.org.jline.reader.ConfigurationPath;
//import org.springframework.core.MethodParameter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyParameterCustomizer implements org.springdoc.core.customizers.ParameterCustomizer {
//
//    static {
//        SpringDocUtils.getConfig().removeRequestWrapperToIgnore(java.util.Map.class);
//    }
//
//    @Override
//    public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
//        if ("params".equals(parameterModel.getName())) {
//            Schema customSchema = new MapSchema()
//                    .addProperties("propA", new NumberSchema())
//                    .addProperties("propB", new NumberSchema())
//                    .addProperties("propC", new StringSchema());
//            parameterModel.setSchema(customSchema);
//        }
//        return parameterModel;
//    }
//}
