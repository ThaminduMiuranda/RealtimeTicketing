import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class LogConfigChecker {
    public static void main(String[] args) {
        try {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            System.out.println("Loaded Log4j2 configuration: " +
                    context.getConfiguration().getConfigurationSource().getURI());
        } catch (ClassCastException e) {
            System.err.println("ClassCastException detected. Possible logging conflict.");
            e.printStackTrace();
        }
    }
}
