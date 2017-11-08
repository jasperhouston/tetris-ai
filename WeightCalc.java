import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.AlphaComposite;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class WeightCalc
{
    public static void main() {
        PSO alg = new PSO(10, 1000);
        Vector<Double> results = alg.solvePSO();
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i) + " ");
        }
    }
}
