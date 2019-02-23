/**
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Copyright 2014 Edgar Espina
 */
package io.jooby.json;

import io.jooby.Body;
import io.jooby.Context;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.Parser;
import io.jooby.Renderer;

import javax.annotation.Nonnull;
import javax.json.bind.Jsonb;

import java.io.InputStream;
import java.lang.reflect.Type;

public class Yasson implements Extension, Parser, Renderer {
  private static final byte[] ARRAY_START = {'['};

  private static final byte[] ARRAY_SEPARATOR = {','};

  private static final byte[] ARRAY_END = {']'};

  private static final byte[][] DELIMITERS = {ARRAY_START, ARRAY_SEPARATOR, ARRAY_END};

  private final Jsonb jsonB;

  public Yasson(Jsonb jsonB) {
    this.jsonB = jsonB;
  }

  public Yasson() {
    this(defaultObjectMapper());
  }

  public static final ObjectMapper defaultObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(new ParameterNamesModule());
    objectMapper.registerModule(new AfterburnerModule());

    return objectMapper;
  }

  @Override public void install(Jooby application) {
    application.parser(MediaType.json, this);
    application.renderer(MediaType.json, this);
  }

  @Override public byte[] render(@Nonnull Context ctx, @Nonnull Object value) throws Exception {
    ctx.setDefaultContentType(MediaType.json);
    return mapper.writeValueAsBytes(value);
  }

  @Override public <T> T parse(Context ctx, Type type) throws Exception {
    JavaType javaType = mapper.getTypeFactory().constructType(type);
    Body body = ctx.body();
    if (body.isInMemory()) {
      return mapper.readValue(body.bytes(), javaType);
    } else {
      try (InputStream stream = body.stream()) {
        return mapper.readValue(stream, javaType);
      }
    }
  }
}
