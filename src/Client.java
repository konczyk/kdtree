import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;

public class Client {

    private final Random rand = new Random();

    @Parameter(
        names = {"--points-num", "-n"},
        description = "Number of randomly generated points (range 0-1)",
        validateWith = PositiveInteger.class)
    private int num;

    @Parameter(
        names = {"--brute-force", "-b"},
        description = "Use brute-force algorithm instead of the default fast one")
    private boolean brute = false;

    @Parameter(
        names = {"--help", "-h"},
        description = "Usage help",
        help = true)
    private boolean help = false;

    @Parameter(
        names = {"--stdin", "-"},
        description = "Read data from stdin ")
    private boolean stdin = false;

    @Parameter(
        names = {"--vis-type", "-t"},
        description = "Visualisation type\n"
                    + "p: Points\n"
                    + "r: Range search\n"
                    + "n: Nearest neighbor search",
        validateWith = TypeValidator.class)
    private String type = "k";

    public static class TypeValidator implements IParameterValidator {
        private static List<String> types = Arrays.asList("k", "r", "n");
        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!types.contains(value)) {
                throw new ParameterException("Invalid visualisation type: " + value);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        JCommander jc = new JCommander(client);
        jc.setProgramName("KdTree");
        try {
            jc.parse(args);
            client.validate();
            if (client.help || args.length == 0) {
                jc.usage();
                return;
            }
            client.run();
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
        }
    }

    private void validate() throws ParameterException {
        if (stdin && num > 0) {
            throw new ParameterException(
                "Parameters --stdin and --points-num are mutually exclusive");
        }
    }

    private void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Searchable points;
                if (!brute) {
                    points = new KdTree();
                } else {
                    points = new PointSet();
                }
                final Plane plane = new Plane(points, num, stdin);
                new Visualizer(points, plane);
            }
        });
    }

}
