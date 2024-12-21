# Here are the solutions
# (1) Load the data from the file into an R data frame called "employees":
employees <- read.csv("employees.csv")

# (2) Create a new column called "Salary Per Year" that contains the calculated salary per year for each employee (Monthly Salary multiplied by 12). Add this column to the "employees" data frame:
employees$Salary_Per_Year <- employees$Monthly_Salary * 12

# (3) Find out the average salary per year for male and female employees separately, and print the results on the screen with the following template (2 lines altogether):
avg_salary_male <- mean(employees$Salary_Per_Year[employees$Gender == "Male"])
avg_salary_female <- mean(employees$Salary_Per_Year[employees$Gender == "Female"])
cat("The average salary per year for male employees is", round(avg_salary_male, 2),"\n")
cat("The average salary per year for female employees is", round(avg_salary_female, 2),"\n")

# (4) Create a histogram to visualize the distribution of years of experience among all employees. Label the x-axis as "Years of Experience" and the y-axis as "Frequency":
hist(employees$Years_of_Experience, xlab = "Years of Experience", ylab = "Frequency")
# or 
library(ggplot2)
ggplot(employees) + geom_histogram(mapping=aes(x=Years_of_Experience)) + xlab("Years of Experience") + ylab("Frequency")

# (5) Create a scatter plot to visualize the relationship between years of experience and monthly salary among all employees. Label the x-axis as "Years of Experience" and the y-axis as "Monthly Salary". Use different colors to distinguish male and female employees:
library(ggplot2)
ggplot(data = employees, aes(x = Years_of_Experience, y = Monthly_Salary, color = Gender)) + 
  geom_point() + 
  xlab("Years of Experience") + 
  ylab("Monthly Salary")


