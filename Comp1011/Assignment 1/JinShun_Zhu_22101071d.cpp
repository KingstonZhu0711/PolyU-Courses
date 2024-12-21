//student name:Zhu Jin Shun Student ID:22101071d
#include <iostream>
#include <cmath>
#include <iomanip>
using namespace std;
int main()
{
    cout << "MENU" << endl;
    cout << "\t1.Divide.a/b" << endl;
    cout << "\t2.Multiply.a*b" << endl;
    cout << "\t3.Power.a^b" << endl;
    cout << "\t4.Square root.sqrt(a)" << endl;
    
    double a;
    cout << "Enter your choice:";
    cin >> a;
    if (a==1|a==2|a==3|a==4){
        if (a == 1)
        {
            double b, c, d;
            cout << "Enter two numbers:";
            cin >> b;
            cin >> c;
            d = b / c;
             
            cout << b;
            cout << "/";
             
            cout << c;
            cout << "=";
            
            cout << d<< endl;
        }
        if(a == 2)
        {
            double b, c, d;
            cout << "Enter two numbers:";
            cin >> b;
            cin >> c;
            d = b * c;
            cout << b;
            cout << "*";
            cout << c;
            cout << "=";
            
            cout<<d<<endl;
        }
        if(a == 3){
            double b,c,d;
            cout << "Enter two numbers:";
            cin >> b;
            cin >> c;
            d=pow(b,c);
            cout << b;
            cout << "^";
            cout << c;
            cout << "=";
            
            cout<<d<<endl;
        }
        if(a == 4){
            double b,c;
            cout << "Enter a number:";
            cin >> b;
            if (b>=0){
                c=sqrt(b);
                cout << "sqrt";
                cout << "(";
                cout << b;
                cout << ")";
                cout << "=";
                
                cout<< c<<endl;
            }
            else{
                cout<<"Please enter a number greater or equal to 0"<<endl;
            }
        }
    }
    else{
        cout << "Please choose from the functions 1 to 4" << endl;
    }
return 0;
}