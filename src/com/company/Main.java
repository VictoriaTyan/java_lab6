package com.company;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
Реализовать фабрику (или фабричный метод), которая позволяет
получить объекты RingBuffer с указанными типами параметризации.
При этом возращаться должен экземпляр класса-прокси, который
добавляет к обычному классу следующее поведение:
• Каждый раз при заполнении буфер во время вызова метода add
() удаляемый элемент (перезаписываемый поверх) должен
выводиться в консоль с соотвествующим сообщением.
• Каждый раз при вызове метода poll() , когда буфер
оказывается пуст, в консоль должно выводиться сообщение с
содержимым буфера в последний момент, когда он был полон.
Если такого момента еще не было, сообщение должно указывать
на этот факт.
• Кроме того, в сообщениях из предыдущих двух требований
должно содержаться время (в миллисекундах или наносекундах),
которое потребовалось обработку операции.
 */

public class Main {

    public enum ParameterType {Int, Str}

    public static RingBuffer Fabric_RingBuffer(int MaxSize, ParameterType valType){
        RingBufferImpl original;
        switch(valType){
            case Int:
                original= new RingBufferImpl<Integer>(MaxSize);
                break;
            case Str:
                original= new RingBufferImpl<String>(MaxSize);
                break;
            default:
                return null;
        }
        return (RingBuffer)Proxy.newProxyInstance(original.getClass().getClassLoader(),
                original.getClass().getInterfaces(), new Handler(original));
    }

    public static void main(String[] args) {
        RingBuffer rb = Fabric_RingBuffer(5, ParameterType.Int);
        for(int i=0; i<10; i++)
            rb.add(i+1);
        for(int i=0; i<5; i++)
            rb.poll();
    }
}
