import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PSO {
    // size of the board in blocks
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    // Extra blocks at the top for pieces to start.
    // If a piece is sticking up into this area
    // when it has landed -- game over!
    public static final int TOP_SPACE = 4;

    // need to be able to access this from the solver, thus not private
    double gBestValue;

    private int swarmSize;
    private int iterations;
    private int dimension;
    // position of all-time best particle
    private Vector<Double> gBest;
    private Vector<Neighborhood> neighborhoodList;

    private final static double constrict = 0.7298;
    private double nBest;
    private Vector<Particle> swarm;

    /****   Dimension order:
    maximum column height (maxHeight)
    difference between min and max column height (heightDiff)
    average column height (avgHeight)
    number of holes (numHoles)
    number of tiles (numTiles)
     */
    /****   Initial value ranges:
    maxHeight: 1-20 (double?)
    heightDiff: 1-20 (double?)
    avgHeight: 1-20 (double?)
    numHoles: 1-20 (double?)
    numTiles: 1-20 (double?)
     */
    public double[] maxHeightInitList = {0.0, 1.0};
    public double[] heightDiffInitList = {0.0, 1.0};
    public double[] avgHeightInitList = {0.0, 1.0};
    public double[] numHolesInitList = {0.0, 1.0};
    public double[] numTilesInitList = {0.0, 1.0};

    private double PHI_1 = .6;
    private double PHI_2 = .85;

    public PSO(int swarmSize, int iterations) {
        this.swarmSize = swarmSize;
        this.iterations = iterations;
        this.dimension = 5;
        gBestValue = 0.0;

        gBest = new Vector<Double>(0);
        swarm = new Vector<Particle>(0);
        neighborhoodList = new Vector<Neighborhood>(0);

        //maxHeightWeight
        double total = maxHeightInitList[1] - maxHeightInitList[0];
        double ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        double posRandom = total * ratio + maxHeightInitList[0];
        gBest.add(posRandom);

        //heightDiffWeight
        total = heightDiffInitList[1] - heightDiffInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + heightDiffInitList[0];
        gBest.add(posRandom);

        //avgHeightWeight
        total = avgHeightInitList[1] - avgHeightInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + avgHeightInitList[0];
        gBest.add(posRandom);

        //numHolesWeight
        total = numHolesInitList[1] - numHolesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numHolesInitList[0];
        gBest.add(posRandom);

        //numTilesWeight
        total = numTilesInitList[1] - numTilesInitList[0];
        ratio = ThreadLocalRandom.current().nextDouble(0, 1);
        posRandom = total * ratio + numTilesInitList[0];
        gBest.add(posRandom);
    }

    /* veloctiy and position updates */
    public void updateVelocity(int index) {
        Particle p = swarm.get(index);
        // iterate through dimensions, updating respective velocities
        for(int i = 0; i < dimension; i++) {
            // attraction from personal best
            double pAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_1 * (swarm.get(index).pBest.get(i) - swarm.get(index).position.get(i));

            // get this particle's neighborhood best
            Vector<Double> neighbBestPosition = neighborhoodList.get(index).pBest;
            // attraction from neighborhood best
            double nAttract = ThreadLocalRandom.current().nextDouble(0, 1) * PHI_2 * (neighbBestPosition.get(i) - swarm.get(index).position.get(i));
            double velChange = pAttract + nAttract;
            swarm.get(index).velocity.set(i, swarm.get(index).velocity.get(i) + velChange);
            swarm.get(index).velocity.set(i, swarm.get(index).velocity.get(i) * constrict);
        }
    }

    public void updatePosition(int index) {
        // iterate through dimensions, updating respective positions based on velocity
        for(int i = 0; i < dimension; i++) {
            swarm.get(index).position.set(i, swarm.get(index).position.get(i) + swarm.get(index).velocity.get(i));
        }
    }

    /* neighborhoods */
    public void initializeNeighborhoods() {
        initializeRandomNeighborhood();
    }
    // public void global();
    // public void ring();
    // public void vonNeumann();
    public void initializeRandomNeighborhood() {
        int k = 5;

        for (int i = 0; i < swarmSize; i++) {
            Neighborhood temp = new Neighborhood(dimension);
            // particle is in its own neighborhood
            temp.add(swarm.get(i));
            for (int j = 0; j < k - 1; j++) {
                // get non-duplicate index
                int randIndex = getNewRandIndex(temp);
                // add the index that is not a duplicate
                temp.add(swarm.get(randIndex));
            }
            neighborhoodList.add(temp);
        }
    }

    public void updateRandomNeighborhood() {
        int k = 5;
        double minProb = 0.2;

        for (int i = 0; i < swarmSize; i++) {
            double probability = ThreadLocalRandom.current().nextDouble(0, 1);
            if (probability < minProb) {
                // clear neighborhood
                neighborhoodList.get(i).reset();
                neighborhoodList.get(i).add(swarm.get(i));
                for (int j = 0; j < k - 1; j++) {
                    // get non-duplicate index
                    int randIndex = getNewRandIndex(neighborhoodList.get(i));
                    // add the index that isn't a duplicate to the neighborhood
                    neighborhoodList.get(i).add(swarm.get(randIndex));
                }
            }
        }
    }

    public int getNewRandIndex(Neighborhood h) {
        int randIndex = ThreadLocalRandom.current().nextInt(0, swarmSize);

        // check to make sure that this index isn't a duplicate
        boolean indexAlreadySelected = false;
        while (!indexAlreadySelected) {
            indexAlreadySelected = true;
            // iterate through particle swarm[i]'s neighborhood
            for (int n = 0; n < h.neighborhood.size(); n++) {
                Particle randPart = swarm.get(randIndex);
                if (h.neighborhood.get(n).isEqual(randPart)){
                    // if you've added the index already
                    // break, get a new number, and repeat
                    indexAlreadySelected = false;
                    break;
                }
            }
            if (indexAlreadySelected == false) {
                randIndex = ThreadLocalRandom.current().nextInt(0, swarmSize);
            }
        }

        return randIndex;
    }

    /* function evaluation */
    /* this is where the GA gets called */
    public void eval(int index) {
        //save the particle that we're looking at
        Particle p = swarm.get(index);
        double pVal = 0.0;

        /******************************************************/

        double maxHeightWeight = p.position.get(0);
        double heightDiffWeight = p.position.get(1);
        double avgHeightWeight = p.position.get(2);
        double numHolesWeight = p.position.get(3);
        double numTilesWeight = p.position.get(4);
        //System.out.println(maxHeightWeight + " " + heightDiffWeight + " " + avgHeightWeight + " " + numHolesWeight + " " + numTilesWeight);
        // TODO here we need to run an evaluation
        final int pixels = 16;
        final double trials = 50.0;
        DJBrainNoGraphics tetris = new DJBrainNoGraphics(WIDTH*pixels+2, (HEIGHT+TOP_SPACE)*pixels+2, 
            maxHeightWeight, heightDiffWeight, avgHeightWeight, numHolesWeight, numTilesWeight);
            
        // In this we're just running a fixed number of trials and outputting how many pieces we
        // got before losing in each trial
        double totalPieces = 0.0;
        for (int i = 0; i < trials; i++) {
            tetris.startGame();
            totalPieces += (double)tetris.getPieces();
            
        }
        pVal = totalPieces / trials;
        
        /******************************************************/

        //update the personal best value if necessary
        if (pVal > p.pBestValue) {
            swarm.get(index).pBest = p.position;
            swarm.get(index).pBestValue = pVal;
        }
        //update the best overall best if necessary
        if(pVal > gBestValue) {
            gBestValue = pVal;
            gBest = p.pBest;
        }
        //update all neighborhood bests
        updateNeighborhoodBest();
    }

    public void updateNeighborhoodBest(){
        // iterate through neighborhoods, updating bests
        for(int i = 0; i < swarmSize; i++) {
            // update each particle's neighborhood
            neighborhoodList.get(i).updateBest();
        }
    }

    /* initialization */
    public void initializeSwarm() {
        // create swarm of 'swarmSize' particles
        for (int i = 0; i < swarmSize; i++) {
            Particle newParticle = new Particle(dimension);
            swarm.add(newParticle);
        }
    }

    /* general algorithm controller */
    public Vector<Double> solvePSO() {
        System.out.println("Solving PSO...");

        initializeSwarm();
        initializeNeighborhoods();

        int iterRemaining = iterations;

        while(iterRemaining >= 0) {

            // iterate through particles, updating velocity & position
            for(int i = 0; i < swarmSize; i++) {
                updateVelocity(i);
                updatePosition(i);
                // evaluate at position so later particles benefit from moves before
                eval(i);
            }

            updateRandomNeighborhood();
            System.out.println("gBest = " + gBestValue + " iteration " + (iterations - iterRemaining) + " \n");
            
            iterRemaining--;
        }

        return gBest;
    }
}
