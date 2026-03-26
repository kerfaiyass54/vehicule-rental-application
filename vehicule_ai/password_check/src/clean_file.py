import csv
from src.config import DATA_PATH, DATA_PATH_CLEAN

def cleanCsv():
    input_file = DATA_PATH_CLEAN
    output_file = DATA_PATH

    with open(input_file, "r", newline="", encoding="utf-8") as infile, \
        open(output_file, "w", newline="", encoding="utf-8") as outfile:
        reader = csv.reader(infile)
        writer = csv.writer(outfile)
    
        header = next(reader)
        writer.writerow(header)
    
        expected_columns = len(header)
    
        for row in reader:
            if len(row) == expected_columns:
                writer.writerow(row)



