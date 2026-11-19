"""
Flask Web Application: Student Academic Performance and Result Analysis System
Institution: Velalar College of Engineering and Technology, Erode - 12
Department: Information Technology | Class: 25IT3A | Scenario: 8
Developer: Jaswanth surya S (Reg No: 732925ITR038)
"""

import os
import sys
import sqlite3
import subprocess
import json
import pandas as pd
import numpy as np
from flask import Flask, render_template, request, jsonify, send_from_directory

import shutil

# Path configurations
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
DB_PATH = os.path.join(BASE_DIR, 'database', 'student_performance.db')
JAVA_DIR = os.path.join(BASE_DIR, 'java_app')
VIS_DIR = os.path.join(BASE_DIR, 'visualizations')
DOCS_DIR = os.path.join(BASE_DIR, 'docs')

def get_java_executable():
    candidates = [
        shutil.which('java'),
        r"C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot\bin\java.exe",
        r"C:\Program Files\Java\jdk-21\bin\java.exe"
    ]
    for c in candidates:
        if c and os.path.exists(c):
            return c
    return "java"


app = Flask(
    __name__,
    template_folder=os.path.join(os.path.dirname(__file__), 'templates'),
    static_folder=os.path.join(os.path.dirname(__file__), 'static')
)
app.config['SECRET_KEY'] = 'vcet-it-scenario8-jaswanthsurya'

def get_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON;")
    return conn

# -------------------------------------------------------------
# FRONTEND TEMPLATE ROUTES
# -------------------------------------------------------------

@app.route('/')
def dashboard():
    return render_template('dashboard.html', active_page='dashboard')

@app.route('/students')
def students_page():
    return render_template('students.html', active_page='students')

@app.route('/subjects')
def subjects_page():
    return render_template('subjects.html', active_page='subjects')

@app.route('/marks')
def marks_page():
    return render_template('marks.html', active_page='marks')

@app.route('/results')
def results_page():
    return render_template('results.html', active_page='results')

@app.route('/analytics')
def analytics_page():
    return render_template('analytics.html', active_page='analytics')

@app.route('/database')
def database_page():
    return render_template('database.html', active_page='database')

@app.route('/java-module')
def java_module_page():
    return render_template('java_module.html', active_page='java-module')

@app.route('/software-engineering')
def se_docs_page():
    return render_template('se_docs.html', active_page='se-docs')

@app.route('/subject-coverage')
def subject_coverage_page():
    return render_template('subject_coverage.html', active_page='subject-coverage')

@app.route('/visualizations/<path:filename>')
def serve_visualization(filename):
    return send_from_directory(VIS_DIR, filename)

# -------------------------------------------------------------
# API: DASHBOARD KPIS & CHARTS
# -------------------------------------------------------------

@app.route('/api/dashboard/stats')
def api_dashboard_stats():
    conn = get_db()
    try:
        cur = conn.cursor()
        
        # Core KPIs
        cur.execute("SELECT COUNT(*) FROM students;")
        total_students = cur.fetchone()[0]

        cur.execute("SELECT COUNT(*) FROM subjects;")
        total_subjects = cur.fetchone()[0]

        cur.execute("SELECT COUNT(*) FROM departments;")
        total_depts = cur.fetchone()[0]

        cur.execute("SELECT COUNT(*) FROM marks;")
        total_marks_records = cur.fetchone()[0]

        cur.execute("SELECT ROUND(AVG(total_marks), 2) FROM marks;")
        overall_avg = cur.fetchone()[0] or 0.0

        cur.execute("""
            SELECT 
                ROUND((CAST(SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS REAL) / COUNT(*)) * 100, 2)
            FROM marks;
        """)
        overall_pass_pct = cur.fetchone()[0] or 0.0

        # Top Performer
        cur.execute("""
            SELECT register_no, student_name, gpa, grand_total, section, dept_code
            FROM v_student_semester_summary
            WHERE overall_result = 'PASS'
            ORDER BY gpa DESC, grand_total DESC
            LIMIT 5;
        """)
        top_performers = [dict(r) for r in cur.fetchall()]

        # Subject-wise pass rates
        cur.execute("SELECT subject_code, subject_name, pass_percentage, avg_marks, total_appeared FROM v_subject_performance ORDER BY pass_percentage ASC;")
        subject_perf = [dict(r) for r in cur.fetchall()]

        # Grade distribution
        cur.execute("""
            SELECT grade, COUNT(*) as count 
            FROM marks 
            GROUP BY grade 
            ORDER BY CASE grade 
                WHEN 'O' THEN 1 WHEN 'A+' THEN 2 WHEN 'A' THEN 3 
                WHEN 'B+' THEN 4 WHEN 'B' THEN 5 ELSE 6 END;
        """)
        grade_dist = [dict(r) for r in cur.fetchall()]

        # Section-wise pass rate
        cur.execute("""
            SELECT s.section, 
                   COUNT(DISTINCT s.register_no) as student_count,
                   ROUND(AVG(m.total_marks), 2) as avg_marks,
                   ROUND((CAST(SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS REAL) / COUNT(m.mark_id)) * 100, 2) as pass_percentage
            FROM students s
            JOIN marks m ON s.register_no = m.register_no
            WHERE s.dept_id = 1
            GROUP BY s.section;
        """)
        section_perf = [dict(r) for r in cur.fetchall()]

        return jsonify({
            'total_students': total_students,
            'total_subjects': total_subjects,
            'total_departments': total_depts,
            'total_evaluations': total_marks_records,
            'overall_avg_marks': overall_avg,
            'overall_pass_pct': overall_pass_pct,
            'top_performers': top_performers,
            'subject_performance': subject_perf,
            'grade_distribution': grade_dist,
            'section_performance': section_perf
        })
    finally:
        conn.close()

# -------------------------------------------------------------
# API: STUDENT MANAGEMENT (CRUD)
# -------------------------------------------------------------

@app.route('/api/students', methods=['GET', 'POST'])
def api_students():
    conn = get_db()
    try:
        cur = conn.cursor()
        if request.method == 'POST':
            data = request.json or {}
            reg_no = data.get('register_no', '').strip()
            name = data.get('name', '').strip()
            dept_id = int(data.get('dept_id', 1))
            year = int(data.get('year_of_study', 2))
            section = data.get('section', '').strip()
            email = data.get('email', '').strip()
            phone = data.get('phone', '').strip()

            if not reg_no or not name:
                return jsonify({'success': False, 'message': 'Register number and name are required.'}), 400

            # Check duplicate
            cur.execute("SELECT register_no FROM students WHERE register_no = ?;", (reg_no,))
            if cur.fetchone():
                return jsonify({'success': False, 'message': f'Student with Register No {reg_no} already exists.'}), 409

            cur.execute("""
                INSERT INTO students (register_no, name, dept_id, year_of_study, section, email, phone)
                VALUES (?, ?, ?, ?, ?, ?, ?);
            """, (reg_no, name, dept_id, year, section, email, phone))
            conn.commit()
            return jsonify({'success': True, 'message': f'Student {name} registered successfully!'}), 201

        # GET with search & filter
        dept_filter = request.args.get('dept_id')
        search = request.args.get('search', '').strip()

        query = """
            SELECT s.*, d.dept_code, d.dept_name 
            FROM students s 
            JOIN departments d ON s.dept_id = d.dept_id 
            WHERE 1=1
        """
        params = []
        if dept_filter:
            query += " AND s.dept_id = ?"
            params.append(dept_filter)
        if search:
            query += " AND (s.register_no LIKE ? OR s.name LIKE ? OR s.section LIKE ?)"
            s_param = f"%{search}%"
            params.extend([s_param, s_param, s_param])

        query += " ORDER BY s.register_no ASC;"
        cur.execute(query, params)
        students = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'count': len(students), 'students': students})
    finally:
        conn.close()

@app.route('/api/students/<reg_no>', methods=['GET', 'PUT', 'DELETE'])
def api_student_detail(reg_no):
    conn = get_db()
    try:
        cur = conn.cursor()
        if request.method == 'GET':
            cur.execute("""
                SELECT s.*, d.dept_code, d.dept_name 
                FROM students s 
                JOIN departments d ON s.dept_id = d.dept_id 
                WHERE s.register_no = ?;
            """, (reg_no,))
            row = cur.fetchone()
            if not row:
                return jsonify({'success': False, 'message': 'Student not found'}), 404
            return jsonify({'success': True, 'student': dict(row)})

        elif request.method == 'PUT':
            data = request.json or {}
            name = data.get('name', '').strip()
            dept_id = int(data.get('dept_id', 1))
            year = int(data.get('year_of_study', 2))
            section = data.get('section', '').strip()
            email = data.get('email', '').strip()
            phone = data.get('phone', '').strip()

            cur.execute("""
                UPDATE students 
                SET name = ?, dept_id = ?, year_of_study = ?, section = ?, email = ?, phone = ?
                WHERE register_no = ?;
            """, (name, dept_id, year, section, email, phone, reg_no))
            conn.commit()
            return jsonify({'success': True, 'message': 'Student details updated successfully!'})

        elif request.method == 'DELETE':
            cur.execute("DELETE FROM students WHERE register_no = ?;", (reg_no,))
            conn.commit()
            return jsonify({'success': True, 'message': f'Student {reg_no} and associated marks removed.'})
    finally:
        conn.close()

# -------------------------------------------------------------
# API: SUBJECT MANAGEMENT (CRUD)
# -------------------------------------------------------------

@app.route('/api/subjects', methods=['GET', 'POST'])
def api_subjects():
    conn = get_db()
    try:
        cur = conn.cursor()
        if request.method == 'POST':
            data = request.json or {}
            code = data.get('subject_code', '').strip()
            name = data.get('subject_name', '').strip()
            dept_id = int(data.get('dept_id', 1))
            semester = int(data.get('semester', 3))
            credits = int(data.get('credits', 3))
            max_int = float(data.get('max_internal', 40.0))
            max_ext = float(data.get('max_external', 60.0))

            if not code or not name:
                return jsonify({'success': False, 'message': 'Subject code and name are required.'}), 400

            cur.execute("""
                INSERT INTO subjects (subject_code, subject_name, dept_id, semester, credits, max_internal, max_external)
                VALUES (?, ?, ?, ?, ?, ?, ?);
            """, (code, name, dept_id, semester, credits, max_int, max_ext))
            conn.commit()
            return jsonify({'success': True, 'message': f'Subject {code} added successfully!'}), 201

        cur.execute("""
            SELECT sub.*, d.dept_code, d.dept_name,
                   (sub.max_internal + sub.max_external) as max_total,
                   (SELECT COUNT(*) FROM marks WHERE subject_code = sub.subject_code) as enrolled_count
            FROM subjects sub
            JOIN departments d ON sub.dept_id = d.dept_id
            ORDER BY sub.subject_code ASC;
        """)
        subjects = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'subjects': subjects})
    finally:
        conn.close()

@app.route('/api/subjects/<code>', methods=['PUT', 'DELETE'])
def api_subject_modify(code):
    conn = get_db()
    try:
        cur = conn.cursor()
        if request.method == 'PUT':
            data = request.json or {}
            name = data.get('subject_name', '').strip()
            dept_id = int(data.get('dept_id', 1))
            semester = int(data.get('semester', 3))
            credits = int(data.get('credits', 3))
            cur.execute("""
                UPDATE subjects 
                SET subject_name = ?, dept_id = ?, semester = ?, credits = ?
                WHERE subject_code = ?;
            """, (name, dept_id, semester, credits, code))
            conn.commit()
            return jsonify({'success': True, 'message': f'Subject {code} updated successfully.'})

        elif request.method == 'DELETE':
            cur.execute("DELETE FROM subjects WHERE subject_code = ?;", (code,))
            conn.commit()
            return jsonify({'success': True, 'message': f'Subject {code} removed.'})
    finally:
        conn.close()

# -------------------------------------------------------------
# API: MARKS MANAGEMENT & RECORDING
# -------------------------------------------------------------

def compute_grades(internal, external):
    total = internal + external
    if external < 27.0 or total < 50.0:
        return total, 'RA', 0, 'FAIL'
    elif total >= 90.0:
        return total, 'O', 10, 'PASS'
    elif total >= 80.0:
        return total, 'A+', 9, 'PASS'
    elif total >= 70.0:
        return total, 'A', 8, 'PASS'
    elif total >= 60.0:
        return total, 'B+', 7, 'PASS'
    else:
        return total, 'B', 6, 'PASS'

@app.route('/api/marks', methods=['GET', 'POST'])
def api_marks():
    conn = get_db()
    try:
        cur = conn.cursor()
        if request.method == 'POST':
            data = request.json or {}
            reg_no = data.get('register_no', '').strip()
            subject_code = data.get('subject_code', '').strip()
            try:
                internal = float(data.get('internal_marks', 0))
                external = float(data.get('external_marks', 0))
            except ValueError:
                return jsonify({'success': False, 'message': 'Marks must be valid numeric values.'}), 400

            if internal < 0 or internal > 40:
                return jsonify({'success': False, 'message': 'Internal marks must be between 0 and 40.'}), 400
            if external < 0 or external > 60:
                return jsonify({'success': False, 'message': 'Semester exam marks must be between 0 and 60.'}), 400

            total, grade, grade_point, status = compute_grades(internal, external)

            # Upsert into marks table
            cur.execute("""
                INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(register_no, subject_code) DO UPDATE SET
                    internal_marks = excluded.internal_marks,
                    external_marks = excluded.external_marks,
                    total_marks = excluded.total_marks,
                    grade = excluded.grade,
                    grade_point = excluded.grade_point,
                    status = excluded.status;
            """, (reg_no, subject_code, internal, external, total, grade, grade_point, status))
            conn.commit()

            return jsonify({
                'success': True,
                'message': f'Marks recorded for {reg_no} in {subject_code}.',
                'result': {
                    'total': total,
                    'grade': grade,
                    'grade_point': grade_point,
                    'status': status
                }
            })

        # GET marks
        reg_no = request.args.get('register_no')
        sub_code = request.args.get('subject_code')

        query = """
            SELECT m.*, s.name as student_name, s.section, sub.subject_name, sub.credits
            FROM marks m
            JOIN students s ON m.register_no = s.register_no
            JOIN subjects sub ON m.subject_code = sub.subject_code
            WHERE 1=1
        """
        params = []
        if reg_no:
            query += " AND m.register_no = ?"
            params.append(reg_no)
        if sub_code:
            query += " AND m.subject_code = ?"
            params.append(sub_code)

        query += " ORDER BY m.register_no, m.subject_code;"
        cur.execute(query, params)
        records = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'count': len(records), 'marks': records})
    finally:
        conn.close()

# -------------------------------------------------------------
# API: RESULT ANALYSIS & REPORTS
# -------------------------------------------------------------

@app.route('/api/reports/marksheet/<reg_no>')
def api_report_marksheet(reg_no):
    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute("SELECT * FROM v_student_semester_summary WHERE register_no = ?;", (reg_no,))
        summary = cur.fetchone()
        if not summary:
            return jsonify({'success': False, 'message': 'Student not found in results.'}), 404

        cur.execute("""
            SELECT m.*, sub.subject_name, sub.credits
            FROM marks m
            JOIN subjects sub ON m.subject_code = sub.subject_code
            WHERE m.register_no = ?
            ORDER BY sub.subject_code ASC;
        """, (reg_no,))
        marks = [dict(r) for r in cur.fetchall()]

        return jsonify({
            'success': True,
            'summary': dict(summary),
            'marks': marks
        })
    finally:
        conn.close()

@app.route('/api/reports/ranks')
def api_report_ranks():
    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute("""
            SELECT * FROM v_student_semester_summary 
            WHERE overall_result = 'PASS'
            ORDER BY gpa DESC, grand_total DESC;
        """)
        ranks = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'ranks': ranks})
    finally:
        conn.close()

@app.route('/api/reports/arrears')
def api_report_arrears():
    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute("""
            SELECT * FROM v_student_semester_summary 
            WHERE subjects_failed > 0
            ORDER BY subjects_failed DESC, average_marks ASC;
        """)
        arrears = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'arrears': arrears})
    finally:
        conn.close()

@app.route('/api/reports/subjects')
def api_report_subjects():
    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute("SELECT * FROM v_subject_performance ORDER BY pass_percentage ASC;")
        subjects = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'subjects': subjects})
    finally:
        conn.close()

@app.route('/api/reports/departments')
def api_report_departments():
    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute("SELECT * FROM v_department_summary ORDER BY dept_code ASC;")
        depts = [dict(r) for r in cur.fetchall()]
        return jsonify({'success': True, 'departments': depts})
    finally:
        conn.close()

# -------------------------------------------------------------
# API: DATA SCIENCE ANALYTICS (PANDAS & NUMPY)
# -------------------------------------------------------------

@app.route('/api/ds/summary')
def api_ds_summary():
    conn = get_db()
    try:
        df = pd.read_sql_query("SELECT internal_marks, external_marks, total_marks FROM marks;", conn)
        
        desc = df.describe().to_dict()
        medians = df.median().to_dict()
        variances = df.var().to_dict()
        skews = df.skew().to_dict()

        # Outlier calculation via IQR on total_marks
        q1 = df['total_marks'].quantile(0.25)
        q3 = df['total_marks'].quantile(0.75)
        iqr = q3 - q1
        lower_bound = q1 - 1.5 * iqr
        upper_bound = q3 + 1.5 * iqr

        outliers_count = int(((df['total_marks'] < lower_bound) | (df['total_marks'] > upper_bound)).sum())

        return jsonify({
            'success': True,
            'count': int(len(df)),
            'descriptive_stats': desc,
            'medians': medians,
            'variances': variances,
            'skews': skews,
            'iqr_analysis': {
                'q1': round(float(q1), 2),
                'q3': round(float(q3), 2),
                'iqr': round(float(iqr), 2),
                'lower_bound': round(float(lower_bound), 2),
                'upper_bound': round(float(upper_bound), 2),
                'outliers_count': outliers_count
            }
        })
    finally:
        conn.close()

@app.route('/api/ds/pivot')
def api_ds_pivot():
    conn = get_db()
    try:
        query = """
            SELECT s.register_no, s.name as student_name, s.section, m.subject_code, m.total_marks
            FROM marks m
            JOIN students s ON m.register_no = s.register_no;
        """
        df = pd.read_sql_query(query, conn)
        pivot = df.pivot_table(index=['register_no', 'student_name', 'section'], columns='subject_code', values='total_marks', fill_value=0).reset_index()
        records = pivot.to_dict(orient='records')
        columns = list(pivot.columns)
        return jsonify({'success': True, 'columns': columns, 'matrix': records})
    finally:
        conn.close()

@app.route('/api/ds/rerun', methods=['POST'])
def api_ds_rerun():
    try:
        cmd = [sys.executable, os.path.join(BASE_DIR, 'python_analysis', 'run_analysis.py')]
        result = subprocess.run(cmd, capture_output=True, text=True, cwd=BASE_DIR, check=True)
        return jsonify({
            'success': True,
            'message': 'Python Data Science Pipeline executed successfully!',
            'output': result.stdout
        })
    except subprocess.CalledProcessError as e:
        return jsonify({'success': False, 'error': str(e), 'stderr': e.stderr}), 500

# -------------------------------------------------------------
# API: DATABASE PLAYGROUND & LIVE SQL RUNNER
# -------------------------------------------------------------

@app.route('/api/db/query', methods=['POST'])
def api_db_query():
    sql = (request.json or {}).get('query', '').strip()
    if not sql:
        return jsonify({'success': False, 'message': 'Query cannot be empty.'}), 400

    # Safety check: allow read queries primarily or controlled updates
    forbidden = ['drop database', 'vacuum', 'alter user']
    if any(f in sql.lower() for f in forbidden):
        return jsonify({'success': False, 'message': 'Command restricted in web playground.'}), 403

    conn = get_db()
    try:
        cur = conn.cursor()
        cur.execute(sql)
        if sql.lower().startswith('select') or sql.lower().startswith('pragma'):
            cols = [desc[0] for desc in cur.description] if cur.description else []
            rows = [list(r) for r in cur.fetchall()]
            return jsonify({
                'success': True,
                'columns': cols,
                'rows': rows,
                'row_count': len(rows)
            })
        else:
            conn.commit()
            return jsonify({
                'success': True,
                'message': f'Statement executed successfully. Rows affected: {cur.rowcount}',
                'row_count': cur.rowcount
            })
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 400
    finally:
        conn.close()

# -------------------------------------------------------------
# API: JAVA ENGINE BRIDGE
# -------------------------------------------------------------

@app.route('/api/java/run', methods=['POST'])
def api_java_run():
    data = request.json or {}
    action = data.get('action', 'test')
    reg_no = data.get('reg_no', '732925ITR038')

    java_bin = get_java_executable()
    cmd = [
        java_bin,
        '-cp', 'bin;lib/*',
        'com.vcet.studentanalysis.JavaEngineRunner',
        f'--action={action}',
        f'--reg={reg_no}'
    ]

    try:
        res = subprocess.run(cmd, capture_output=True, text=True, cwd=JAVA_DIR, timeout=10)
        return jsonify({
            'success': res.returncode == 0,
            'stdout': res.stdout,
            'stderr': res.stderr,
            'command': ' '.join(cmd)
        })
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/api/java/source')
def api_java_source():
    file_rel = request.args.get('file', 'model/Student.java')
    # Prevent directory traversal
    clean_path = os.path.normpath(file_rel).lstrip(os.path.sep).lstrip('/')
    full_path = os.path.join(JAVA_DIR, 'src', 'com', 'vcet', 'studentanalysis', clean_path)

    if not os.path.exists(full_path) or not full_path.endswith('.java'):
        return jsonify({'success': False, 'message': 'Java file not found.'}), 404

    with open(full_path, 'r', encoding='utf-8') as f:
        code = f.read()

    return jsonify({'success': True, 'file': file_rel, 'code': code})

# -------------------------------------------------------------
# APP STARTUP
# -------------------------------------------------------------

if __name__ == '__main__':
    print("Starting Student Academic Performance Portal...")
    print(f"Database Path: {DB_PATH}")
    app.run(host='127.0.0.1', port=5000, debug=False)
