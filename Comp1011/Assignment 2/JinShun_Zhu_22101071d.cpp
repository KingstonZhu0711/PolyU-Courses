#include <iostream>
#include <iomanip>
using namespace std;
int main()
{
    int a, b, c, d;
    
    cout << "Please input the branch size: ";
    cin >> a;
    d= a%2;
    b = a / 2;
    c = 1;
    int num = 65;
    int i = 0, j = 0, k = 0;
    if (d> 0)
    {
        cout << "You could only input an even number as branch size" << endl;
    }
    if(d==0)
    {

        while (i <= a)
        {
            while (k <= a - i - 1)
            {
                cout << " ";
                k++;
            }
            k = 0;
            if (num > 90)
            {
                num = 65;
            }
            while (j < 2 * i - 1)
            {
                if (num >= 65 && num <= 90)
                {
                    char ch = char(num);
                    cout << ch << "";
                    num++;
                    if (num > 90)
                    {
                        num = 65;
                    }
                }
                j++;
            }
            j = 0;
            i++;
            cout << endl;
        }
        if (i = a)
        {
            while (c <= b)
            {
                int f;

                f = a - 2;
                while (f > 0)
                {
                    cout << " ";
                    f = f - 1;
                }
                if (num >= 65 && num <= 90)
                {
                    char ch = char(num);
                    cout << ch << "";
                    cout << " ";
                    cout << ch << endl;
                    num++;
                    if (num > 90)
                    {
                        num = 65;
                    }
                }
                c++;
            }
        }
    }
    return 0;
}