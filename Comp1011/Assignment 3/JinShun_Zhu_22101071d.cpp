#include <iostream>
using namespace std;

int main()
{
  int a[1000];
  for(int i =0;i<1000;i++){
    a[i]=-999;
  }
  cout << "Enter a sequence of integers(-999 to finish): ";
  int cnt = 0;
  int sgn=1;
  int sum=0;
  while (cnt<1000) //length check to avoid more input out of range
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
    sum+=sgn*a[cnt];
    sgn *=-1;
    cnt += 1;
  }
  cout<<sum<<endl;
  return 0;
}