import org.junit.runner.RunWith;

import Grader.Annotation.Project.Grader;

@RunWith(ScreenCapture.class)
public class Program2 {
	
	@Grader(StudentName="Alaa", StudentID="009797814", Class= "CS585")
	public void compltedMethod() {
        System.out.println("This method is complete");
    }   
	
	@Grader
	public void Test() {
        System.out.println("This method is Tested");
	}
	
	
	//@Grader
	public int Add(int b, int c){
	b=6; c=7;
		int a;
		a = b + c;
		
		return a;
		
	}

}
