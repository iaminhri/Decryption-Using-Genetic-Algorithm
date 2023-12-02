/**
 * @ Name: Hridoy Rahman
 * @ Version: 1.0
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;
import java.util.Scanner;

public class GA{
    String alphabet = "abcdefghijklmnopqrstuvwxyz-";
    String [] Chromosomes;

    /**
     * Constructor calls for the GA_System function
     * @ param fname -> takes a file input
     * @ param popSize -> takes a population size input
     * @ param keySize -> Password length
     * @ param Pc -> probability of Crossover
     * @ param Pm -> Probability of Mutation
     * @ param chooseCrossover -> Chooses one of the crossover, can be change while instantiating GA Class
     */

    String text;
    int keySize;
    double Pc;
    double Pm;
    String chooseCrossover;
    int maxGeneration;
    int eliteCount;

    double [] avg_fitness_elites;
    double [] avg_fitness_population;


    public GA(File fname, int popSize, int maxGeneration, double Pc, double Pm, int eliteCount, String chooseCrossover){
        StringBuilder tempStr = new StringBuilder();
        try{
            Scanner read = new Scanner(fname);
            this.keySize = read.nextInt();
            read.nextLine();
            while(read.hasNextLine()){
                tempStr.append(read.nextLine());
            }
            read.close();
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        this.text = tempStr.toString();
        this.Pc = Pc;
        this.Pm = Pm;
        this.chooseCrossover = chooseCrossover;
        this.eliteCount = eliteCount;
        this.chooseCrossover = chooseCrossover;
        this.maxGeneration = maxGeneration;
        this.avg_fitness_elites = new double[maxGeneration];
        this.avg_fitness_population = new double[maxGeneration];

        System.out.println(this.keySize);
        System.out.println(this.text);

        //calling main GA_System
        GA_System(popSize);
    }

    /**
     * @ popSize: Population Size
     * @ Max_Gen: Maximum Iteration for generation
     * @ Pc : Probability of CrossOver
     * @ Pm : Probability of Mutation
     * Termination Condition is close to 2.3~2.5 values
     */

    //caution: you still have to check for duplicates
    public void GA_System(int popSize){
        double sum = 0;
        double elite_sum = 0;

        //Random Initialization of the population
        this.Chromosomes = initial_pop_initializer(popSize);

        // average fitness of the initial population population
        for(String avg : this.Chromosomes){
            sum += Evaluation.fitness(avg, text);
        }
        avg_fitness_population[0] = sum/popSize;

        System.out.println("-------------------- Generation 1 -------------------");

        String[] elite = elitism(this.Chromosomes, eliteCount); // first generation elite

        for(int k = 0; k < elite.length; k++){
            elite_sum += Evaluation.fitness(elite[k], text);
        }
        avg_fitness_elites[0] = elite_sum/elite.length;

        System.out.println("Decrypted Text:");
        System.out.println(Evaluation.decrypt(elite[0], text));

        System.out.println("-----------------------------------------------------");

        //Maximum generation Iteration
        for(int i = 1; i < maxGeneration; i++){
            System.out.println("-------------------- Generation " + i + " -------------------");
            String []newGeneration = new String[popSize];

            //copies the elites to new generations
            for(int j = 0; j < elite.length; j++){
                newGeneration[j] = elite[j];
            }

            //copies from elite.length to pop size and creates new population
            for(int j = eliteCount; j < popSize; j+=2){
                //Two parents selected using tournament selections
                String parent1 = tournamentSelection(this.Chromosomes);
                String parent2 = tournamentSelection(this.Chromosomes);

                //selects one of the crossover methods.
                if(this.chooseCrossover.equals("twopoint")){
                    String []child = twoPointCrossOver(parent1, parent2, this.Pc);
                    String child1 = mutate(child[0], Pm);
                    String child2 = mutate(child[1], Pm);

                    newGeneration[j] = child1;
                    newGeneration[j+1] = child2;
                }
                else if (this.chooseCrossover.equals("uniform")){
                    String []child = uniformCrossOver(parent1, parent2, this.Pc);
                    String child1 = mutate(child[0], Pm);
                    String child2 = mutate(child[1], Pm);

                    newGeneration[j] = child1;
                    newGeneration[j+1] = child2;
                }
            }

            // overwrites previous generation with new generations
            this.Chromosomes = newGeneration;

            elite_sum = 0;
            sum = 0;

            //computes Average per population
            for(String avg : this.Chromosomes){
                sum += Evaluation.fitness(avg, text);
            }
            //stores average fitness per population for ith indices
            avg_fitness_population[i] = sum/popSize;
            //_____________________________________________________________________________

            //Gets the best chromosome from the current generation
            elite = elitism(this.Chromosomes, eliteCount); // i-th generation elite

            //averages the fitness of the elites per generation and stores inside a fitness of elites array variable.
            for(int k = 0; k < elite.length; k++){
                elite_sum += Evaluation.fitness(elite[k], text);
            }
            avg_fitness_elites[i] = elite_sum/elite.length;

            //outputs decrypted text
            System.out.println("Decrypted Text:");
            System.out.println(Evaluation.decrypt(elite[0], text));

            System.out.println("-----------------------------------------------------");
        }

        // outputs each average section in a file.
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("avg_fitness_elites.txt"));
            for(Double fitness : avg_fitness_elites){
                writer.write(fitness.toString());
                writer.newLine();
            }
            writer.close();
        }catch (IOException e){
            System.out.println( e.getMessage());
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("avg_fitness_population.txt"));
            for(Double fitness : avg_fitness_population){
                writer.write(fitness.toString());
                writer.newLine();
            }
            writer.close();
        }catch (IOException e){
            System.out.println( e.getMessage());
        }
    }


    /**
     * finds elite of a generation returns best selected elites of that population
     * @param population -> takes population as input parameter
     * @param eliteCount -> takes a count of the elites, how many elites should be added to the next gen
     * @return -> returns an array with elites.
     */
    public String[] elitism(String [] population, int eliteCount){
        String [] elites = new String[eliteCount];

        double[] evaluatedFitness = new double[population.length];

        for(int i = 0; i < population.length; i++){
            evaluatedFitness[i] = Evaluation.fitness(population[i], text);
        }

        Arrays.sort(evaluatedFitness);

        System.out.println(elites.length);

        for(int i = 0; i < elites.length; i++){
            for(int j = 0; j < population.length; j++){
                if(Double.compare( evaluatedFitness[i], Evaluation.fitness(population[j], text)) == 0){
                    elites[i] = population[j];
                }
            }
        }

        System.out.println("elite[" + 0 + "]: " + elites[0] + " || Fitness: " + Evaluation.fitness(elites[0], text));

        return elites;
    }

    /**
     * Checks for duplicated string in an array of strings
     * @param str -> array of generated strings
     * @param str2 -> takes an string input to check if it's duplicated
     * @return -> true or false
     */
    public boolean checkDuplicate(String [] str, String str2){
        for(String s : str){
            if(s != null && s.equals(str2))
                return true;
        }
        return false;
    }

    /**
     * Generates a unique strings randomly, also checks if the generated string exists in the population array
     * @param pop -> takes an array as input parameter
     * @return -> returns a random string
     */
    public String generateUniqueString(String[] pop){
        String randomStr = "";
        do{
            randomStr = generate_randomString(this.alphabet, this.keySize);
        }while(checkDuplicate(pop, randomStr));

        return randomStr;
    }

    /**
     * Creates a generation of initial population
     * @param popSize -> population size
     * @return -> returns an array of initial population
     */
    public String[] initial_pop_initializer(int popSize){
        String [] initial_pop = new String[popSize];

        for(int i = 0; i < popSize; i++){
            initial_pop[i] = generateUniqueString(initial_pop);
        }

        return initial_pop;
    }

    /**
     * Takes a list of population of chromosomes and
     * selects two chromosomes randomly then chooses the best one.
     */
    public String tournamentSelection(String [] population){
        String best = "";

        //generates a random number
        int check = rand(2, 5);

        int []tempSize = new int[check];
        String []tempPopulation = new String[check];

        //generates random numbers of random size based on check
        for(int i = 0; i < tempSize.length; i++){
            tempSize[i] = rand(0, population.length-1);
        }

        // checks for uniqueness of random values
        for(int i = 0; i < check; i++){
            while(containsParent(tempSize, tempSize[i]) >= 2){
                tempSize[i] = rand(0, population.length-1);
            }
        }

        // copies population to temporary population based on random numbers from population array
        for(int i = 0; i < tempPopulation.length; i++){
            tempPopulation[i] = population[tempSize[i]];
        }

        //checks which is the best parent.
        for(int i = 0; i < tempPopulation.length; i++){
            for(int j = i; j < tempPopulation.length; j++){
                if(Evaluation.fitness(tempPopulation[i], text) < Evaluation.fitness(tempPopulation[j],text))
                    best = tempPopulation[i];
                else
                    best = tempPopulation[j];
            }
        }

        //returns the best parent.
        return best;
    }

    /**
     * Checks if a random number is already inside an int array or not.
     * @param arr -> takes an integer array
     * @param check -> checks against the check integer value
     * @return -> returns a counter
     */
    public int containsParent(int []arr, int check){
        int counter = 0;
        for(int i : arr){
            if(i == check)
                counter++;
        }
        return counter;
    }

    /**
     * Performs uniform crossover
     * @param parent1 -> takes parent1 as input param
     * @param parent2 -> takes parent2 as input param
     * @param Pc -> probability of crossover
     * @return -> returns two new children
     */
    public String [] uniformCrossOver(String parent1, String parent2, double Pc){
        int [] mask = new int[parent1.length()];
        char [] child1 = new char[parent1.length()];
        char [] child2 = new char[parent2.length()];
        String [] childs = new String[2];

        if(Math.random() < Pc){
            for(int i = 0; i < mask.length; i++){
                double random = Math.random();
                if(random < 0.5)
                    mask[i] = 0;
                else
                    mask[i] = 1;
            }

            for(int i = 0; i < parent1.length(); i++){
                if(mask[i] == 0){
                    child1[i] = parent2.charAt(i);
                    child2[i] = parent1.charAt(i);
                }
                else{
                    child1[i] = parent1.charAt(i);
                    child2[i] = parent2.charAt(i);
                }
            }

            childs[0] = toString(child1);
            childs[1] = toString(child2);
        }
        else{
            childs[0] = parent1;
            childs[1] = parent2;
        }

        return childs;
    }

    /**
     * takes two chromosomes performs crossovers and returns two new genes
     * @ Pc -> probability of crossover
     * Use the Pc to choose between the parent and child
     * The crossover function runs in each chromosome inside the population array
     */
    public String[] twoPointCrossOver(String parent1, String parent2, double Pc){
        String child1, child2;
        String [] childs = new String[2];

        if(Math.random() < Pc){
            int len1 = parent1.length();

            // Checks if random number 1 == random number 2 or random number 1 > random number 2
            // it generates random numbers until those two conditions are met.
            int r1, r2;
            do{
                r1 = rand(0, len1);
                r2 = rand(0, len1);
            }while(r1 == r2 || r1 > r2);

            /**
             *  Implementation of two point crossover.
             *  Copies parent 1's values from range 0 to r1 to child1
             *  Copies parent 2's values from range r1 to r2 to child1
             *  Copies parent 1's values from range r2 to length to child1
             */

            child1 = copyValue(parent1, 0, r1) + copyValue(parent2, r1,r2) + copyValue(parent1, r2, len1);
            child2 = copyValue(parent2, 0, r1) + copyValue(parent1, r1,r2) + copyValue(parent2, r2, len1);

            childs[0] = child1;
            childs[1] = child2;
        }
        else{
            childs[0] = parent1;
            childs[1] = parent2;
        }

        return childs;
    }

    // copies value from range to range from a parent
    public String copyValue(String parent, int first, int range){
        return parent.substring(first, range);
    }

    /**
     * given a chromosome it will mutate the chromosome and return it.
     * @ param: Pm -> Probability of mutation 0-1
     */
    public String mutate(String str1, double Pm){
        char [] tempCharArr = str1.toCharArray();

        if (Math.random() < Pm){
            int r1 = rand(0, str1.length()-1);
            int r2 = rand(0, str1.length()-1);

            char temp = tempCharArr[r1];
            tempCharArr[r1] = tempCharArr[r2];
            tempCharArr[r2] = temp;
        }
        else{
            return str1;
        }

        return toString(tempCharArr);
    }

    /**
     * Generates random numbers
     */
    public static int rand(int lb, int ub){
        return (int)(Math.random() * ub-lb) + lb;
    }

    /**
     * Generates random strings based on given alphabet sequence and a keysize
     * @param alphabet -> "a-z" && "-"
     * @param keySize -> based on input files first line.
     * @return -> returns a string of random generated alphabets
     */

    public String generate_randomString(String alphabet, int keySize){
        StringBuilder str = new StringBuilder();
        int lb = 0, ub = alphabet.length()-1;
        for(int i = 0; i < keySize; i++){
            int rand = rand(lb, ub);
            str.append(alphabet.charAt(rand));
        }
        return str.toString();
    }

    /**
     * Converts an character to String.
     * @param charArr -> Takes a character array as input
     * @return -> returns a String
     */
    public static String toString(char[] charArr){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < charArr.length; i++){
            str.append(charArr[i]);
        }
        return ""+str;
    }
}
