#Part B
library(ggplot2)
set.seed(42)
# Simulate 10,000 random variables from negative binomial distribution
a = rep(1:100)
N<-10000
X_rnbinom <- rnbinom(N, size = 20, prob = 0.5) 
X_rnbinom <- table(X_rnbinom)
Graph2<-data.frame(X_rnbinom)
Graph2$Freq = Graph2$Freq/10000
# Calculate the theoretical probability mass function and put into data frame
X_dnbinom <- dnbinom(rep(1:100),20,0.5)
Graph<-data.frame(X_dnbinom,a)
# Plot the simulated and theoretical probability mass functions
ggplot() + 
  geom_point(data = Graph2,mapping = aes(x = X_rnbinom, y = Freq,color="Simulated"))+
  geom_point(data = Graph,mapping = aes(x =a , y = X_dnbinom,color="Theoretical"))+
  labs(x="Number of failures until get the 20th success",y="Probability")+
  scale_x_discrete(breaks=seq(0,100,by=5))

#Part D
#simulation for Montgomery will survive for another 104 weeks if he has 9 lives left
X_rnbinom1 <- rnbinom(550000, size = 1, prob = 0.05) 
Number_of_sucsesses1<-sum(X_rnbinom1 == 103)
Probability1<-Number_of_sucsesses1/600000
theoretical_Probability1<-0.0002538
print(paste0("The simulated probability of Montgomery will survive for another 104 weeks if he has 9 lives left ",Probability1))
print(paste0("The theoretical probability of Montgomery will survive for another 104 weeks if he has 9 lives left ",theoretical_Probability1))

#simulation for Montgomery will survive for another 104 weeks if he has 1 life left
X_rnbinom2 <- rnbinom(550000, size = 9, prob = 0.05) 
Number_of_sucsesses2<-sum(X_rnbinom2 == 95)
Probability2<-Number_of_sucsesses2/600000
theoretical_Probability2<-0.003553
print(paste0("The simulated probability for Montgomery will survive for another 104 weeks if he has 1 life left is ",Probability2))
print(paste0("The theoretical probability for Montgomery will survive for another 104 weeks if he has 1 life left is ",theoretical_Probability2))

