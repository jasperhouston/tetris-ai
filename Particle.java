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
                average column height (avgHeight)
                number of holes (numHoles)
                number of tiles (numTiles)
    */
    /****   Initial value ranges: ---------NEED TO DECIDE THESE STARTING RANGES---------
                maxHeight: 1-20 (double?)
                heightDiff: 1-20 (double?)
                avgHeight: 1-20 (double?)
                numHoles: 1-20 (double?)
                numTiles: 1-20 (double?)
    */
    public double[] maxHeightInitList = {1.0, 20.0};
    public double[] heightDiffInitList = {1.0, 20.0};
    public double[] avgHeightInitList = {1.0, 20.0};
    public double[] numHolesInitList = {1.0, 20.0};
    public double[] numTilesInitList = {1.0, 20.0};

    private int[] MAX_VEL_RAND_VALUE = {2, 4, 4};
    private int MIN_VEL_RAND_VALUE = -2;

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
            double velRandom = ThreadLocalRandom.current().nextDouble(MIN_VEL_RAND_VALUE, 0.5);
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
        
        //avgHeightWeight
        total = avgHeightInitList[1] - avgHeightInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + avgHeightInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
        
        //numHolesWeight
        total = numHolesInitList[1] - numHolesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numHolesInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
        
        //numTilesWeight
        total = numTilesInitList[1] - numTilesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numTilesInitList[0];
        pBest.add(posRandom);
        position.add(posRandom);
    }

    public boolean isEqual(Particle p) {
        return p.ID == ID;
    }
}
