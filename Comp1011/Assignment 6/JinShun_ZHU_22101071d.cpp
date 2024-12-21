#include <iostream>
#include <cstring>
using namespace std;

int main()
{
    int const MAXSTUDENTS = 200;
    int const MAXLEN = 51;
    char students[MAXSTUDENTS][MAXLEN];
    int num = 0;
    cout << "Enter student names and input END to finish the input:"<<endl;
    while (num < MAXSTUDENTS)
    {
        cin.getline(students[num], 51, '\n');
        if (strcmp(students[num], "END") == 0)
        {
            break;
        }
        else
        {
            bool valid = true;
            int d= strlen(students[num]);
            for (int i = 0; i < d; i++)
            {
                if (!isalpha(students[num][i]))
                {
                    valid = false;
                    break;
                }
                students[num][i] = toupper(students[num][i]);
            }
            if (!valid)
            {
                cout << "Wrong input: please input only upper-case and low-case letters with no space in between"<<endl;
                continue;
            }
        }
        num++;
    }
    char temp[1][50];
    for (int a = 0; a < num - 1; a++)
    {
        for (int b = 0; b < num - 1 - a; b++)
        {
            int cmp = strcmp(students[b + 1], students[b]);
            if (cmp == -1)
            {
                strcpy(temp[0], students[b]);
                strcpy(students[b], students[b + 1]);
                strcpy(students[b + 1], temp[0]);
            }
        }
    }
    for (int c = 0; c < num; c++)
    {
        cout << students[c] << endl;
    }
    return 0;
}