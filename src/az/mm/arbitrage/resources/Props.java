package az.mm.arbitrage.resources;

import az.mm.arbitrage.exceptionHandler.ExceptionHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author MM <mushfiqazeri@gmail.com>
 */
public class Props {
    private static Properties props;

    private Props() {}
    
    public static Properties getInstance() {
        if (props == null) 
            try (FileInputStream in = new FileInputStream("src/az/mm/arbitrage/resources/props.properties");) {
                props = new Properties();
                props.load(in);
            } catch (IOException e) {
                ExceptionHandler.catchMessage(Props.class, new Object() {}.getClass().getEnclosingMethod().getName(), e);
            }

        return props;
    }
    
}
