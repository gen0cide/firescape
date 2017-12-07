package org.firescape.server.util;

import org.firescape.server.model.Entity;

import java.util.Iterator;
import java.util.Set;

public class EntityListIterator<E extends Entity> implements Iterator<E> {

  private final Integer[] indicies;
  private final Object[] entities;
  private final EntityList<E> entityList;
  private int curIndex;

  public EntityListIterator(Object[] entities, Set<Integer> indicies, EntityList<E> entityList) {
    this.entities = entities;
    this.indicies = indicies.toArray(new Integer[0]);
    this.entityList = entityList;
  }

  public boolean hasNext() {
    return indicies.length != curIndex;
  }

  @SuppressWarnings("unchecked")
  public E next() {
    Object temp = entities[indicies[curIndex]];
    curIndex++;
    return (E) temp;
  }

  public void remove() {
    if (curIndex >= 1) {
      entityList.remove(indicies[curIndex - 1]);
    }
  }

}
