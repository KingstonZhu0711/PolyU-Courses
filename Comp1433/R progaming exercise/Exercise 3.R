library("ggplot2")
data1 = read.csv("/Users/Zhu Jin Shun/OneDrive - The Hong Kong Polytechnic University/Desktop/Comp1433/R progaming exercise/BigMart.csv", header=TRUE,sep=",")
ggplot(data= data1)+geom_histogram(mapping=aes(x=Item_Outlet_Sales),binwidth=500,fill="blue",color="black")
ggplot(data = data1) + 
  geom_point(mapping = aes(x = Item_MRP, y = Item_Outlet_Sales,color=Outlet_Type,shape=Outlet_Type))+
  facet_wrap(~Outlet_Type,nrow=2)
  