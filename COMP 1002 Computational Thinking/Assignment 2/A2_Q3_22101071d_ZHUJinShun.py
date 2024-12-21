 

def myMax():#define a function to find the largest in the list and list out the location
    """Entered a list of numbers a. The list will find out the maximal number by comparing every number in the list and remembering the location by adding one each time finding the maximal.
    The maximal number is max1. Which is found after comparing each number in list. If there is a larger number found a new max will replace the x before. But if no new x is found, the if will stop and the max won't change
    Its location index is location. Which is found while we find the maximal number, it adds 1 to the x everytime when finding max, and will stop adding when max is found."""
    a=list(eval(input("Please enter a list of different numbers separated by ',':")))
    length=len(a)
    x=0
    max1=a[x]
    while x <length:#if x is less than the numbers of letters in list a, continue
        if a[x] > max1:
            max1=a[x]
            location= x + 1
        x=x+1
    
    print("The maximal number is ",max1)
    print("Its location number is ",location)

print("The docstring for myMax is shown below")
print(myMax.__doc__)
myMax()


def mySort():
    """Please enter a list of different numbers separated by ',':b. A new list will be asked to enter for finding the values
A list of sorting values in desrending order:  c. c will be the new list after adding the numbers from list b in descending order by using mymax function to find the largest and continue till no numbers left in list b"""
    b=list(eval(input("Please enter a list of different numbers separated by ',':")))
    c=[]
    def mymax():# define a new function to find the largest value in the list b to put in new list c
       while len(b)>0:#repeat till all numbers in list b is removed
            length=len(b)
            x=0
            max2=b[x]
            while x <length:
                if b[x] > max2:
                    max2=b[x]
                x=x+1
            c.append(max2)#put into new list
            b.remove(max2)#remove the number in list b
                
    mymax()
    print("A list of sorting values in desrending order: ",c)

print("The docstring for mySort is shown below")
print(mySort.__doc__)
mySort()
