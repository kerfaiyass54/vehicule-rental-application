import pandas as pd

# Input variables
GITHUB_USERNAME = "josephnehrenz"
REPO_NAME = "car_service_data"
BRANCH = "main"

# Load data directly from GitHub
base_url = f"https://raw.githubusercontent.com/{GITHUB_USERNAME}/{REPO_NAME}/refs/heads/{BRANCH}/data/"

service_df = pd.read_csv(f"{base_url}service_records.csv")
line_df = pd.read_csv(f"{base_url}line_items.csv")
subitem_df = pd.read_csv(f"{base_url}line_subitems.csv")

print("Data loaded successfully!")
print(f"Total services: {len(service_df)}")
print(f"Total spent: ${service_df['total_payment_due'].sum():,.2f}")
print(f"Average cost per service: ${service_df['total_payment_due'].mean():,.2f}")

# Quick visualization (optional)
import matplotlib.pyplot as plt
service_df['visit_date'] = pd.to_datetime(service_df['visit_date'])
service_df.plot(x='visit_date', y='total_payment_due', kind='line', title='Service Costs Over Time')
plt.show()
