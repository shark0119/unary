/*package com.cn.unary;

import java.util.function.Function;

public class TestFunc {

	private final String name;

    public String getName() {
        return name;
    }

    public TestFunc(String name) {
        this.name = name;
    }

    public String nonStaticFunc1() {
        return "nonStaticAndPara of fun1: " + name;
    }

    public String nonStaticFunc1(TestFunc tf) {
        // tf is received but ignored
        return "nonStaticWithPara of fun1 " + name;
    }

    public String nonStaticFunc2(TestFunc tf) {
        // tf is received but ignored
        return "nonStaticWithPara of fun2 " + name;
    }

    public void testFunc() {
        Function<TestFunc, String> func;
        TestFunc parameter = new TestFunc("hehe");

        func = TestFunc::nonStaticFunc1;
        System.out.println(func.apply(parameter));

        func = testFunc -> testFunc.nonStaticFunc1();
        System.out.println(func.apply(parameter));
        // TestFunc::nonStaticFunc1
        // means
        // calling nonStaticFunc1 from the parameter as in `apply(parameter)`

        func = this::nonStaticFunc1;
        System.out.println(func.apply(parameter));

        func = testFunc -> this.nonStaticFunc1(testFunc);
        System.out.println(func.apply(parameter));
        // this::nonStaticFunc1
        // means
        // this.nonStaticFunc1(testFunc)

        // similarly func2
        func = this::nonStaticFunc2;
        System.out.println(func.apply(parameter));

        func = testFunc -> this.nonStaticFunc2(testFunc);
        System.out.println(func.apply(parameter));

        // func = TestFunc::nonStaticFunc2; // doesn't make sense, there's no such thing as parameter of the parameter
    }

    public static void main(String[] args) {
        new TestFunc("duh").testFunc();
    }
}
*/