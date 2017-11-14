package az.mm.arbitrage.exceptionHandler;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ExceptionHandler {
    
    public static void catchMessage(Object currenctObj, String methodName, Exception e) {
        String fullClassName = currenctObj.getClass().getCanonicalName();
        String message = fullClassName + " " + methodName + " method catch --> " + e;
        System.out.println(message);
        e.printStackTrace();
        
//        String mName = new Object(){}.getClass().getEnclosingMethod().getName(); //Burada hele ki duzgun ishlemir, gelen metodu yox cari metodu gosterir

    }
    
}
