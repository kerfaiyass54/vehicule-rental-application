# Vehicle Sender

This service uses a local normalized subset of Kaggle's **Car Features and MSRP**
dataset as its semantic reference data. The full dataset can be obtained from:
https://www.kaggle.com/datasets/CooperUnion/cardataset

The request pipeline extracts constraints, applies TF-IDF semantic similarity,
then reranks by budget and vehicle attributes. It returns fields compatible with
the application's `Vehicule` entity.
