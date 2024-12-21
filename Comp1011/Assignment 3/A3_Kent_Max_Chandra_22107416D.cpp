#include <iostream>
#include <cmath>
using namespace std;

int main(){
    int array1[1000] = {};
    int count = 0;
    int user_input;
    cout << "Please enter a sequence of integer ('-999' to finish): ";
    do{
        cin >> user_input;
        if (user_input != -999){
            array1[count] = user_input;
            count ++;
        }
    } while( user_input != -999 && count < 1000);
    int total = 0;
    for (int i = 0; i < count; i++){
        total = total + int(pow(-1,i))*array1[i];  
    }
    cout << total << endl;
    
}
