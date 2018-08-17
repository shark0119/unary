package com.cn.unary.jvm;

import java.lang.invoke.*;

public class Foo {
  public static void bar(Object o) {
    new Exception().printStackTrace();
  }

  public static void main(String[] args) throws Throwable {
    MethodHandles.Lookup l = MethodHandles.lookup();
    MethodType t = MethodType.methodType(void.class, Object.class);
    MethodHandle mh = l.findStatic(Foo.class, "bar", t);
    mh.invokeExact(new Object());
  }
}

