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
package io.jooby;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public interface Parser {

  Parser UNSUPPORTED_MEDIA_TYPE = new Parser() {
    @Override public <T> T parse(Context ctx, Type type) {
      throw new Err(StatusCode.UNSUPPORTED_MEDIA_TYPE);
    }
  };
  Parser RAW = new Parser() {
    @Override public <T> T parse(Context ctx, Type type) {
      return ctx.body().to(type);
    }
  };

  @Nonnull <T> T parse(@Nonnull Context ctx, @Nonnull Type type) throws Exception;
}
