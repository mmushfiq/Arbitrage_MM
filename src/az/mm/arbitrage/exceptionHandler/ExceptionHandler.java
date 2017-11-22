package az.mm.arbitrage.exceptionHandler;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class ExceptionHandler {
    
    public static void catchMessage(Object currenctObj, String methodName, Exception e, String... optionalText) {
        String fullClassName = currenctObj.getClass().getCanonicalName();
        StringBuilder message = new StringBuilder()
                .append(fullClassName)
                .append(" ").append(methodName)
                .append(" method catch --> \n")
                .append(e).append(System.lineSeparator());
        
        for(String s: optionalText) message.append(s).append(System.lineSeparator());
        
        System.err.println(message.toString());
        e.printStackTrace();
        
        // Logger de elave etmek olar
    }
    
}
