package com.nostra13.universalimageloader.core.assist.deque;

import java.util.Iterator;
import java.util.Queue;

/* loaded from: classes.dex */
public interface Deque<E> extends Queue<E> {
    @Override // java.util.Queue, java.util.Collection, com.nostra13.universalimageloader.core.assist.deque.Deque
    boolean add(E e);

    void addFirst(E e);

    void addLast(E e);

    @Override // java.util.Collection, com.nostra13.universalimageloader.core.assist.deque.Deque
    boolean contains(Object obj);

    Iterator<E> descendingIterator();

    @Override // com.nostra13.universalimageloader.core.assist.deque.Deque
    E element();

    E getFirst();

    E getLast();

    @Override // java.lang.Iterable, com.nostra13.universalimageloader.core.assist.deque.Deque
    Iterator<E> iterator();

    @Override // java.util.Queue, com.nostra13.universalimageloader.core.assist.deque.Deque
    boolean offer(E e);

    boolean offerFirst(E e);

    boolean offerLast(E e);

    @Override // com.nostra13.universalimageloader.core.assist.deque.Deque
    E peek();

    E peekFirst();

    E peekLast();

    @Override // com.nostra13.universalimageloader.core.assist.deque.Deque
    E poll();

    E pollFirst();

    E pollLast();

    E pop();

    void push(E e);

    @Override // com.nostra13.universalimageloader.core.assist.deque.Deque
    E remove();

    @Override // java.util.Collection, com.nostra13.universalimageloader.core.assist.deque.Deque
    boolean remove(Object obj);

    E removeFirst();

    boolean removeFirstOccurrence(Object obj);

    E removeLast();

    boolean removeLastOccurrence(Object obj);

    @Override // com.nostra13.universalimageloader.core.assist.deque.Deque
    int size();
}
