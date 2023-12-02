This folder contains the datafiles required to finish Assignment 2.
The details of each of the included files are as follows:

- Data1.txt and Data2.txt are the encrypted text you should be using for your experiments.
Each of these text files contains a number at the beginning (either 26 or 40 depending on the file you are using)
indicating the maximum length of the key.
The remainder of the file is the encrypted text as it is provided in the assignment write up.

- Evaluation.java contains the code for the fitness evaluation.
Note that for these functions both the key and the text to be encrypted/decrypted should each be passed in as Strings.
The methods within Evaluation.java which will be useful for your assignment are as follows:
- public static String decrypt(String k, String t): decrypt the text in string 't' using key 'k'.
- public static String encrypt(String k, String t): encyrpt the text in string 't' using the key 'k'
- public static double fitness(String k, String t):
returns the fitness of text resulting from using key 'k' to decrypt the text 't'.
This fitness function is based on the expected frequency of each character in the english language.
There is lots of room for improvement in this function, feel free to experiment with this for the bonus marks.