# Load the iris dataset
data <- iris[,c(3,4)]

# Set the number of clusters
K <- 3

# Initialize cluster centroids
centroids <- data.frame(
  x = c(1.4, 1.3, 1.7),
  y = c(0.1, 0.2, 0.1)
)

# Define a function to calculate the Euclidean distance between two points
euclidean_distance <- function(x, y) {
  sqrt(sum((x - y) ^ 2))
}

# Initialize empty vectors to store cluster assignments and mean distances
assignments <- numeric(nrow(data))
mean_distances <- numeric(100)

# Run the k-means algorithm for 100 iterations
for (iteration in 1:100) {
  
  # Assign each data point to the nearest centroid
  for (i in 1:nrow(data)) {
    distances <- apply(centroids, 1, function(c) euclidean_distance(data[i,], c))
    assignments[i] <- which.min(distances)
  }
  
  # Calculate the mean distance of data points to their respective cluster centroid
  for (k in 1:K) {
    cluster_points <- data[assignments == k,]
    centroid <- colMeans(cluster_points)
    centroids[k,] <- centroid
    mean_distances[iteration] <- mean_distances[iteration] + sum(apply(cluster_points, 1, function(p) euclidean_distance(p, centroid))) / nrow(data)
  }
}

# Plot the scatter plot of the data with colors indicating the assigned cluster
library(ggplot2)
ggplot(data, aes(x=Petal.Length, y=Petal.Width, color=factor(assignments))) + 
  geom_point() + 
  scale_color_manual(values=c("red", "green", "blue"))

# Plot the line plot of mean distances over iterations
library(ggplot2)
ggplot(data.frame(x=1:100, y=mean_distances), aes(x=x, y=y)) + 
  geom_line()

