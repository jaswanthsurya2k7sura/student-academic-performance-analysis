"""
Data Visualization Module for Student Academic Performance & Result Analysis
Course: Foundations of Data Science (25ITC31 - Unit 5)
Libraries: Matplotlib, Seaborn, Pandas, NumPy
"""

import os
import matplotlib
matplotlib.use('Agg') # Headless rendering
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import numpy as np
from data_preprocessing import preprocess_academic_data

# Set visual aesthetic styling
sns.set_theme(style="whitegrid", palette="muted")
plt.rcParams['font.sans-serif'] = 'Arial'
plt.rcParams['font.family'] = 'sans-serif'
plt.rcParams['figure.titlesize'] = 14
plt.rcParams['axes.titlesize'] = 12
plt.rcParams['axes.labelsize'] = 11

def generate_visualizations(preprocessed=None):
    if preprocessed is None:
        preprocessed = preprocess_academic_data()

    df_marks = preprocessed['clean_marks']
    df_students = preprocessed['clean_students']
    df_subjects = preprocessed['subject_perf']

    output_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'visualizations'))
    os.makedirs(output_dir, exist_ok=True)

    generated_charts = []

    # -------------------------------------------------------------
    # Chart 1: Subject-Wise Pass Percentage (Highlighting Low Pass Rates)
    # -------------------------------------------------------------
    plt.figure(figsize=(10, 5.5))
    bars = plt.bar(
        df_subjects['subject_code'] + '\n' + df_subjects['subject_name'],
        df_subjects['pass_percentage'],
        color=['#d9534f' if p < 85 else '#5cb85c' for p in df_subjects['pass_percentage']],
        edgecolor='black',
        alpha=0.85,
        width=0.55
    )
    plt.axhline(85, color='darkred', linestyle='--', linewidth=1.5, label='Institutional Benchmark (85%)')
    plt.title('Subject-Wise Pass Percentage & Low Pass Rate Identification\n(Scenario 8: VCET Information Technology)', fontsize=13, fontweight='bold', pad=15)
    plt.ylabel('Pass Percentage (%)', fontweight='bold')
    plt.xlabel('Curriculum Subject', fontweight='bold')
    plt.ylim(0, 115)
    plt.legend(loc='lower right')

    for bar in bars:
        h = bar.get_height()
        plt.text(bar.get_x() + bar.get_width()/2., h + 2, f'{h:.1f}%', ha='center', va='bottom', fontweight='bold', fontsize=10)

    plt.tight_layout()
    chart1_path = os.path.join(output_dir, '01_subject_pass_percentage.png')
    plt.savefig(chart1_path, dpi=300)
    plt.close()
    generated_charts.append(chart1_path)

    # -------------------------------------------------------------
    # Chart 2: Grade Distribution Across Department (Donut Chart)
    # -------------------------------------------------------------
    plt.figure(figsize=(8, 7))
    grade_counts = df_marks['grade'].value_counts()
    grade_order = ['O', 'A+', 'A', 'B+', 'B', 'RA']
    counts_ordered = [grade_counts.get(g, 0) for g in grade_order if grade_counts.get(g, 0) > 0]
    labels_ordered = [g for g in grade_order if grade_counts.get(g, 0) > 0]
    colors = ['#2ca02c', '#1f77b4', '#17becf', '#ff7f0e', '#bcbd22', '#d62728']

    wedges, texts, autotexts = plt.pie(
        counts_ordered,
        labels=labels_ordered,
        autopct='%1.1f%%',
        startangle=140,
        colors=colors[:len(labels_ordered)],
        wedgeprops=dict(width=0.4, edgecolor='white', linewidth=2),
        pctdistance=0.75,
        textprops=dict(fontweight='bold')
    )
    plt.title('Department-Wide Letter Grade Distribution\n(VCET 2026-27 ODD Semester)', fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart2_path = os.path.join(output_dir, '02_grade_distribution.png')
    plt.savefig(chart2_path, dpi=300)
    plt.close()
    generated_charts.append(chart2_path)

    # -------------------------------------------------------------
    # Chart 3: Distribution of Marks (Histogram & KDE)
    # -------------------------------------------------------------
    plt.figure(figsize=(10, 5.5))
    sns.histplot(df_marks['total_marks'], kde=True, color='#2b5c8f', bins=12, edgecolor='black', alpha=0.6, label='Total Marks (100)')
    plt.axvline(df_marks['total_marks'].mean(), color='red', linestyle='--', linewidth=1.5, label=f'Mean Score ({df_marks["total_marks"].mean():.1f})')
    plt.axvline(df_marks['total_marks'].median(), color='green', linestyle=':', linewidth=1.5, label=f'Median Score ({df_marks["total_marks"].median():.1f})')
    plt.title('Distribution of Student Total Marks with KDE Fit', fontsize=13, fontweight='bold', pad=15)
    plt.xlabel('Total Marks (out of 100)', fontweight='bold')
    plt.ylabel('Frequency (Number of Evaluations)', fontweight='bold')
    plt.legend()
    plt.tight_layout()
    chart3_path = os.path.join(output_dir, '03_marks_distribution_kde.png')
    plt.savefig(chart3_path, dpi=300)
    plt.close()
    generated_charts.append(chart3_path)

    # -------------------------------------------------------------
    # Chart 4: Subject Marks Dispersion (Seaborn Box Plot)
    # -------------------------------------------------------------
    plt.figure(figsize=(11, 6))
    it_marks = df_marks[df_marks['dept_code'] == 'IT']
    sns.boxplot(
        x='subject_code',
        y='total_marks',
        data=it_marks,
        palette='Set2',
        width=0.45,
        fliersize=5
    )
    sns.stripplot(x='subject_code', y='total_marks', data=it_marks, color='black', alpha=0.5, jitter=0.2, size=6)
    plt.title('Subject-Wise Score Dispersion and Quartile Spread (IT Department)\nBoxplot with Overlaid Individual Observations', fontsize=13, fontweight='bold', pad=15)
    plt.xlabel('Subject Code', fontweight='bold')
    plt.ylabel('Total Marks (out of 100)', fontweight='bold')
    plt.ylim(30, 105)
    plt.tight_layout()
    chart4_path = os.path.join(output_dir, '04_subject_marks_boxplot.png')
    plt.savefig(chart4_path, dpi=300)
    plt.close()
    generated_charts.append(chart4_path)

    # -------------------------------------------------------------
    # Chart 5: Correlation Heatmap
    # -------------------------------------------------------------
    plt.figure(figsize=(8, 6.5))
    corr_cols = ['internal_marks', 'external_marks', 'total_marks', 'credits', 'grade_point']
    corr_matrix = df_marks[corr_cols].corr()
    sns.heatmap(
        corr_matrix,
        annot=True,
        cmap='Blues',
        fmt='.2f',
        linewidths=1.0,
        linecolor='white',
        cbar_kws={'label': 'Pearson Correlation Coefficient'}
    )
    plt.title('Correlation Heatmap: Assessment Metrics & Performance', fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart5_path = os.path.join(output_dir, '05_correlation_heatmap.png')
    plt.savefig(chart5_path, dpi=300)
    plt.close()
    generated_charts.append(chart5_path)

    # -------------------------------------------------------------
    # Chart 6: Section-Wise Comparative Analysis (25IT3A vs 25IT3B)
    # -------------------------------------------------------------
    plt.figure(figsize=(9, 5.5))
    section_summary = it_marks.groupby('section').agg(
        avg_total=('total_marks', 'mean'),
        avg_internal=('internal_marks', 'mean'),
        avg_external=('external_marks', 'mean')
    ).reset_index()

    x = np.arange(len(section_summary))
    width = 0.25
    plt.bar(x - width, section_summary['avg_internal'], width, label='Internal Avg (40)', color='#4a90e2')
    plt.bar(x, section_summary['avg_external'], width, label='External Avg (60)', color='#50e3c2')
    plt.bar(x + width, section_summary['avg_total'], width, label='Total Avg (100)', color='#f5a623')

    plt.xticks(x, ['Section 25IT3A', 'Section 25IT3B'], fontweight='bold', fontsize=11)
    plt.ylabel('Average Score', fontweight='bold')
    plt.title('Academic Performance Comparison Between Class Sections (25IT3A vs 25IT3B)', fontsize=13, fontweight='bold', pad=15)
    plt.legend()
    plt.ylim(0, 100)
    plt.tight_layout()
    chart6_path = os.path.join(output_dir, '06_section_performance_comparison.png')
    plt.savefig(chart6_path, dpi=300)
    plt.close()
    generated_charts.append(chart6_path)

    # -------------------------------------------------------------
    # Chart 7: Top Performers (Merit Leaderboard)
    # -------------------------------------------------------------
    plt.figure(figsize=(10, 6))
    top_students = df_students[df_students['dept_code'] == 'IT'].sort_values(by=['gpa', 'grand_total'], ascending=True).tail(8)

    colors = ['#f39c12' if '038' in reg else '#3498db' for reg in top_students['register_no']]
    bars = plt.barh(
        top_students['student_name'] + ' (' + top_students['register_no'] + ')',
        top_students['gpa'],
        color=colors,
        edgecolor='black',
        height=0.6
    )
    plt.title('Department Merit Leaderboard (Top Rankers by GPA)\nHighlighting Jaswanth surya S (Reg No: 732925ITR038)', fontsize=13, fontweight='bold', pad=15)
    plt.xlabel('Grade Point Average (GPA)', fontweight='bold')
    plt.xlim(0, 11)

    for bar in bars:
        w = bar.get_width()
        plt.text(w + 0.15, bar.get_y() + bar.get_height()/2., f'GPA: {w:.2f}', va='center', fontweight='bold', fontsize=9.5)

    plt.tight_layout()
    chart7_path = os.path.join(output_dir, '07_top_rankers_gpa.png')
    plt.savefig(chart7_path, dpi=300)
    plt.close()
    generated_charts.append(chart7_path)

    print(f"\n[✓] Successfully generated and exported {len(generated_charts)} publication-quality charts to:")
    print(f"    {output_dir}")
    return generated_charts

if __name__ == '__main__':
    generate_visualizations()
