#include <iostream>
using namespace std;

void Sort(int arr[], int n)
{
    int i, j;
    for (i = 0; i < n - 1; i++)
        for (j = 0; j < n - i - 1; j++)
            if (arr[j] > arr[j + 1])
                swap(arr[j], arr[j + 1]);
}

int main()
{
    int a[1000];
    for (int i = 0; i < 1000; i++)
    {
        a[i] = -999;
    }
    cout << "Enter a sequence of integers(-999 to finish): ";
    int cnt = 0;
    int n = 0;
    int y = 0;

    while (true)
    {
        int tmp;
        cin >> tmp;
        if (tmp == -999)
        {
            break;
        }
        else
        {
            a[cnt] = tmp;
        }
        cnt += 1;
    }
    Sort(a, cnt);

    while (n < cnt)
    {
        if (a[n] % 2 != 0)
        {
            cout << a[n]<<" ";
        }
        n++;
    }
    y = cnt-1;
    while (y >= 0){
        if (a[y] % 2 == 0)
        {
            cout << a[y]<<" ";
        }
        y--;
    }

    return 0;
}