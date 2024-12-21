set.seed(2)
serviceTime<-rnorm(100,0.9,0.25)
firstArrival<-1.0
interArrivaltime<-rexp(99,1)
arrivaltime<-cumsum(c(firstArrival,interArrivaltime))
leavetime<-0.0
waittimes<-0.0
waitimesall<-array(0,100)

for(i in 1:100){
  waitimes<-max(0,leavetime-arrivaltime[i])
  leavetime<-arrivaltime[i]+waitimes +serviceTime[i]
  waitimesall[i]<-waitimes
}
plot(waitimesall,xlab="Customer ID",ylab="Wait times")
