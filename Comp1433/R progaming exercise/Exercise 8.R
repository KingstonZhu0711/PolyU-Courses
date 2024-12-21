roil.coin<-function(){
  v<-round(runif(1,0,1))
  return(v)
}
simulate.bettor<-function(funds,wager_per_game,game_count){
  total_wager =funds
  x<-numeric()
  y<-numeric()
  for (i in 1:game_count){
    print(i)
    v=roll.coin()
    if(v==1){
      total_wager=total_wager+wager_per_game
    }else{
      total_wager=total_wager-wager_per_game
    }
    total_wager=total_wager-1
    x<-append(x,log10(i))
    y<-append(y,total_wager)
  }
  df<-dta.frame(X=x,Y=y)
  reeturn(df)
}
library("ggplot2")
vals<-simulate.bettor(funds=10000,wager_per_game = 100,game_count = 100)
ggplot(data=vals,mapping=aes(x=X,y=Y))+geom_line()+
  xlab('Game Count,log10(X)') +ylab('Total Wager','Y')
