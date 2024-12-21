### Q3(b)
set.seed(42)
r = 20
p = 0.5
x_lower_bound = 0
x_higher_bound = 100

M<-matrix(nrow= x_higher_bound - x_lower_bound + 1, ncol=3)

x_dnbinom <- seq(x_lower_bound, x_higher_bound, by = 1)
M[,1]<-x_dnbinom
### true probabilities
y_dnbinom <- dnbinom(x_dnbinom, size = r, prob = p)  # Apply dnbinom function
M[,2]<-y_dnbinom
### simulated frequencies
sim <- rnbinom(n = 10000, size = r, prob = p)

for (i in 1:length(x_dnbinom)){
  M[i,3] <- mean(sim == x_dnbinom[i])
}

df <- as.data.frame(M)
colnames(df)<-c("Number of failures", "True", "Simulation")
library('ggplot2')
library('tidyverse')

df = df %>% 
  gather("type","value",2:3)

df %>%
  ggplot( aes(x=df$`Number of failures`, y=value, color = type)) +
  geom_line() +
  geom_point()+ 
  labs(title = 'Probability mass function of negative binomial distribution (r = 20,p = 0.05)',y = "Probability", x = "Number of failures")

### Q3(d)
# Y is the number of lives lost by an cat in 104 weeks, Y follows a binomial distribution (104,0.05)
# Sum it up from y = 0 to 8
p = 0
for (i in 0:8){
  p = p + dbinom(i, size=104, prob=0.05) 
  print(i)
}
print(p)

