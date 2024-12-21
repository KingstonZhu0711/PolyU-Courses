#include <iostream>
using namespace std;
int Largest(int arr[], int n)
{
    int o;
    int max = arr[0];
    for (o = 1; o < n; o++)
        if (arr[o] > max)
            max = arr[o];
    return max;
}
int Smallest(int arr[], int n)
{
    int e;
    int min = arr[0];
    for (e = 1; e < n; e++)
        if (arr[e] < min)
            min = arr[e];
    return min;
}
int Sum(int arr[], int n)
{
    int d;
    int sum = 0;
    for (int d = 0; d < n; d++)
    {
        sum = sum + arr[d];
    }
    return sum;
}
float Average(int arr[], int n)
{
    int g;
    int sum = 0;
    double f;
    for (int g = 0; g < n; g++)
    {
        sum = sum + arr[g];
    }
    f = (sum * 1.0) / n;
    return f;
}
int main()
{
    int a[1000];
    for (int i = 0; i < 1000; i++){
        a[i] = -999;
    }
    cout << "Enter a sequence of integers(-999 to finish): ";
    int cnt = 0;
    int n = 0;
    int y = 0;
    int o = 0;
    while (cnt < 1000)
    {
        int tmp;
        cin >> tmp;
        if (tmp == -999){
            break;
        }
        else{
            a[cnt] = tmp;
        }
        cnt += 1;
    }
    int max = Largest(a, cnt);
    cout << "Largest number is ";
    cout << max << endl;
    int min = Smallest(a, cnt);
    cout << "Smallest number is ";
    cout << min << endl;
    int sum = Sum(a, cnt);
    cout << "Total is ";
    cout << sum << endl;
    float f = Average(a, cnt);
    cout << "Average is ";
    printf("%.3f", f);
    return 0;
}