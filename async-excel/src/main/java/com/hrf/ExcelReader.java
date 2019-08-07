package com.hrf;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ExcelReader<S,T> {


    protected int windowSize;

    protected List<T> list;


    protected Consumer<List<T>> windowListener;


    public List<T> getList() {
        return list;
    }



    public abstract ExcelReader<S,T> process(Function<S, T> readFunction, int startNum) throws Exception;



    protected void invokeListener() {
        //如果相等，windowSize一定不为0，所以windowSize=0，可以表示不启用监听函数。
        if(list.size() == windowSize){
            //监听器执行自定义逻辑
            windowListener.accept(list);
            //重新初始化list，保证之前的数据被GC
            list = new ArrayList<T>(windowSize);
        }
    }
}
