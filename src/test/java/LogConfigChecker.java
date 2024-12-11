import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * This class checks the Log4j2 configuration and prints the loaded configuration URI.
 * It handles potential ClassCastException to detect logging conflicts.
 */
public class LogConfigChecker {

    /**
     * Main method to check the Log4j2 configuration.
     * It prints the loaded configuration URI and handles potential ClassCastException
     * to detect logging conflicts.
     *
     * @param args command line arguments
     */
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
