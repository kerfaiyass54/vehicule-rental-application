import pickle

from pipeline.config import (
    MODEL_PATH,
    VEHICLES_PATH
)


def recommend(car_name, top_n=100):

    # load similarity matrix
    with open(MODEL_PATH, "rb") as f:
        similarity = pickle.load(f)

    # load vehicles dataframe
    df = pickle.load(
        open(VEHICLES_PATH, "rb")
    )

    car_name = str(car_name).lower().strip()

    matches = df[
        df["car_name"]
        .str.lower()
        .str.contains(car_name)
    ]

    if matches.empty:

        return []

    idx = matches.index[0]

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
            "price": float(vehicle["price"]),
            "horsepower": float(vehicle["horsepower"]),
            "top_speed": float(vehicle["top_speed"]),
            "acceleration_0_100": float(
                vehicle["acceleration_0_100"]
            ),
            "fuel_type": vehicle["fuel_type"],
            "torque": float(vehicle["torque"])

        })

    return recommendations