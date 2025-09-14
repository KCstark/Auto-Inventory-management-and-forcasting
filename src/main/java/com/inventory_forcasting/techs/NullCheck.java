package com.inventory_forcasting.techs;

import java.util.function.Function;

public class NullCheck {

    public static <T,R> R getSafe(T value, Function<T,R> function){
        return value==null?null: function.apply(value);
    }

    public static <T,R> R getSafeDefault(T value, Function<T,R> function, R defaultValue){
        return value==null?defaultValue: function.apply(value);
    }

    public static  <T,R> R getSafeReference(T value, Function<T,R> repoReference){
        return value==null?null: repoReference.apply(value);
    }
}
