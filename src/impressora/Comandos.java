package impressora;

public class Comandos {
	
	public static char novaLinha = 10;
	public static String cortar = ""+(char)27 + (char)105;
	public static String avanca = ""+(char)27 + (char)74;
	
	public static String abreGaveta = ""+(char)27 + (char)112 + (char)0 + (char)10 + (char)100;
	
	public static String italicoOn = ""+(char)27 + (char)52;
	public static String italicoOff = ""+(char)27 +(char)53;
	public static String sublinhadoOn = ""+(char)27 + (char)45 + "1";
	public static String sublinhadoOff = ""+(char)27 +(char)45 + "0";
        public static String negritoOn = ""+(char)27 +(char)69;
        public static String negritoOff = ""+(char)27 +(char)70;
        
	
}
