set.seed(1)

coin_toss<- function(){
  first_coin <-round(runif(1,0,1))
  second_coin<-round(runif(1,0,1))
  res<- c(first_coin,second_coin)
  while(sum(tail(res,2)!=2)){
    res <-append(res,round(runif(1,0,1)))
  }
  l=length(res)
  return(l)
}

num_times_all <-c(100,1000,10000)
for (num_times in num_times_all){
  num_coin<-0
  for(i in 1:num_times){
    num_coin <- num_coin + coin_toss()
  }
  print(num_coin/num_times)
}
