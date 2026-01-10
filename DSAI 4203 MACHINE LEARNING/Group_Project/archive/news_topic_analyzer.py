import pandas as pd
import plotly.graph_objects as go
import os
import webbrowser
import time

# === UPDATE THIS PATH IF YOUR FILE IS IN A DIFFERENT LOCATION ===
CSV_PATH = r"C:\Users\Zhu Jin Shun\Desktop\DSAI 4203 MACHINE LEARNING\Group_Project\archive\abcnews-date-text.csv"

if not os.path.exists(CSV_PATH):
    print("CSV file not found! Please check the path below:")
    print("Looking for:", CSV_PATH)
    print("\nTip: Right-click your csv file → Properties → Copy the full path")
    exit()

print("Loading ABC news dataset...")
df = pd.read_csv(CSV_PATH)
df['publish_date'] = pd.to_datetime(df['publish_date'], format='%Y%m%d')
df = df.sort_values('publish_date').reset_index(drop=True)
df['year_month'] = df['publish_date'].dt.to_period('M').astype(str)
print(f"Successfully loaded {len(df):,} headlines!")

# === Smart keyword labeling (same as original) ===
def label_headline(text):
    text = text.lower()
    labels = []
    rules = {
        "bushfires & disasters": ["fire", "bushfire", "blaze", "wildfire", "burn"],
        "animal cruelty": ["maul", "cruelty", "shark attack", "crocodile", "koala", "kangaroo", "dog attack"],
        "covid & pandemic": ["covid", "coronavirus", "omicron", "vaccine", "lockdown", "quarantine", "pandemic"],
        "climate & environment": ["climate", "warming", "drought", "carbon", "emissions", "greenhouse", "reef"],
        "crime & violence": ["murder", "stab", "shot", "kill", "robbery", "assault", "police raid"],
        "floods & storms": ["flood", "storm", "deluge", "cyclone", "rain bomb"],
        "politics": ["howard", "rudd", "gillard", "abbott", "turnbull", "morrison", "alp", "liberal", "labor", "election"],
        "sport": ["afl", "nrl", "cricket", "warne", "ashes", "olympics", "tennis"],
        "indigenous": ["indigenous", "aboriginal", "stolen generation", "reconciliation"]
    }
    for topic, keywords in rules.items():
        if any(k in text for k in keywords):
            labels.append(topic)
    return labels if labels else ["other"]

print("Analyzing topics in headlines...")
df['topics'] = df['headline_text'].apply(label_headline)

# Explode for counting
exploded = df.explode('topics')
monthly = exploded.groupby(['year_month', 'topics']).size().unstack(fill_value=0)
percentage = monthly.div(monthly.sum(axis=1), axis=0) * 100

# === Create beautiful chart ===
topics_to_show = [
    "bushfires & disasters", "covid & pandemic", "animal cruelty",
    "climate & environment", "crime & violence", "floods & storms", "politics"
]
colors = ["red", "purple", "orange", "green", "darkblue", "cyan", "brown"]

fig = go.Figure()
for i, topic in enumerate(topics_to_show):
    if topic in percentage.columns:
        fig.add_trace(go.Scatter(
            x=percentage.index,
            y=percentage[topic],
            mode='lines',
            name=topic,
            line=dict(color=colors[i], width=3)
        ))

fig.update_layout(
    title="<b>Australian News Topics Over Time (2003–2021)</b>",
    title_font_size=20,
    xaxis_title="Year-Month",
    yaxis_title="Percentage of Headlines (%)",
    hovermode="x unified",
    template="plotly_white",
    height=700,
    legend_title="Topic",
    xaxis=dict(tickangle=45, tickfont_size=10),
    yaxis=dict(gridcolor='lightgray')
)

# === SAVE AS HIGH-QUALITY PNG + POP UP ===
png_file = "Australian_News_Topics_2003_2021.png"

print("Saving high-quality PNG image...")
fig.write_image(png_file, width=1600, height=800, scale=3)  # scale=3 = ultra sharp!

print("Opening the interactive graph in your browser...")
fig.show()  # This opens interactive version in browser

print("Opening the PNG image now...")
time.sleep(1)  # Small delay so browser opens first
webbrowser.open(os.path.abspath(png_file))

# === Final success message ===
print("\n" + "="*60)
print("SUCCESS! Everything done!")
print(f"   Interactive graph → opened in browser")
print(f"   High-quality PNG  → saved & popped up as:")
print(f"   {os.path.abspath(png_file)}")
print("="*60)

# === Quick predictor (bonus) ===
def predict(headline):
    labels = label_headline(headline)
    main = [t for t in labels if t != "other"]
    print(f"\nHeadline: {headline}")
    print("Detected →", " | ".join(main) if main else "No major topic (other)")

# Test predictions
predict("bushfires destroy thousands of homes in nsw")
predict("omicron cases surge in sydney")
predict("dog mauls toddler in backyard")
predict("kevin rudd apologises to stolen generations")