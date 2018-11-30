package io.jooby.internal;

import io.jooby.Renderer;
import io.jooby.Route;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class RouteImpl implements Route {

  private final String method;
  private final String pattern;
  private final Handler handler;
  private Route.Handler pipeline;
  private final Renderer renderer;
  private Executor executor;
  private boolean gzip;
  List<String> paramKeys;

  public RouteImpl(String method, String pattern, Handler handler,
      Route.Handler pipeline, Renderer renderer) {
    this.method = method.toUpperCase();
    this.pattern = pattern;
    this.handler = handler;
    this.pipeline = pipeline;
    this.renderer = renderer;
  }

  @Override public String pattern() {
    return pattern;
  }

  public List<String> paramKeys() {
    return paramKeys == null ? Collections.EMPTY_LIST : paramKeys;
  }

  @Override public String method() {
    return method;
  }

  @Override public boolean gzip() {
    return gzip;
  }

  public void gzip(boolean gzip) {
    this.gzip = gzip;
  }

  public Executor executor() {
    return executor;
  }

  public void executor(Executor executor) {
    this.executor = executor;
  }

  @Override public Handler handler() {
    return handler;
  }

  @Override public Route.Handler pipeline() {
    return pipeline;
  }

  @Override public Renderer renderer() {
    return renderer;
  }

  @Override public String toString() {
    return method + " " + pattern;
  }

  public void pipeline(Route.Handler pipeline) {
    this.pipeline = pipeline;
    this.executor = null;
  }
}