package org.gooru.groups.app.jdbi;

/**
 * @author ashish on 6/3/18.
 */

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import org.postgresql.util.PGobject;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@BindingAnnotation(BindJsonb.JsonBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindJsonb {
  String value();

  public static class JsonBinderFactory implements BinderFactory {
    @Override
    public Binder build(Annotation annotation) {
      return new Binder<BindJsonb, String>() {
        @Override
        public void bind(SQLStatement q, BindJsonb bind, String jsonString) {
          try {
            PGobject data = new PGobject();
            data.setType("jsonb");
            data.setValue(jsonString);
            q.bind(bind.value(), data);
          } catch (SQLException ex) {
            throw new IllegalStateException("Error Binding JSON", ex);
          }
        }
      };
    }
  }
}
