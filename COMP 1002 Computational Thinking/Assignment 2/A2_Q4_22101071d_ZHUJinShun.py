def main():# This function is to find the character that occurs in a certain text and the location
    """Input text:t. The input value of the text so we could find the character 
Input a charcter to be searched:c The character in the text that occurs will be set as a value to be find in the text by using 
The character c in the text occured num times at L.The character c will be shown appeared num times as calculated using append to record the locations in a new list"""
    t=input("Input text:")
    c=input("Input a character to be searched:")
    L=[]#Set a new list to input the times of occurrence of the letter
    l=len(t)
    for x in range(0,l):#It will search all text in the list because it is the length of the text
        if t[x]==c:
            L.append(x+1)#put the location of the numbers in new list
    num=len(L)
    print("The character",c,"in the text occured",num,"times at location",L)
print("The docstring of the function is shown below")
print(main.__doc__)
main()
