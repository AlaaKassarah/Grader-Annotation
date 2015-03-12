package Grader.Annotation.Project;
import org.junit.runner.RunWith;

@RunWith(Grader_Runner.class)

public class Program {
	
	@Grader
	public static  void Method1() {
        System.out.println("This method is Static");
    }  
	
	@Grader(StudentName="Alaa", StudentID="009797814", Class= "CS585")
 
	public void compltedMethod() {
        System.out.println("This method is complete");
    }   
	
	@Grader(Assignment= "final Project")
	public void Test() {
        System.out.println("This method is Tested");
    } 
	
	@Grader
	public int Add(){
		int a;
		a =5+6;
		//System.out.println(a);
	
		return a;	
	}
	
	
	@Grader
	public int Add1(int b, int c){
		int a;
		a = b + c;
		System.out.println(a);
	
		return a;
	}
	
	
	@Grader
	public String message(){
		String a, b;
		b= "world";
		a = "Hello" + b;
		System.out.print(a);
	
		return a;	
	}
	
	
	
	
}
