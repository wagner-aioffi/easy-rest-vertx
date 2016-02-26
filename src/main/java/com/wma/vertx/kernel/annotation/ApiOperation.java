package com.wma.vertx.kernel.annotation;

import javax.activation.MimeType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface ApiOperation {

    enum Method { GET, PUT, POST }

    enum Type { JSON, TEXT}

    String path();

    Method method();

    Type returnType() default Type.JSON;

}
