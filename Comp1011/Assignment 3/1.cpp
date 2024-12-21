#include <iostream>
#include<cstring>
using namespace std;
int main()
{
    char fn[10];
    char ln[10];
    int a = 0;
    int b = 0;
    cout << "Please enter the first name: ";
    cin.getline(fn, 10, '\n');
    cout << "Please enter the last name: ";
    cin.getline(ln, 10, '\n');
    int c=strlen(fn);
    int d=strlen(ln);
    char tmp1[10];
    char tmp2[10];
    for(int e=0;e<=c;e++){
        tmp1[e]=fn[e];
        cout<<tmp1<<endl;
    }
    for(int f=0;f<d;f++){
        tmp2[f]=ln[f];
        cout<<tmp1<<" "<<tmp2<<endl;
    }
    for(int g=d-1;g>0;g--){
        cout<<tmp1<<" ";
        for(int k=0;k<g;k++){
            cout<<tmp2[k];
        }
        cout<<endl;
    }
    cout<<tmp1<<endl;
    for(int z=c;z>0;z--){
        for(int v=0;v<z;v++){
            cout<<tmp1[v];
        }
        cout<<endl;
    }
    return 0;
}