from pipeline.train_model import train_model
from pipeline.recommend import recommend
from pipeline.elastic import save_recommendations
import pickle


# train model
train_model()

# load all vehicles
df = pickle.load(
    open("./data/vehicles.pkl", "rb")
)

# loop through all vehicles
for _, row in df.iterrows():

    vehicle_id = row.name

    car_name = row["car_name"]

    try:

        recommendations = recommend(
            car_name,
            top_n=5
        )

        save_recommendations(
            vehicle_id=vehicle_id,
            recommendations=recommendations
        )

        print(
            f"Saved recommendations for {car_name}"
        )

    except Exception as e:

        print(
            f"Error for {car_name}: {e}"
        )