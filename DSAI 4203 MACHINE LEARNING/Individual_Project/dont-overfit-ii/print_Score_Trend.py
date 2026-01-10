import matplotlib.pyplot as plt

# Data preparation
attempts = [1, 2, 3, 4, 5, 6]  # X-axis: Number of attempts
scores = [0.497, 0.501, 0.749, 0.797, 0.810, 0.815]  # Y-axis: Accuracy/Public score

# Create a figure and axis
plt.figure(figsize=(10, 6))  # Set figure size (width, height)

# Plot the trend line with markers for data points
plt.plot(attempts, scores, 'o-', color='darkblue', markersize=8, linewidth=2, 
         markerfacecolor='orange', markeredgewidth=2)  # 'o-' = circles + solid line

# Customize axis labels and title
plt.xlabel('Number of Attempts', fontsize=12, fontweight='bold')
plt.ylabel('Accuracy/Public Score', fontsize=12, fontweight='bold')
plt.title('Trend of Private Score vs. Number of Attempts (1-6)', 
          fontsize=14, fontweight='bold', pad=20)

# Adjust axis ranges for better visibility
plt.xlim(0.5, 6.5)  # Slight padding around x-values
plt.ylim(0.4, 0.85)  # Focus on the relevant score range

# Add grid lines for readability
plt.grid(True, linestyle='--', alpha=0.6)  # Dashed grid with transparency

# Annotate data points with their exact values
for x, y in zip(attempts, scores):
    plt.text(x, y + 0.01, f'{y:.3f}',  # Position text slightly above the point
             ha='center', fontsize=10, fontweight='medium')

# Display the plot
plt.tight_layout()  # Adjust spacing
plt.show()