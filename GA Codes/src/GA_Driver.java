/**
 * @ Name: Hridoy Rahman
 * @ Version: 1.0
 */

import java.io.File;
import java.util.Scanner;

public class GA_Driver {
    public static void main(String[] args){

        //change the string to change data set file.
        Scanner keyIn = new Scanner(System.in);
        String filename;

        System.out.println("Enter A File Name('Data1.txt'): ");
        filename = keyIn.nextLine();

        try{
            File fname = new File(filename);

            System.out.println("Enter Population Size(Integer): ");
            int populationSize;
            populationSize = keyIn.nextInt();
            keyIn.nextLine();

            System.out.println("Enter Maximum Generation(Integer): ");
            int maxGeneration;
            maxGeneration = keyIn.nextInt();
            keyIn.nextLine();

            System.out.println("Enter Probability of Crossover (Pc) (Double): ");
            double Pc;
            Pc = keyIn.nextDouble();
            keyIn.nextLine();

            System.out.println("Enter Probability of mutation (Pm) (Double): ");
            double Pm;
            Pm = keyIn.nextDouble();
            keyIn.nextLine();

            System.out.println("Choose a Configuration ('twopoint' or 'uniform'): ");
            String chooseCrossover;
            chooseCrossover = keyIn.nextLine();
            keyIn.close();

            /**
             * @param: file_name
             * @param: population Size
             * @param: Max Generation
             * @param: Probability of Crossover, Pc
             * @Param: Probability of Mutations, Pm
             * @param: EliteCount -> decides how many elites to carry over next generation
             * @param: chooseCrossover -> Selects one of the crossover methods, ("two point" and "uniform")
             */
            //change these parameters to check for different parameter settings
            new GA( fname,populationSize, maxGeneration, Pc, Pm, 2, chooseCrossover);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
