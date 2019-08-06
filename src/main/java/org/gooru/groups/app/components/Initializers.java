
package org.gooru.groups.app.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author szgooru Created On 25-Dec-2018
 */
public class Initializers implements Iterable<Initializer> {

  private final Iterator<Initializer> internalIterator;

  public Initializers() {
    List<Initializer> initializers = new ArrayList<>();
    initializers.add(DataSourceRegistry.getInstance());
    initializers.add(AppConfiguration.getInstance());
    this.internalIterator = initializers.iterator();
  }

  @Override
  public Iterator<Initializer> iterator() {
    return new Iterator<Initializer>() {

      @Override
      public boolean hasNext() {
        return Initializers.this.internalIterator.hasNext();
      }

      @Override
      public Initializer next() {
        return Initializers.this.internalIterator.next();
      }

    };
  }

}
