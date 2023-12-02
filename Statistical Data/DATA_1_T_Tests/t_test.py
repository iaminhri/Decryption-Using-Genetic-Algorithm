#this program performs t-tests between two files.

import numpy as np
from scipy import stats

#reads data line from file
def read_data_from_file(file_path):
    with open(file_path, 'r') as file:
        data = [float(line.strip()) for line in file]
    return data

# performs t-test 
def perform_t_test(data1, data2):
    t_stat, p_value = stats.ttest_ind(data1, data2)
    return t_stat, p_value

# main function

file_path1 = "twopoint_fitness_data.txt" 
file_path2 = "uniform_fitness_data.txt" 

data1 = read_data_from_file(file_path1)
data2 = read_data_from_file(file_path2)

t_stat, p_value = perform_t_test(data1, data2)

print("T-Statistic:", t_stat)
print("P-Value:", p_value)

# if p value is less than alpha value 0.5 then rejects the null hypothesis
if p_value < 0.5:
    print("Reject the null hypothesis. There is a significant difference between the two crossover methods.")
    # checks which mean data is less to check which method performs better.
    if np.mean(data1) < np.mean(data2):
        print("The 'two-point' crossover method performs better.")
    else:
        print("The 'uniform' crossover method performs better.")
else:
    print("Fail to reject the null hypothesis. There is no significant difference between the two crossover methods.")