import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE

train_data = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv')

X = train_data.drop(['id', 'target'], axis=1)
y = train_data['target']

tsne = TSNE(n_components=2, random_state=42)
X_embedded = tsne.fit_transform(X)

tsne_df = pd.DataFrame(X_embedded, columns=['Feature 1', 'Feature 2'])
tsne_df['Target'] = y

plt.figure(figsize=(10, 8))

sns.scatterplot(data=tsne_df, x='Feature 1', y='Feature 2', hue='Target', palette='Set1')

plt.title('Distribution of Training Dataset')
plt.xlabel('Feature 1')
plt.ylabel('Feature 2')

plt.show()