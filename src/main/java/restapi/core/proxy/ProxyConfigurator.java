package restapi.core.proxy;

public interface ProxyConfigurator {

  Object wrapInProxyIfNeeded(Object t, Class implClass);

}
