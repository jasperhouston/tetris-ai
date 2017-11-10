import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Neighborhood {
    //static, thus accessible to all instances of the class
    static int IDCounter;

    Vector<Double> pBest;
    Vector<Particle> neighborhood;

    double bestValue;
    int size;
    int ID;
    int dimension;

    /****   Dimension order:
    maximum column height (maxHeight)
    difference between min and max column height (heightDiff)
    average column height (avgHeight)
    number of holes (numHoles)
    number of tiles (numTiles)
    aggregate height (aggrHeight)
    complete lines (compLines)
    bumpiness measure (bumpiness)
     */
    /****   Initial value ranges:
    maxHeight: -1 to 1 (double)
    heightDiff: -1 to 1 (double)
    avgHeight: -1 to 1 (double)
    numHoles: -1 to 1 (double)
    numTiles: -1 to 1 (double)
    aggrHeight: -1 to 1 (double)
    compLines: -1 to 1 (double)
    bumpiness: -1 to 1 (double)
     */
    public double[] maxHeightInitList = {-1.0, 1.0};
    public double[] heightDiffInitList = {-1.0, 1.0};
    public double[] avgHeightInitList = {-1.0, 1.0};
    public double[] numHolesInitList = {-1.0, 1.0};
    public double[] numTilesInitList = {-1.0, 1.0};
    public double[] aggrHeightInitList = {-1.0, 1.0};
    public double[] compLinesInitList = {-1.0, 1.0};
    public double[] bumpinessInitList = {-1.0, 1.0};   

    public Neighborhood(int dimension) {
        //assign respective class variables
        size = 0;
        bestValue = Integer.MAX_VALUE;
        this.dimension = dimension;
        ID = IDCounter;
        IDCounter++;

        pBest = new Vector<Double>(0);
        neighborhood = new Vector<Particle>(0);

        //maxHeightWeight
        double total = maxHeightInitList[1] - maxHeightInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + maxHeightInitList[0];
        pBest.add(posRandom);

        //heightDiffWeight
        total = heightDiffInitList[1] - heightDiffInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + heightDiffInitList[0];
        pBest.add(posRandom);

        //avgHeightWeight
        total = avgHeightInitList[1] - avgHeightInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + avgHeightInitList[0];
        pBest.add(posRandom);

        //numHolesWeight
        total = numHolesInitList[1] - numHolesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numHolesInitList[0];
        pBest.add(posRandom);

        //numTilesWeight
        total = numTilesInitList[1] - numTilesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numTilesInitList[0];
        pBest.add(posRandom);
        
        //aggrHeightWeight
        total = aggrHeightInitList[1] - aggrHeightInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + aggrHeightInitList[0];
        pBest.add(posRandom);
        
        //compLinesWeight
        total = compLinesInitList[1] - compLinesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + compLinesInitList[0];
        pBest.add(posRandom);
        
        //numTilesWeight
        total = bumpinessInitList[1] - bumpinessInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + bumpinessInitList[0];
        pBest.add(posRandom);
    }

    public void add(Particle x) {
        neighborhood.add(x);
        size++;
    }

    public void updateBest() {
        for (int i = 0; i < size; i++) {
            if (neighborhood.get(i).pBestValue > bestValue) {
                // if particle's value better than neighborhood best, replace
                bestValue = neighborhood.get(i).pBestValue;

                pBest = neighborhood.get(i).pBest;
            }
        }
    }

    public void reset() {
        pBest.clear();
        neighborhood.clear();
        // reset vector values
        for(int i = 0; i < dimension; i++) {
            double total = 2;
            double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
            double posRandom = total * ratio + -1;
            pBest.add(posRandom);
        }

        bestValue = Integer.MAX_VALUE;
        size = 0;
    }
}