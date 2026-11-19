"""
Orchestration Script: End-to-End Data Science Pipeline
Course: Foundations of Data Science (25ITC31)
Author: Jaswanth surya S (Reg No: 732925ITR038 | Class: 25IT3A)
"""

import sys
import os

from db_extractor import extract_datasets
from data_preprocessing import preprocess_academic_data
from visualizer import generate_visualizations

def main():
    print("=" * 80)
    print("       VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY, ERODE - 12")
    print("                 DEPARTMENT OF INFORMATION TECHNOLOGY")
    print("      SCENARIO 8: STUDENT ACADEMIC PERFORMANCE & RESULT ANALYSIS SYSTEM")
    print("                   DATA SCIENCE & VISUALIZATION PIPELINE")
    print("       Student: Jaswanth surya S | Reg No: 732925ITR038 | Class: 25IT3A")
    print("=" * 80)

    print("\n>>> STEP 1: EXTRACTING DATA FROM SQLITE DATABASE...")
    datasets = extract_datasets(export_csv=True)

    print("\n>>> STEP 2: PREPROCESSING, CLEANING & FEATURE ENGINEERING...")
    preprocessed = preprocess_academic_data(datasets)

    print("\n>>> STEP 3: GENERATING STATISTICAL VISUALIZATIONS...")
    charts = generate_visualizations(preprocessed)

    print("\n" + "=" * 80)
    print(f" PIPELINE EXECUTION COMPLETED SUCCESSFULLY!")
    print(f" Total Charts Created: {len(charts)}")
    print(f" Saved Charts Location: {os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'visualizations'))}")
    print("=" * 80)

if __name__ == '__main__':
    main()
