import pandas as pd
import matplotlib.pyplot as plt

# ---------------------- Step 1: Prepare the data ----------------------
data = [
    {"Feature": 217, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 117, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 199, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 194, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 80, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 73, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 65, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 91, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 33, "Times_Selected": 3, "Avg_CV_AUC": 0.883449},
    {"Feature": 108, "Times_Selected": 2, "Avg_CV_AUC": 0.927257},
    {"Feature": 82, "Times_Selected": 2, "Avg_CV_AUC": 0.927257},
    {"Feature": 69, "Times_Selected": 2, "Avg_CV_AUC": 0.927257},
    {"Feature": 43, "Times_Selected": 2, "Avg_CV_AUC": 0.927257},
    {"Feature": 46, "Times_Selected": 2, "Avg_CV_AUC": 0.927257},
    {"Feature": 295, "Times_Selected": 2, "Avg_CV_AUC": 0.647917}
]

# Create DataFrame and sort features by Avg_CV_AUC (for better readability)
df = pd.DataFrame(data)
df_sorted = df.sort_values(by="Avg_CV_AUC", ascending=True)  # Sort low→high for horizontal bars


# ---------------------- Step 2: Create the visualization ----------------------
plt.figure(figsize=(12, 8))  # Set chart size (width, height)

# Define colors: blue for features selected 3 times, orange for 2 times
colors = ["#1f77b4" if ts == 3 else "#ff7f0e" for ts in df_sorted["Times_Selected"]]

# Plot horizontal bars (y = Feature IDs, x = Avg_CV_AUC)
bars = plt.barh(
    y=df_sorted["Feature"],  # Y-axis: Feature IDs
    width=df_sorted["Avg_CV_AUC"],  # X-axis: Avg_CV_AUC values
    color=colors,
    edgecolor="black",  # Add black borders to bars for clarity
    linewidth=0.5
)

# ---------------------- Step 3: Customize labels and style ----------------------
# Axis labels and title
plt.xlabel("Average CV AUC (Model Performance)", fontsize=12, fontweight="bold")
plt.ylabel("Feature ID", fontsize=12, fontweight="bold")
plt.title(
    "Feature Selection Frequency vs. Average CV AUC\n(Across Model Training)",
    fontsize=14, fontweight="bold", pad=20
)

# Adjust X-axis range to focus on relevant values (0.6 to 1.0)
plt.xlim(0.6, 1.0)

# Add grid lines (X-axis only) for easier value reading
plt.grid(axis="x", linestyle="--", alpha=0.6)

# Add value labels on top of each bar (show exact Avg_CV_AUC)
for bar in bars:
    width = bar.get_width()  # Get the AUC value of the bar
    # Add text slightly to the right of the bar's end
    plt.text(
        width + 0.005,  # X-position (offset to avoid overlapping)
        bar.get_y() + bar.get_height()/2,  # Y-position (center of the bar)
        f"{width:.4f}",  # Format to 4 decimal places
        ha="left", va="center", fontsize=9
    )

# Add a legend to explain color coding
from matplotlib.patches import Patch
legend_elements = [
    Patch(facecolor="#1f77b4", label="Selected 3 Times"),
    Patch(facecolor="#ff7f0e", label="Selected 2 Times")
]
plt.legend(handles=legend_elements, loc="lower right", fontsize=10)

# Adjust layout to prevent label cutoff
plt.tight_layout()

# Show the plot
plt.show()