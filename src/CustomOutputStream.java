 
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
 
/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 */
public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
     
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
     
    @Override
    public void write(int b) throws IOException {
    	
    	 SwingUtilities.invokeLater(new Runnable() {
             public void run() {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
       // textArea.setText(String.valueOf((char)b));

        
             }
    	 });
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
       
    }
}