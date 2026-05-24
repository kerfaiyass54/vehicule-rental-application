import time

from pipeline.train_model import train_model
from pipeline.recommend import recommend
from pipeline.elastic import save_recommendations
from pipeline.get_vehicles import get_all_vehicles


REFRESH_INTERVAL = 20 * 60  # 20 minutes


def process_vehicle(row):

    try:

        # PostgreSQL columns
        vehicle_id = row["idvehicule"]

        car_name = row["name_vehicule"]

        print(
            f"\nGenerating recommendations "
            f"for vehicle: {car_name} "
            f"(ID: {vehicle_id})"
        )

        # Generate recommendations
        recommendations = recommend(
            car_name=car_name,
            top_n=100
        )

        # Save into Elasticsearch
        save_recommendations(
            vehicle_id=vehicle_id,
            recommendations=recommendations
        )

        print(
            f"Successfully saved "
            f"{len(recommendations)} "
            f"recommendations for "
            f"{car_name}"
        )

    except Exception as e:

        # IMPORTANT:
        # never stop the application
        print(
            f"Error processing vehicle "
            f"{row.get('name_vehicule', 'UNKNOWN')} : {e}"
        )

        # continue automatically
        return


def run_pipeline():

    try:

        print("\n===================================")
        print("Starting AI recommendation pipeline")
        print("===================================\n")

        # Train model
        print("Training recommendation model...")

        train_model()

        print("Model training completed.\n")

        # Load vehicles
        print(
            "Loading vehicles from PostgreSQL..."
        )

        vehicles_df = get_all_vehicles()

        print(
            f"Found {len(vehicles_df)} vehicles\n"
        )

        # Process ALL vehicles
        for _, row in vehicles_df.iterrows():

            process_vehicle(row)

        print(
            "\nAll vehicles processed successfully."
        )

    except Exception as e:

        # Global protection
        print(
            f"\nGlobal pipeline error: {e}"
        )

        # Never stop app
        pass


def main():

    print(
        "\nAI Recommendation Service Started"
    )

    print(
        "Pipeline refresh interval: "
        "20 minutes\n"
    )

    # Infinite loop
    while True:

        start_time = time.strftime(
            "%Y-%m-%d %H:%M:%S"
        )

        print(
            f"\nPipeline execution started at "
            f"{start_time}"
        )

        # Run pipeline
        run_pipeline()

        end_time = time.strftime(
            "%Y-%m-%d %H:%M:%S"
        )

        print(
            f"\nPipeline execution finished at "
            f"{end_time}"
        )

        print(
            "\nWaiting 20 minutes before "
            "next refresh...\n"
        )

        # Wait 20 minutes
        time.sleep(REFRESH_INTERVAL)


if __name__ == "__main__":

    main()