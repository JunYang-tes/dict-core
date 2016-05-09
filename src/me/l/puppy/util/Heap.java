package me.l.puppy.util
public class Heap<T implements Comparable<T>>{
    T []array;
    int size;
    public Heap(int capacity){
        this.array=new T[capacity];
        size=0;
    }
    public int add(T ele){
        if(size<array.length){
            array[size++]=ele;
            return this.adjust(size-1);
        }
        return -1;
    }
    public int adjust(int idx){

    }
}