package Grader.Annotation.Project;
import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE)

@Target(ElementType.METHOD)
public @interface Grader {
	
	 static class None extends Throwable {
		
        private static final long serialVersionUID = 1L;

        private None() {
        }
    }
  
	Class<? extends Throwable> expected() default None.class;
	String StudentName() default " Please Specify your Name";	
	String StudentID() default "";
	String Class() default "Please specify class code";	
	String Assignment() default "Please specify the Title";
	long timeout() default 0L;
}
