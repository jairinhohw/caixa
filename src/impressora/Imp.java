/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package impressora;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;

public class Imp {
	
	private static PrintService impressora;
	
	public static boolean imprime(String texto) {  
		  
        if (impressora == null) {  
            JOptionPane.showMessageDialog(null, "Nennhuma impressora foi encontrada. Instale uma impressora padrão \r\n(Generic Text Only) e reinicie o programa."); 
        } else {  
            try {  
                DocPrintJob dpj = impressora.createPrintJob();  
                InputStream stream = new ByteArrayInputStream((texto + "\n").getBytes());  
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
                Doc doc = new SimpleDoc(stream, flavor, null);  
                dpj.print(doc, null);  
                return true;  
            } catch (PrintException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }


	public static void detectaImpressoras(String impressoraSelecionada) {
	        try {  
	            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;  
	            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);  
	            for (PrintService p : ps) {  
	                if(p.getName()!=null && p.getName().contains(impressoraSelecionada)){  
	                    impressora = p;  
	                }     
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }

}
