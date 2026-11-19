"""
Data Preprocessing & Wrangling Module
Course: Foundations of Data Science (25ITC31 - Unit 3 & Unit 4)
Techniques: Data Cleaning, Outlier Treatment, Feature Engineering, Aggregation & Pivot Tables
"""

import os
import pandas as pd
import numpy as np
from db_extractor import extract_datasets

def preprocess_academic_data(datasets=None):
    if datasets is None:
        datasets = extract_datasets(export_csv=False)

    df_marks = datasets['marks_detail'].copy()
    df_students = datasets['student_summary'].copy()

    print("\n--- 1. DATA QUALITY & INTEGRITY CHECKS ---")
    # Check for missing values
    missing_count = df_marks.isnull().sum().sum()
    print(f"Missing values count in marks dataset: {missing_count}")

    # Check for duplicate rows
    duplicate_count = df_marks.duplicated().sum()
    print(f"Duplicate records count: {duplicate_count}")

    print("\n--- 2. DESCRIPTIVE SUMMARY STATISTICS ---")
    stats = df_marks[['internal_marks', 'external_marks', 'total_marks']].describe()
    print(stats)

    print("\n--- 3. OUTLIER DETECTION (INTERQUARTILE RANGE - IQR) ---")
    q1 = df_marks['total_marks'].quantile(0.25)
    q3 = df_marks['total_marks'].quantile(0.75)
    iqr = q3 - q1
    lower_bound = q1 - 1.5 * iqr
    upper_bound = q3 + 1.5 * iqr
    outliers = df_marks[(df_marks['total_marks'] < lower_bound) | (df_marks['total_marks'] > upper_bound)]
    print(f"IQR: {iqr:.2f} | Normal Range: [{lower_bound:.2f}, {upper_bound:.2f}]")
    print(f"Detected Outliers count: {len(outliers)}")

    print("\n--- 4. FEATURE ENGINEERING: ACADEMIC CLASSIFICATION ---")
    # Categorize performance tier based on GPA and overall result
    def classify_performance(row):
        if row['overall_result'] == 'FAIL' or row['subjects_failed'] > 0:
            return 'Remedial / Arrear'
        elif row['gpa'] >= 8.5:
            return 'Distinction'
        elif row['gpa'] >= 7.0:
            return 'First Class'
        else:
            return 'Second Class'

    df_students['performance_tier'] = df_students.apply(classify_performance, axis=1)
    tier_distribution = df_students['performance_tier'].value_counts()
    print("Performance Tier Distribution:")
    for tier, count in tier_distribution.items():
        print(f"  - {tier}: {count} students ({count/len(df_students)*100:.1f}%)")

    print("\n--- 5. PIVOT TABLE: STUDENT MARKS MATRIX ---")
    # Create pivot table of students vs subjects total marks
    marks_pivot = df_marks.pivot_table(
        index=['register_no', 'student_name', 'section'],
        columns='subject_code',
        values='total_marks',
        fill_value=0
    ).reset_index()

    # Save cleaned and transformed datasets
    data_dir = os.path.join(os.path.dirname(__file__), 'data')
    os.makedirs(data_dir, exist_ok=True)
    df_students.to_csv(os.path.join(data_dir, 'students_preprocessed.csv'), index=False)
    marks_pivot.to_csv(os.path.join(data_dir, 'marks_pivot_matrix.csv'), index=False)
    print(f"[✓] Preprocessed data saved successfully in {os.path.abspath(data_dir)}")

    return {
        'clean_marks': df_marks,
        'clean_students': df_students,
        'marks_pivot': marks_pivot,
        'subject_perf': datasets['subject_performance'],
        'dept_summary': datasets['department_summary']
    }

if __name__ == '__main__':
    preprocess_academic_data()
