
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.junit.Ignore;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import Grader.Annotation.Project.Grader;


public class ScreenCapture extends ParentRunner<FrameworkMethod> {
	
 /*make change */  
	
	private final ConcurrentHashMap<FrameworkMethod, Description> methodDescriptions = new ConcurrentHashMap<FrameworkMethod, Description>();

	private List<FrameworkMethod> listofMethods = new ArrayList<FrameworkMethod>();


	JFrame Gframe = new JFrame();
	 String name;
	 
	 
	 
	public ScreenCapture(java.lang.Class<?> Class) throws InitializationError  {
		super(Class);
			
		 
	} 
	
	

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
	
	

	@Override
	protected List<FrameworkMethod> getChildren() {
	
		listofMethods =getTestClass().getAnnotatedMethods(Grader.class);
	
		return listofMethods;
	}

	
	
	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		
		if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            try {
            	
				runLeaf(Monitor(method,Gframe,name), description, notifier);
            	
			} catch (Exception e) {
				
				e.printStackTrace();
			}
        }
		
	}
	
	
	

	protected Object createTest(FrameworkMethod method) throws Exception {
        return getTestClass().getOnlyConstructor().newInstance();
    }
	
	
	
	 private Class<? extends Throwable> getExpectedException(Grader annotation) {
	        if (annotation == null ){
	            return null;
	        } else {
	            return annotation.expected();
	        }
	    }

	    private boolean expectsException(Grader annotation) {
	        return getExpectedException(annotation) != null;
	    }
     
	protected Statement possiblyExpectingExceptions(FrameworkMethod method,
            Object test, Statement next) {
		
        Grader annotation = method.getAnnotation(Grader.class);
        return expectsException(annotation) ? new ExpectException(next,
                getExpectedException(annotation)) : next;
    }
	
	
	
	
	
	protected Statement Monitor(final FrameworkMethod method,JFrame frame, String fileName) throws Exception{
		
		   
		
		this.Gframe = frame;
		this.name = fileName;
		
		frame = new JFrame( name);
		frame.setSize(600, 400);
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		
		JPanel panel1 = new JPanel();
		panel.setOpaque(true);
		
		JTextArea result = new JTextArea();
		result.setEnabled(false);
		
	    JTextArea textArea = new JTextArea(30,30);
		textArea.setEnabled(false);
		
		
          
       
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
         statement = possiblyExpectingExceptions(method, test, statement);
         
       
     


		Grader graderAnnotation = (Grader)method.getAnnotation(Grader.class);
		
        if(graderAnnotation != null) {
        	
      	result.setText(" Method Name : " + method.getName() + "\n"+ " Student Name: " + graderAnnotation.StudentName()
        		+"\n"+" Student ID : " + graderAnnotation.StudentID()+"\n"+" Class Number: " + graderAnnotation.Class()
        		+"\n"+" Title: " + graderAnnotation.Assignment()+"\n"+" --------------------------- "+"\n");
             	
      	 PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
      	//textArea.add(Syste m.setOut(printStream));
	  		System.setOut(printStream);
	  		//textArea.setText(System.setOut(printStream));
	  		 // re-assigns standard output stream and error output stream
	        
	        System.setErr(printStream);
	          

	  		
  		
        }
        

	  	
        panel.add(result);
        frame.add(panel);
        frame.setVisible(true);
        
        panel1.add(textArea);
        frame.add(panel1);
        frame.setVisible(true);
         
           System.out.println("Name the file");
    	    Scanner i = new Scanner(System.in);
    	  	name = i.nextLine();
    	  	
         Rectangle rec = frame.getBounds(); // Getting the size of JFrame
  		 
  		 BufferedImage capture = new BufferedImage(rec.width, rec.height,  BufferedImage.TYPE_INT_ARGB);
  		 
  		 frame.paint(capture.getGraphics());
  		  try {
		    	
  			
        
		        ImageIO.write(capture, "png", new File("/Users/alaakassarah/"+name));
		        
		       
		         		    } catch (IOException ioe) {
		 
		        System.out.println(ioe);
		 
		    }
  		
  		
         
		 return statement;
	}
		

	}
