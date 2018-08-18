package com.cn.unary.jvm;

public class Foo {
    static void take() {
        System.out.println("take from Foo");
    }

    public static void main(String[] args) {
        Foo foo = new FooChild();
        foo.take();
    }
}

class FooChild extends Foo {
    static void take() {
        System.out.println("take from FooChild");
    }
}

