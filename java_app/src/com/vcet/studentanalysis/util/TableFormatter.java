package com.vcet.studentanalysis.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for formatting CLI output into clean ASCII tables.
 */
public class TableFormatter {
    private final String[] headers;
    private final List<String[]> rows = new ArrayList<>();

    public TableFormatter(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... row) {
        rows.add(row);
    }

    public void print() {
        if (headers == null || headers.length == 0) return;

        int cols = headers.length;
        int[] colWidths = new int[cols];

        for (int i = 0; i < cols; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < Math.min(row.length, cols); i++) {
                if (row[i] != null && row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        // Add padding
        for (int i = 0; i < cols; i++) {
            colWidths[i] += 2;
        }

        String border = buildBorder(colWidths);
        System.out.println(border);
        printRow(headers, colWidths);
        System.out.println(border);

        for (String[] row : rows) {
            printRow(row, colWidths);
        }

        System.out.println(border);
    }

    private void printRow(String[] row, int[] colWidths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < colWidths.length; i++) {
            String val = (i < row.length && row[i] != null) ? row[i] : "";
            sb.append(" ").append(val);
            int padding = colWidths[i] - val.length() - 1;
            for (int p = 0; p < padding; p++) {
                sb.append(" ");
            }
            sb.append("|");
        }
        System.out.println(sb.toString());
    }

    private String buildBorder(int[] colWidths) {
        StringBuilder sb = new StringBuilder("+");
        for (int width : colWidths) {
            for (int i = 0; i < width; i++) {
                sb.append("-");
            }
            sb.append("+");
        }
        return sb.toString();
    }
}
