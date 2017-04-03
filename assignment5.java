import java.io.*;
import java.util.*;

public class assignment5 {

    static int machines;
    static int budget;
    static ArrayList<Float> reliability = new ArrayList<Float>();
    static ArrayList<Integer> cost = new ArrayList<Integer>();

    public static void main(String[] args) {
        File inFile = null;
        String sCurrentLine;
        String[] line;
        BufferedReader br = null;
        // Read file input, store in array
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            budget = Integer.parseInt(br.readLine()); 
            machines = Integer.parseInt(br.readLine()); 
            
            while ((sCurrentLine = br.readLine()) != null) {
                line = sCurrentLine.split(" ");
                try {
                    cost.add(Integer.parseInt(line[0]));
                    reliability.add(Float.parseFloat(line[1]));
            } catch (NumberFormatException nfe) {};
        }
        br.close();
        // Call iterative version
        iterativeReliability();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    static double iterativeReliability() {
        double[][] maxReliability = new double[machines+1][budget+1];
        
        int i = 1;
        int b = 0;
        int k = 0;
        // Initialize arrays
        for (int j = 1; j <= machines; j++)
        {
            maxReliability[j][0] = 0;
        }

        for (b = 0; b <= budget; b++)
        {
            maxReliability[0][b] = 1;  
        }

        for (i = 1; i <= machines; i++) {
            for (b = 0; b <= budget; b++) {
                maxReliability[i][b] = 0;
                for (k = 1; k <= (Math.floor(b/cost.get(i - 1))); k++) {
                    maxReliability[i][b] = Math.max(maxReliability[i][b], 
                                                   (maxReliability[i-1][b-cost.get(i -1 )*k] * (1-Math.pow((1-reliability.get(i - 1)),k))));
                }
            } 
        }

        System.out.println("");
        System.out.println("Total Budget: " + budget);
        System.out.println("Total Number of Machines: " + machines);
        System.out.println("");
        System.out.println("Iterative Version: ");        
        System.out.println("Maximum Reliability: " + maxReliability[machines][budget]);
        System.out.println("");

        // Call memoized version
        System.out.println("Memoized Version: "); 
        System.out.println ("Maximum Reliability: " + memoizedReliability(machines, budget, prepareArray(budget, machines)));
        System.out.println("");
        return 0;
    }

    static double[][] prepareArray(int budget, int machines) {
        double [][] totalreliability = new double[machines+1][budget+1];
        for(int i = 0; i <= machines; i++)
        {
            for(int j = 0; j <= budget; j++) 
            {
                totalreliability[i][j]=-1;
            }
        }
        for (int j = 1; j <= machines; j++)
        {
            totalreliability[j][0] = 0;
        }

        for (int b = 0; b <= budget; b++)
        {
            totalreliability[0][b] = 1;
        }
        return totalreliability;
    }

    static double memoizedReliability(int i, int b, double [][]totalreliability) {
        if (totalreliability[i][b] == -1) {
            
        totalreliability[i][b] = 0;           
            for (int k = 1; k <= (Math.floor(b/cost.get(i - 1))); k++) {  
               totalreliability[i][b] = Math.max(totalreliability[i][b], 
                                                (memoizedReliability(i-1,b-cost.get(i - 1 )*k, totalreliability) * (1-Math.pow((1-reliability.get(i - 1)),k))));      
            }  
        return totalreliability[i][b];
        }
        else 
        {   
            return totalreliability[i][b];
        }
    }
}
