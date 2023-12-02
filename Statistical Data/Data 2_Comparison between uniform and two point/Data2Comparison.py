import matplotlib.pyplot as plt
import numpy as np

# Creating the generation_numbers array from 0 to 799 = 800
generation_numbers = np.arange(800)

# Reading data for average_best_fitness and average_population_fitness from separate text files for two different configuration of algorithm
average_best_fitness1 = np.loadtxt('tp_avg_fitness_elites.txt')
average_best_fitness2 = np.loadtxt('uniform_avg_fitness_elites.txt')
average_population_fitness1 = np.loadtxt('tp_avg_fitness_population.txt')
average_population_fitness2 = np.loadtxt('uniform_avg_fitness_population.txt')

# Calculating mean and minimum values from average_best_fitness
mean_best_fitness1 = np.mean(average_best_fitness1)
mean_best_fitness2 = np.mean(average_best_fitness2)
min_best_fitness1 = np.min(average_best_fitness1)
min_best_fitness2 = np.min(average_best_fitness2)

# Creating a graph figure
fig, ax = plt.subplots(figsize=(8, 6))

# Plotting the average best fitness and average population fitness for two different configurations
ax.plot(generation_numbers, average_best_fitness1, label="Average Best Fitness: two pointer", color='black')
ax.plot(generation_numbers, average_best_fitness2, label="Average Best Fitness: uniform", color='green')
ax.plot(generation_numbers, average_population_fitness1, label="Average Population Fitness: two pointer", color='red')
ax.plot(generation_numbers, average_population_fitness2, label="Average Population Fitness: uniform", color='yellow')


# Plotting mean and minimum best fitness as horizontal lines
ax.axhline(mean_best_fitness1, color='brown', linestyle='--', label=f"Mean Fitness: two pointer {mean_best_fitness1:.4f}")
ax.axhline(mean_best_fitness2, color='brown', linestyle='--', label=f"Mean Fitness: uniform {mean_best_fitness2:.4f}")

ax.axhline(min_best_fitness1, color='purple', linestyle='--', label=f"Best Fitness: two pointer {min_best_fitness1:.4f}")
ax.axhline(min_best_fitness2, color='purple', linestyle='--', label=f"Best Fitness: uniform {min_best_fitness2:.4f}")


ax.set_xlabel("Generation Number")
ax.set_ylabel("Fitness")
ax.legend(loc='upper right')

# tittle
plt.title("DATA 2: Average Fitness per Generation\nand Average elites fitness comparison between uniform and two point")

# plot display
plt.show()



# summary statistics generate
# summary_statistics = {
#     'Minimum Fitness': np.min(average_best_fitness),
#     'Maximum Fitness': np.max(average_best_fitness),
#     'Median Fitness': np.median(average_best_fitness),
#     'Mean Fitness': np.mean(average_best_fitness),
#     'Standard Deviation': np.std(average_best_fitness)
# }

# # output summary stats
# for stat, value in summary_statistics.items():
#     print(f'{stat}: {value:.4f}')
