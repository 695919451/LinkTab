package Annotation.Constrains;

/**
 * Created by liuguoyun on 2018/7/14.
 */
public @interface OrderBy {
    String mode() default "asc";
}
