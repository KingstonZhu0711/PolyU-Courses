import matplotlib.pyplot as plt
import numpy as np

# Your data
models = ['Model 1', 'Model 2']
cv_scores = [
    [0.77083333, 0.64756944, 0.69444444, 0.89409722, 0.89583333],
    [0.75520833, 0.734375, 0.68576389, 0.8125, 0.84548611]
]
mean_scores = [0.7806, 0.7667]
std_devs = [0.2027, 0.1133]

# Create the plot
plt.figure(figsize=(10, 6))

# Plot individual CV scores
positions = np.array([1, 2])
for i, (model, scores) in enumerate(zip(models, cv_scores)):
    # Jitter the points slightly for better visibility
    jitter = np.random.normal(0, 0.02, len(scores))
    plt.scatter(np.full(len(scores), positions[i]) + jitter, scores, 
                alpha=0.7, label=f'{model} CV scores', s=60)

# Plot mean scores with error bars
plt.errorbar(positions, mean_scores, yerr=std_devs, fmt='o', 
             markersize=8, capsize=5, capthick=2, 
             label='Mean ± Std Dev', linewidth=2)

# Customize the plot
plt.xlabel('Models', fontsize=12)
plt.ylabel('AUC Score', fontsize=12)
plt.title('Model Comparison: Cross-Validation AUC Scores', fontsize=14, fontweight='bold')
plt.xticks(positions, models, fontsize=11)
plt.ylim(0.5, 1.0)  # AUC typically ranges from 0.5 to 1.0
plt.grid(True, alpha=0.3)
plt.legend()

# Add value annotations for mean scores
for i, (mean, std) in enumerate(zip(mean_scores, std_devs)):
    plt.annotate(f'{mean:.3f} ± {std:.3f}', 
                xy=(positions[i], mean), 
                xytext=(positions[i], mean + 0.05),
                ha='center', va='bottom', fontweight='bold')

plt.tight_layout()
plt.show()

# Optional: Print summary statistics
print("Model Performance Summary:")
print("=" * 40)
for i, model in enumerate(models):
    print(f"{model}:")
    print(f"  CV Scores: {[f'{score:.4f}' for score in cv_scores[i]]}")
    print(f"  Mean AUC: {mean_scores[i]:.4f} (±{std_devs[i]:.4f})")
    print(f"  Score Range: {min(cv_scores[i]):.4f} - {max(cv_scores[i]):.4f}")
    print()