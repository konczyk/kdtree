import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;
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
        names = {"--nearest-neighbor-search", "-ns"},
        description = "Use nearest neighbor search visualization")
    private boolean ns = false;

    @Parameter(
        names = {"--range-search", "-rs"},
        description = "Use range search visualization")
    private boolean rs = false;

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

        if (!help && !ns && !rs) {
            throw new ParameterException(
                "You must choose visualization type");
        }

        if (ns && rs) {
            throw new ParameterException(
                "Parameters --nearest-neighbor-search and --range-search"
                + " are mutually exclusive");
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
                if (ns) {
                    plane.setNearestNeighborSearch();
                } else {
                    plane.setRangeSearch();
                }
                new Visualizer(plane);
            }
        });
    }

}
