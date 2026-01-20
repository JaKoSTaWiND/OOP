package models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.value.Value;

@Target({ElementType.PACKAGE, ElementType.TYPE}) // can apply for package or class
@Retention(RetentionPolicy.CLASS)                // must only at compile time
@Value.Style(
    typeImmutable = "Base*",           // generate Base* instead of Immutable* in class name
    typeAbstract = "*",                // class can named with basic name
    builder = "new",                   // enable using Base*.builder()
    visibility = Value.Style.ImplementationVisibility.PUBLIC, // Base* can be accessed everywhere
    defaults = @Value.Immutable(copy = true) // enable .copyOf() for all models
)
public @interface ImmutableStyle {}