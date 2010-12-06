package com.mattinsler.cement.guice;

import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.mattinsler.cement.CementRequestParameters;
import com.mattinsler.cement.CementServlet;
import com.mattinsler.cement.contract.mapper.CementErrorMapper;
import com.mattinsler.cement.filter.CementRequestFilter;
import com.mattinsler.cement.filter.CementRequestLogFilter;
import com.mattinsler.cement.routing.CementMethodRouter;
import com.mattinsler.contract.guice.ContractModule;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 16, 2010
 * Time: 3:01:04 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CementServletModule extends ServletModule {
    private static int _counter = 0;

    protected class CementBinder {
        private final String _urlPattern;
        CementBinder(String urlPattern) {
            _urlPattern = urlPattern;
        }
        public void with(Class<?> cementClass) {
            with(Key.get(cementClass));
        }
        public void with(Key<?> cementKey) {
            Key<CementServlet> key = Key.get(CementServlet.class, Names.named(Integer.toString(++_counter)));

            CementMethodRouter router = new CementMethodRouter(cementKey.getTypeLiteral().getRawType(), _urlPattern);
            Multibinder.newSetBinder(binder(), CementMethodRouter.class).addBinding().toInstance(router);

            bind(key).toProvider(new CementServletProvider(cementKey, router)).in(Singleton.class);
            serve(_urlPattern).with(key);
        }
    }

    protected CementBinder serveCement(String urlPattern) {
        return new CementBinder(urlPattern);
    }

    protected void setDefaultResponseFormat(String format) {
        bindConstant().annotatedWith(Names.named("DefaultResponseFormat")).to(format);
    }

    @Override
    protected void configureServlets() {
        bind(CementRequestParameters.class).toProvider(CementRequestParametersProvider.class);

        bind(CementRequestFilter.class).in(Singleton.class);
        bind(CementRequestLogFilter.class).in(Singleton.class);

        filter("/*").through(CementRequestFilter.class);
        filter("/*").through(CementRequestLogFilter.class);

        install(new ContractModule() {
            @Override
            protected void configureContracts() {
                bindMapper(CementErrorMapper.class);
            }
        });

        configureCement();
    }

    protected abstract void configureCement();
}
