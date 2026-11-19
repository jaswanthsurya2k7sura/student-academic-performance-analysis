import sqlite3
import os

db_path = os.path.join(os.path.dirname(__file__), 'student_performance.db')
schema_path = os.path.join(os.path.dirname(__file__), 'schema.sql')
views_path = os.path.join(os.path.dirname(__file__), 'views_triggers.sql')
seed_path = os.path.join(os.path.dirname(__file__), 'seed_data.sql')

if os.path.exists(db_path):
    os.remove(db_path)

conn = sqlite3.connect(db_path)
cursor = conn.cursor()

for path in [schema_path, views_path, seed_path]:
    with open(path, 'r', encoding='utf-8') as f:
        script = f.read()
    cursor.executescript(script)
    print(f"Successfully executed: {os.path.basename(path)}")

conn.commit()

# Run sanity queries
print("\n--- SANITY CHECK RESULTS ---")
cursor.execute("SELECT COUNT(*) FROM departments")
print(f"Departments count: {cursor.fetchone()[0]}")

cursor.execute("SELECT COUNT(*) FROM students")
print(f"Students count: {cursor.fetchone()[0]}")

cursor.execute("SELECT COUNT(*) FROM subjects")
print(f"Subjects count: {cursor.fetchone()[0]}")

cursor.execute("SELECT COUNT(*) FROM marks")
print(f"Marks records: {cursor.fetchone()[0]}")

cursor.execute("SELECT register_no, student_name, section, grand_total, average_marks, gpa, overall_result FROM v_student_semester_summary WHERE register_no = '732925ITR038'")
row = cursor.fetchone()
print(f"\nStudent Details:")
print(f"  Reg No:  {row[0]}")
print(f"  Name:    {row[1]}")
print(f"  Section: {row[2]}")
print(f"  Total:   {row[3]}/400")
print(f"  Avg:     {row[4]}%")
print(f"  GPA:     {row[5]}")
print(f"  Result:  {row[6]}")

conn.close()
print("\nDatabase initialization complete and verified!")
