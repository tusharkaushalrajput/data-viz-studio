package com.dvs.simple;

import com.dvs.model.DataSet;

/**
 * Minimal non-GUI runner to verify JVM can start and data model works under tiny heap.
 */
public class MiniRun {
    public static void main(String[] args) {
        System.out.println("MiniRun start (heap test)");
        DataSet ds = new DataSet("Mini");
        ds.addColumn("Item");
        ds.addColumn("Value");
        for (int i=0;i<5;i++) ds.addRow("Row"+i, i*10);
        System.out.println(ds.getPreviewText());
        System.out.flush();
    }
}
