days<-365
change<-rnorm(365,1.001,0.005)
price<-cumprod(c(30,change))
plot(price,type ="l")
