#read iris data from library
library(datasets)
library(ggplot2)
data(iris)
iris
a1<-c(1.4,1.0)
a2<-c(1.3,0.2)
a3<-c(1.7,0.1)
str<- PL <- iris$Petal.Length
str<- PW <- iris$Petal.Width
totalmeandistance1<-c()
totalmeandistance2<-c()
totalmeandistance3<-c()
#100 iterations grouping results
for (i in 1:100){
  #returning the data for every iteration
  a1distancePL<-c()
  a2distancePL<-c()
  a3distancePL<-c()
  a1distancePW<-c()
  a2distancePW<-c()
  a3distancePW<-c()
  meandistance1<-c()
  meandistance2<-c()
  meandistance3<-c()
  for(i in 1:150){
    distance1<-sqrt((PL[i]-a1[1])^2+(PW[i]-a1[2])^2)
    distance2<-sqrt((PL[i]-a2[1])^2+(PW[i]-a2[2])^2)
    distance3<-sqrt((PL[i]-a3[1])^2+(PW[i]-a3[2])^2)
    a<-c(distance1,distance2,distance3)
    Mind<-min(a)
    if(Mind==distance1){
      a1distancePL<-append(a1distancePL,PL[i])
      a1distancePW<-append(a1distancePW,PW[i])
      meandistance1<-append(meandistance1,Mind)
    }
    else if(Mind==distance2){
      a2distancePL<-append(a2distancePL,PL[i])
      a2distancePW<-append(a2distancePW,PW[i])
      meandistance2<-append(meandistance2,Mind)
    }
    else{
      a3distancePL<-append(a3distancePL,PL[i])
      a3distancePW<-append(a3distancePW,PW[i])
      meandistance3<-append(meandistance3,Mind)
    }
  }
  #New points for next iteration after grouping
  a1[1]=mean(a1distancePL)
  a1[2]=mean(a1distancePW)
  a2[1]=mean(a2distancePL)
  a2[2]=mean(a2distancePW)  
  a3[1]=mean(a3distancePL)
  a3[2]=mean(a3distancePW)
  #Appending the results for final line plot
  totalmeandistance1<-append(totalmeandistance1,mean(meandistance1))
  totalmeandistance2<-append(totalmeandistance2,mean(meandistance2))
  totalmeandistance3<-append(totalmeandistance3,mean(meandistance3))
}
#changing data into data frames
last_graph1<-data.frame(a1distancePL,a1distancePW)
last_graph2<-data.frame(a2distancePL,a2distancePW)
last_graph3<-data.frame(a3distancePL,a3distancePW)
#Final scatter plot for all grouped data samples
ggplot() + 
  geom_point(data=last_graph1,mapping = aes(x = a1distancePL, y = a1distancePW, color="Group1"))+
  geom_point(data=last_graph2,mapping = aes(x = a2distancePL, y = a2distancePW, color="Group2"))+
  geom_point(data=last_graph3,mapping = aes(x = a3distancePL, y = a3distancePW, color="Group3"))+
  labs(x="Petal Length",y="Petal Width")
#Line plot for the 100 iterations distances
Iteration<-c()
for(i in 1:100){
  Iteration[i]=i
}
#Putting each iteration in of each data
Line_last_graph1<-data.frame(totalmeandistance1)
Line_last_graph1$Iteration <- Iteration
Line_last_graph2<-data.frame(totalmeandistance2)
Line_last_graph2$Iteration <- Iteration
Line_last_graph3<-data.frame(totalmeandistance3)
Line_last_graph3$Iteration<- Iteration
#Final line plot
ggplot() +
  geom_line(data=Line_last_graph1,mapping=aes(x=Iteration,y=totalmeandistance1, color="Group1",))+
  geom_line(data=Line_last_graph2,mapping=aes(x=Iteration,y=totalmeandistance2, color="Group2",))+
  geom_line(data=Line_last_graph3,mapping=aes(x=Iteration,y=totalmeandistance3, color="Group3",))+
  labs(x="training iteration",y=" Mean distance to the cluster centroids")
  


