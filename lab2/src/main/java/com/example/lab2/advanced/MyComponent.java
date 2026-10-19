package com.example.lab2.advanced;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // doar pe clase
@Retention(RetentionPolicy.RUNTIME)
public @interface MyComponent {
}
