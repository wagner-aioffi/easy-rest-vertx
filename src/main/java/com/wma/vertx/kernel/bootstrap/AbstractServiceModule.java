package com.wma.vertx.kernel.bootstrap;

import com.google.inject.AbstractModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractServiceModule extends AbstractModule {

    private final List<Class<?>> apis = new ArrayList<>();

    public <T> void configureApi(final Class<T> apiImplClazz) {
        bind(apiImplClazz).asEagerSingleton();
        apis.add(apiImplClazz);
    }

    public List<Class<?>> getApis() {
        return Collections.unmodifiableList(apis);
    }
}
