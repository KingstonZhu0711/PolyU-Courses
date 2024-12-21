#include <iostream>
#include <cstring>
using namespace std;
// Define public variables
const int size = 100;
int sizeOfArray = 0;
char charArray[size];
char temp[size];
int space = 0;
int check = 0;
// Defining function
void rotate(char *charArray, int *sizeOfArray)//using function with void return type   
{
    char b = charArray[0];
    for (int a = 0; a < *sizeOfArray - 1; a++)
    {
        charArray[a] = charArray[a + 1];
    }
    charArray[*sizeOfArray - 1] = b;
}
int main()
{
    cout << "Please enter your input(numbers or letters without space in between) " << endl;
    cin.getline(charArray, 100, '\n');
    sizeOfArray = strlen(charArray);
    if (sizeOfArray < 101)
    {
        strcpy(temp, charArray);
        for (int e = 0; e < sizeOfArray; e++)
        {
            if (temp[e] == ' ')
            {
                space = 1;
                break;
            }
            else
            {
                space = 0;
            }
        }
        if (space == 0)
        {
            cout << "This is the Output:" << endl;
            for (int c = 0; c < sizeOfArray; c++)
            {
                //calling the function after declaration.
                rotate(charArray, &sizeOfArray);
                cout << charArray << endl;
            }  
        }
        if (space == 1)
        {
            cout << "Please input again with no space in between letters or numbers" << endl;
            
        }
    }
    else
    {
        cout<<"You could only input 100 characters, Please input again"<<endl;
    }
    return 0;
}