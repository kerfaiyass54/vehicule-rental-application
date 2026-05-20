from pipeline.train_model import train_model
from pipeline.recommend import recommend
from pipeline.elastic import save_recommendations
from pipeline.get_vehicles import get_all_vehicles


def main():

    print("Training model...")

    # train recommendation model
    train_model()

    print(
        "Loading vehicles from PostgreSQL..."
    )

    vehicles_df = get_all_vehicles()

    print(
        f"Found {len(vehicles_df)} vehicles"
    )

    for _, row in vehicles_df.iterrows():

        try:

            # PostgreSQL columns
            vehicle_id = row["idvehicule"]

            car_name = row[
                "name_vehicule"
            ]

            print(
                f"Generating recommendations "
                f"for {car_name}"
            )

            recommendations = recommend(
                car_name=car_name,
                top_n=100
            )

            save_recommendations(
                vehicle_id=vehicle_id,
                recommendations=recommendations
            )

            print(
                f"Saved "
                f"{len(recommendations)} "
                f"recommendations "
                f"for {car_name}"
            )

        except Exception as e:

            print(
                f"Error for vehicle "
                f"{car_name}: {e}"
            )


if __name__ == "__main__":

    main()