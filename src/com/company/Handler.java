package com.company;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Handler implements InvocationHandler{
    private RingBuffer buffer;
    private Deque last_full;

    Handler(RingBuffer original)
    {
        this.buffer = original;
        if(buffer.getSize() == buffer.getLimitSize())
            for (Object o: buffer)
                this.last_full.add(o);
        else
            this.last_full=new LinkedList();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Exception
    {
        long start = System.currentTimeMillis(); //засекаем время обработки
        System.out.println("Operation "+ method.getName());
        if (method.getName().equals("add") && buffer.getSize() == buffer.getLimitSize())
        {
            System.out.print("Удаляемый (перезаписываемый поверх) элемент - ");
            System.out.println(buffer.peek());

            this.last_full.clear();  //очищаем содержимое буфера в последний момент, когда он был полон.
        }
        else if(method.getName().equals("poll") && buffer.getSize() == 1)
        {
            if(last_full.isEmpty())
                System.out.println("Буфер еще не был полон");
            else
            {
                System.out.println("Buffer is empty now. Last full buffer:");
                for (Object o : last_full) {
                    System.out.print(o + " ");
                }
                System.out.println();
            }
        }
        Object result=method.invoke(buffer, args);
        if (buffer.getSize() == buffer.getLimitSize())
            for (Object o: buffer)
                this.last_full.add(o); //перезаписываем содержимое буфера в последний момент, когда он был полон.
        System.out.println("Used time: "+(System.currentTimeMillis()-start));
        return result;
    }
}
