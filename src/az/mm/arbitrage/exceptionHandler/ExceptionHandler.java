package az.mm.arbitrage.exceptionHandler;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ExceptionHandler {
    
    public static void catchMessage(Object currenctObj, String methodName, Exception e, String... optionalText) {
        String fullClassName = currenctObj.getClass().getCanonicalName();
        StringBuilder message = new StringBuilder()
                .append(fullClassName).append(" ").append(methodName).append(" method catch --> ").append(e);
        System.err.println(message.toString());
        
        for(String s: optionalText) System.err.println(s);
        
        e.printStackTrace();
        
//        String mName = new Object(){}.getClass().getEnclosingMethod().getName(); //Burada hele ki duzgun ishlemir, gelen metodu yox cari metodu gosterir

    }
    
}
