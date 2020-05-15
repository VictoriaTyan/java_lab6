package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RingBufferImpl<E> implements RingBuffer<E> {

    public class Node {

        private E el;
        private Node next;

        public Node(E elem, Node n){
            el = elem;
            next = n;
        }

        public E getElement() {
            return el;
        }

        public void setElement(E e) {
            el = e;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node n) {
            next = n;
        }
    }

    private Node tail;
    private int size;
    private int MaxSize;

    public RingBufferImpl(int MaxS){
        tail = null;
        size = 0;
        MaxSize = MaxS;
    }

    @Override
    public E poll() {
        if (tail == null)
            return null;
        else {
            E res = tail.getNext().getElement();
            if (tail.getNext() == tail)
                tail = null;
            else
                tail.setNext(tail.getNext().getNext());
            size--;
            return res;
        }
    }

    @Override
    public E peek() {
        if (tail == null)
            return null;
        else
            return tail.getNext().getElement();
    }

    @Override
    public void add(E item) {
        if (tail == null) {
            tail = new Node(item,null);
            tail.setNext(tail);
            size++;
        }
        else if (size == MaxSize) {
                tail.getNext().setElement(item);
                tail = tail.getNext();
            }
            else {
                Node n = new Node(item, tail.getNext());
                tail.setNext(n);
                tail = n;
                size++;
            }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getLimitSize() { return MaxSize; }

    @Override
    public Iterator<E> iterator() {
        return new RingBufferIterator();
    }

    private class RingBufferIterator implements Iterator<E>{
        private Node current;
        private int passes;

        public RingBufferIterator() {
            current = RingBufferImpl.this.tail;
            if (current != null)
                current = current.getNext(); //голова
            passes = 0;
        }

        @Override
        public boolean hasNext() {
            return (current != null) && (passes != RingBufferImpl.this.getSize());
        }

        @Override
        public E next() {
            if (hasNext()) {
                E el = current.getElement();
                current = current.getNext();
                passes++;
                return el;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
// тут должна быть реализация
}
