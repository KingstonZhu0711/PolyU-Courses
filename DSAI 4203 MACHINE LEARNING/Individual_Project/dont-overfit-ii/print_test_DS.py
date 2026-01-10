import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

test_data = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv')

plt.figure(figsize=(10, 8))
sns.scatterplot(data=test_data, x='0', y='1')

plt.title('Distribution of Testing Dataset')
plt.xlabel('Feature 2')
plt.ylabel('Feature 1')

plt.show()