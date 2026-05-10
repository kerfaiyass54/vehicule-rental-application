import pickle

from pipeline.config import (
    MODEL_PATH,
    VEHICLES_PATH
)


# load similarity matrix
with open(MODEL_PATH, "rb") as f:

    similarity = pickle.load(f)


# load original vehicles dataframe
df = pickle.load(
    open(VEHICLES_PATH, "rb")
)


def recommend(car_name, top_n=5):

    car_name = car_name.lower()

    idx = df[
        df["car_name"].str.lower() == car_name
    ].index[0]

    distances = similarity[idx]

    vehicles = sorted(
        list(enumerate(distances)),
        reverse=True,
        key=lambda x: x[1]
    )[1:top_n + 1]

    recommendations = []

    for i in vehicles:

        vehicle = df.iloc[i[0]]

        recommendations.append({

            "car_name": vehicle["car_name"],
            "brand": vehicle["brand"],
            "price": vehicle["price"],
            "horsepower": vehicle["horsepower"],
            "top_speed": vehicle["top_speed"],
            "acceleration_0_100": vehicle[
                "acceleration_0_100"
            ],
            "fuel_type": vehicle["fuel_type"],
            "torque": vehicle["torque"]

        })

    return recommendations