import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Particle {
    //static, thus accessible to all instances of the class
    static int IDCounter;

    Vector<Double> velocity;
    Vector<Double> position;
    // pBest stores the *position* of the best value achieved
    Vector<Double> pBest;
    // stores the actual value
    double pBestValue;

    // unique ID's allow isEqual
    int ID;

    /****   Dimension order:
    maximum column height (maxHeight)
    difference between min and max column height (heightDiff)
    number of holes (numHoles)
    aggregate height (aggrHeight)
    complete lines (compLines)
    bumpiness measure (bumpiness)
     */
    /****   Initial value ranges:
    maxHeight: -1 to 1 (double)
    heightDiff: -1 to 1 (double)
    numHoles: -1 to 1 (double)
    aggrHeight: -1 to 1 (double)
    compLines: -1 to 1 (double)
    bumpiness: -1 to 1 (double)
     */
    public double[] maxHeightInitList = {-1.0, 1.0};
    public double[] heightDiffInitList = {-1.0, 1.0};
    public double[] numHolesInitList = {-1.0, 1.0};
    public double[] aggrHeightInitList = {-1.0, 1.0};
    public double[] compLinesInitList = {-1.0, 1.0};
    public double[] bumpinessInitList = {-1.0, 1.0};    

    public Particle(int dimension) {
        pBestValue = Integer.MAX_VALUE;
        ID = IDCounter;
        IDCounter++;

        //initialize vectors
        velocity = new Vector<Double>(0);
        position = new Vector<Double>(0);
        pBest = new Vector<Double>(0);

        for (int i = 0; i < dimension; i++) {
            // generate random velocity values
            double velRandom = ThreadLocalRandom.current().nextDouble(-.1, .1);
            velocity.add(velRandom);
        }

        //maxHeightWeight
        double total = maxHeightInitList[1] - maxHeightInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + maxHeightInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);

        //heightDiffWeight
        total = heightDiffInitList[1] - heightDiffInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + heightDiffInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);

        //numHolesWeight
        total = numHolesInitList[1] - numHolesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numHolesInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
        
        //aggrHeightWeight
        total = aggrHeightInitList[1] - aggrHeightInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + aggrHeightInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
        
        //compLinesWeight
        total = compLinesInitList[1] - compLinesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + compLinesInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
        
        //numTilesWeight
        total = bumpinessInitList[1] - bumpinessInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + bumpinessInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
    }

    public boolean isEqual(Particle p) {
        return p.ID == ID;
    }
}
