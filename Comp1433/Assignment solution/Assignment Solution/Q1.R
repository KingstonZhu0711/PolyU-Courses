#library for plot
require(ggplot2)

#load data
library(datasets)  # Load/unload base packages manually
data = iris[3:4]
  
# specify the number of cluster
cluster <- 3
# initialize the cluster centroids
centroids.pl <- c(1.44, 1.3, 1.7)
centroids.pw <- c(0.1, 0.2, 0.1)
centroid <- cbind.data.frame(centroids.pl, centroids.pw)

#create a matrix to store distance of data points to their cluster centroid
distance <- matrix(0, nrow = nrow(data), ncol = cluster)
#record the mean distance to cluster centroid during kmeans training
distance.mean_summary <- rep(0, 100)

#creating a vector to store the cluster ID for each data point
c <- matrix(0, nrow = nrow(data), ncol = 1)

#creating a matrix to store new cluster centroids
update_centroid <- matrix(0, nrow = nrow(centroid), ncol = ncol(data))

#stopping criteria for kmean clustering
status <- 1

#iteration variable
iter <- 0

# plot the initial data points
ggplot(data,aes(x=Petal.Length, y=Petal.Width)) + 
  geom_point()

# stop the algorithm when it reaches the Maximum number of iterations or model converges
while (iter <= 1000 && status==1) 
{
  iter <- iter + 1
  #calculate the distance between each data point and cluster centroids
  for (j in 1:cluster)
  { 
    for (i in 1:nrow(data))
    {
      distance[i,j] = sqrt(sum((data[i,1:ncol(data)] - centroid[j,1:ncol(centroid)])^2))
    }
  }
  
  #assign new cluster ID to each data point
  distance_iter = 0
  for(i in 1:nrow(distance))
  {
    c[i] <- (which(distance[i,] == min(distance[i,]), arr.ind = T)) - 1
    distance_iter = distance_iter + min(distance[i,])
  }
  distance.mean_summary[iter] = distance_iter/nrow(distance)
  
  #calculate the new centroid based on new clustered data points
  compare <- cbind(data, c)
  
  for (i in 1:cluster)
  {
    x <- subset(compare[,1:2], compare[,3] == i-1)
    
    for(j in 1:ncol(data))
    {
      update_centroid[i,j] <- mean(x[,j])
    }
  }
  
  #update the current centroid
  if(all(update_centroid == centroid)){
    status = 0
    message("Training finish at iter: ", iter)
  }
  else {
    status = 1
    for (i in 1:cluster)
    {
      for (j in 1:ncol(centroid))
      {
        centroid[i,j] <- update_centroid[i,j]
      }
    } 
  }
}

# plot the learning curve for k-means clustering training
distance = data.frame(iter=1:100, distance=distance.mean_summary)
ggplot(distance, aes(x = iter, y = distance, color="red")) +  
  geom_line()

# plot the kmeans clustering results
ggplot(data, aes(x = Petal.Length, y = Petal.Width, color = factor(c))) + 
  geom_point()


