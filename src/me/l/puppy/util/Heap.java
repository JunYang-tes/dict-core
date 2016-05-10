package me.l.puppy.util;
import java.util.List;
import java.util.ArrayList;
public class Heap<T extends Comparable<T>>{
    public static interface IdxChanged<T extends Comparable<T>>{
        void change(int idx,T item);
    }
    List<T> array;
    int capacity;
    public Heap(int capacity,IdxChanged<T> idxChanged){
        this.array=new ArrayList<T>();
        this.capacity=capacity;
    }
    public int add(T ele){
        if(array.size()<capacity){
            array.add(ele);
            return this.adjust(array.size()-1);
        }
        return -1;
    }
    public T root(){
        int size=array.size();
        if(size>0){
            T ret=array.get(0);
            array.set(0,array.get(size-1));
            array.remove(size-1);
            this.adjustDown(0);
            return ret;
        }
        return null;
    }
    public T get(int idx){
        if(idx>=0 && idx<array.size()){
            return array.get(idx);
        }
        return null;
    }
    public int adjustDown(int idx){
        T tmp=array.get(idx);
        T childEle=null,childRight; 
        int child=idx*2+1;
        int size=array.size();
        while (child<size){
            childEle = array.get(child);
            if(child+1<size)
            {
                childRight=array.get(child+1);
                if(childEle.compareTo(childRight)>0){
                    //the smaller one
                    child+=1;
                    childEle=childRight;
                }
            }
            if(tmp.compareTo(childEle)<=0){
                break;
            } else {
                array.set(idx,childEle);
                idx=child;
            }
            child=child*2+1;
        }
        array.set(idx,tmp);
        return idx;
    }
    public int adjust(int idx){
        T tmp=array.get(idx);
        T parentEle=null;
        int parent=(idx-1)/2;
        while (parent >=0){
            parentEle = array.get(parent);
            //if (parent < array.size()-1 && array.get(parent) .compareTo( array.get(parent+1))<0)
            //parent+=1;
            if (tmp.compareTo(parentEle)>=0){
                break;
            } else {
                array.set(idx,parentEle);
                idx=parent;
                if(parent==0)
                    break;
            }
            parent=(parent-1)/2;
        }
        array.set(idx,tmp);
        return idx;
    }
    public void print(){
        for(T ele : array){
            System.out.print(ele);
            System.out.print(' ');
        }
        System.out.print('\n');
    }
}