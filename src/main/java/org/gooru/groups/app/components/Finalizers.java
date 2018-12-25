
package org.gooru.groups.app.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author szgooru Created On 25-Dec-2018
 */
public class Finalizers implements Iterable<Finalizer> {

  private final Iterator<Finalizer> internalIterator;

  public Finalizers() {
    List<Finalizer> finalizers = new ArrayList<>();
    finalizers.add(DataSourceRegistry.getInstance());
    this.internalIterator = finalizers.iterator();
  }

  @Override
  public Iterator<Finalizer> iterator() {
    return new Iterator<Finalizer>() {

      @Override
      public boolean hasNext() {
        return Finalizers.this.internalIterator.hasNext();
      }

      @Override
      public Finalizer next() {
        return Finalizers.this.internalIterator.next();
      }
    };
  }
}
