"""
Database Extraction Module for Student Academic Performance and Result Analysis System
Course: Foundations of Data Science (25ITC31)
Author: Jaswanth surya S (Reg No: 732925ITR038 | Class: 25IT3A)
Institution: Velalar College of Engineering and Technology, Erode - 12
"""

import sqlite3
import os
import pandas as pd

def get_database_connection():
    candidate_paths = [
        os.path.join(os.path.dirname(__file__), '..', 'database', 'student_performance.db'),
        'd:/project/database/student_performance.db',
        'database/student_performance.db'
    ]
    for path in candidate_paths:
        abs_path = os.path.abspath(path)
        if os.path.exists(abs_path):
            return sqlite3.connect(abs_path)
    raise FileNotFoundError("Could not locate 'student_performance.db'. Please run database initialization first.")

def extract_datasets(export_csv=True):
    """
    Extracts raw database tables and consolidated views into Pandas DataFrames.
    """
    conn = get_database_connection()
    try:
        # 1. Detailed marks dataset with student and subject info
        query_marks_detail = """
        SELECT 
            s.register_no,
            s.name AS student_name,
            s.section,
            s.year_of_study,
            d.dept_code,
            d.dept_name,
            sub.subject_code,
            sub.subject_name,
            sub.credits,
            m.internal_marks,
            m.external_marks,
            m.total_marks,
            m.grade,
            m.grade_point,
            m.status
        FROM marks m
        JOIN students s ON m.register_no = s.register_no
        JOIN subjects sub ON m.subject_code = sub.subject_code
        JOIN departments d ON s.dept_id = d.dept_id;
        """
        df_marks_detail = pd.read_sql_query(query_marks_detail, conn)

        # 2. Student semester summary dataset
        query_semester_summary = "SELECT * FROM v_student_semester_summary;"
        df_summary = pd.read_sql_query(query_semester_summary, conn)

        # 3. Subject-wise performance dataset
        query_subject_perf = "SELECT * FROM v_subject_performance;"
        df_subject_perf = pd.read_sql_query(query_subject_perf, conn)

        # 4. Department-wise summary dataset
        query_dept_summary = "SELECT * FROM v_department_summary;"
        df_dept_summary = pd.read_sql_query(query_dept_summary, conn)

        print("[✓] Extracted 4 DataFrames from Database:")
        print(f"    - Detailed Marks Records: {len(df_marks_detail)} rows")
        print(f"    - Student Summaries:      {len(df_summary)} rows")
        print(f"    - Subject Performance:    {len(df_subject_perf)} rows")
        print(f"    - Department Summaries:   {len(df_dept_summary)} rows")

        if export_csv:
            data_dir = os.path.join(os.path.dirname(__file__), 'data')
            os.makedirs(data_dir, exist_ok=True)
            df_marks_detail.to_csv(os.path.join(data_dir, 'raw_marks_detail.csv'), index=False)
            df_summary.to_csv(os.path.join(data_dir, 'student_semester_summary.csv'), index=False)
            df_subject_perf.to_csv(os.path.join(data_dir, 'subject_performance.csv'), index=False)
            df_dept_summary.to_csv(os.path.join(data_dir, 'department_summary.csv'), index=False)
            print(f"[✓] Exported CSV files to: {os.path.abspath(data_dir)}")

        return {
            'marks_detail': df_marks_detail,
            'student_summary': df_summary,
            'subject_performance': df_subject_perf,
            'department_summary': df_dept_summary
        }
    finally:
        conn.close()

if __name__ == '__main__':
    extract_datasets(export_csv=True)
