package com.mattinsler.cement.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.mattinsler.cement.CementResponseFormatter;
import com.mattinsler.cement.CementServlet;
import com.mattinsler.cement.logging.CementLogger;
import com.mattinsler.cement.routing.CementMethodRouter;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/16/10
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
class CementServletProvider implements Provider<CementServlet> {
    private final Key<?> _cementKey;
    private final CementMethodRouter _router;
    private Injector _injector;

    public CementServletProvider(Key<?> cementKey, CementMethodRouter router) {
        _cementKey = cementKey;
        _router = router;
    }

    @Inject
    void setInjector(Injector injector) {
        _injector = injector;
    }

    public CementServlet get() {
        Object cementHandler = _injector.getInstance(_cementKey);
        return new CementServlet(
                _injector,
                _router,
                cementHandler,
                _injector.getInstance(CementLogger.class),
                _injector.getInstance(CementResponseFormatter.class),
                _injector.getInstance(Key.get(String.class, Names.named("DefaultResponseFormat")))
        );
    }
}
