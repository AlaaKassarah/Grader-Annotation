package Grader.Annotation.Project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Ignore;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import com.google.api.services.drive.Drive;
import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;





public class Grader_Runner extends ParentRunner<FrameworkMethod> {
	
        public GoogleDriveFileSyncManager drive;
        public Drive service;
   
        
	public Grader_Runner(java.lang.Class<?> Class) throws InitializationError, FileNotFoundException {
		super(Class);
		
		service =GoogleDriveServiceProvider.get().getGoogleDriveClient() ; 
	    drive = new GoogleDriveFileSyncManager(service);
	}
	
	
	// To Store the description of each Method associated with it's Method
	 private final ConcurrentHashMap<FrameworkMethod, Description> methodDescriptions = new ConcurrentHashMap<FrameworkMethod, Description>();
  
	 
	 // To store All the Method that is annotated with Grader Annotation
	private List<FrameworkMethod> listofMethods = new ArrayList<FrameworkMethod>();
			
		
  //.................. Build Parent Runner Methods.................................
		 
	
	
	/******************************************1*******************************************************************************
	 *  describeChild Method is to Read the description of each method, where description is (class name and method name),
	* or create one if it does not exit.
	*/
	
	 @Override
		protected Description describeChild(FrameworkMethod method) {
		 
			 Description description = methodDescriptions.get(method);
			
		        if (description == null) {
		        	
		            description = Description.createTestDescription(getTestClass().getJavaClass(),
		            		method.getName(), method.getAnnotations());
		            methodDescriptions.putIfAbsent(method, description);
		        }
			return description;
		}
	
	 /************************************1**************************************************************************
	  * This Method is to store a list of all annotated methods in specific class
	  */

		@Override
		protected List<FrameworkMethod> getChildren() {
		
			listofMethods =getTestClass().getAnnotatedMethods(Grader.class);
		
			return listofMethods;
		}

		
		/*************************************1*************************************************************************
		  * 
		  */
		@Override
		protected void runChild(FrameworkMethod method, RunNotifier notifier) {
			Description description = describeChild(method);
			
			if (method.getAnnotation(Ignore.class) != null) {
	            notifier.fireTestIgnored(description);
	            
	        } else {
	        	
	            try {
					runLeaf(processAnnotation(method), description, notifier);
				    
	            } catch (Throwable e) {
					
					e.printStackTrace();
				}
	            
	        }
			
			
		}
		
		/**************************************************************************************************************
		  * 
		  */

		protected Object createTest(FrameworkMethod method) throws Exception {
			
	        return getTestClass().getOnlyConstructor().newInstance();
	    }
		
		
		/**
		 * @throws Throwable ************************************************************************************************************
		  * 
		  */
    

	
private Statement processAnnotation(FrameworkMethod method) throws Throwable{

	
//................. to redirect result............
		
   File  localFile = new File("/Users/alaakassarah/Result.txt");
   System.setOut(new PrintStream(new FileOutputStream(localFile,true)));

	
//........................ Reflect Method Results.....................................
	   
	   
		 Object test;
		 
         try {
        	 
             test = new ReflectiveCallable() {
            	 
                 @Override
                 protected Object runReflectiveCall() throws Throwable {
                     return createTest(method);
                     
                 }
                
		      }.run();
		      
         } catch (Throwable e) {
             return new Fail(e);
       }
         
        
         
        Statement statement =new InvokeMethod(method, test);
        	 
         
             Grader graderAnnotation = method.getAnnotation(Grader.class);
	            
	            if(graderAnnotation != null) {
	            	
	            	System.out.println(" \nMethod Name : " + method.getName());
	            	System.out.println(" Student Name: " + graderAnnotation.StudentName());
	            	System.out.println(" Student ID : " + graderAnnotation.StudentID());
	            	System.out.println(" Class Number: " + graderAnnotation.Class());
	            	System.out.println(" Title: " + graderAnnotation.Assignment());
	            	System.out.println(" Class: " + graderAnnotation.expected());
	            	System.out.println(" ---------------------------"+ "\n");
	         
	            }
	           
	        
	         drive.addFile(localFile);
	           
				return statement;
	     }
	
	

}


