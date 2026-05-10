import pickle

from sklearn.metrics.pairwise import cosine_similarity

from pipeline.features_engineering import (
    features_engineering
)

from pipeline.config import (
    MODEL_PATH,
    VEHICLES_PATH
)


def train_model():

    # original + processed
    original_df, features_df = (
        features_engineering()
    )

    # cosine similarity matrix
    similarity = cosine_similarity(
        features_df
    )

    # save similarity matrix
    with open(MODEL_PATH, "wb") as f:

        pickle.dump(similarity, f)

    # save original dataframe
    original_df.to_pickle(
        VEHICLES_PATH
    )

    print("Model trained successfully")


if __name__ == "__main__":

    train_model()