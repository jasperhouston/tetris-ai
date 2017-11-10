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
    public static void main(String[] args) {
        PSO alg = new PSO(10, 10);
        Vector<Double> results = alg.solvePSO();
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i) + " ");
        }
    }
}
