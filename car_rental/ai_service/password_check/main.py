from src.train import train
from src.evaluate import evaluate
from src.clean_file import cleanCsv

if __name__ == "__main__":
    cleanCsv()
    model, X_test, y_test = train()
    evaluate(model, X_test, y_test)
