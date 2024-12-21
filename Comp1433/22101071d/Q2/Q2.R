#Part a
library(ggplot2)
#Please go to session-set working dictionary-choose dictionary and select Q2 folder as working dictionary to Load in data ！！！！
employees=read.csv("employees.csv", header=TRUE,sep=",")
Salary_Per_Year<-c()
str<- MS <- employees$Monthly_Salary
for (i in 1:35){
  Salary_Per_Year[i]=MS[i]*12
}
#Input Salery Per Year into data frame
employees$Salary_Per_Year <- Salary_Per_Year

#Part b
MaleTotalSalery<-0
FemaleTotalSalery<-0
cout<-1
for(i in employees$Gender){
  if(i=="Male"){
    MaleTotalSalery<-employees$Salary_Per_Year[cout]+MaleTotalSalery
  }
  else{
    FemaleTotalSalery<-employees$Salary_Per_Year[cout]+FemaleTotalSalery
  }
  cout=cout+1
}
#Calculate and Format results into two decimal places
MaleAverageSalery<-MaleTotalSalery/18
MaleAverageSalery<-format(round(MaleAverageSalery, 3), nsmall = 3)
FemaleAverageSalery<-MaleTotalSalery/17
FemaleAverageSalery<-format(round(FemaleAverageSalery, 3), nsmall = 3)
print(paste0("The average salary per year for male employees is ",MaleAverageSalery))
print(paste0("The average salary per year for female employees is ",FemaleAverageSalery))

#Part c
ggplot(data = employees) + 
  geom_histogram(mapping= aes(x = Years_of_Experience)) + 
  scale_x_continuous(breaks=seq(1,15,by=1))+
  labs(x="Years of Experience",y="Frequency")

#Part D
ggplot(data = employees) + 
  geom_point(mapping = aes(x = Years_of_Experience, y = Monthly_Salary, color=Gender))+
  labs(xlab="Years of Experience",ylab="Monthly Salary")+
  scale_x_continuous(breaks=seq(1,15,by=1))

