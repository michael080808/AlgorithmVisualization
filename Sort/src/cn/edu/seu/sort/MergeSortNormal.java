package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;

public class MergeSortNormal extends List implements Sort {
    public MergeSortNormal(int n, ListType t) {
        super(n, t);
    }

    protected void MergeOP(int l, int m, int r) {
        ArrayList<Node> temp = new ArrayList<>();

        int index_i = l, index_j = m;
        while (index_i < m && index_j < r)
            if (list.get(index_i).GetNumber() < list.get(index_j).GetNumber())
                temp.add(list.get(index_i++));
            else
                temp.add(list.get(index_j++));


        while (index_i < m)
            temp.add(list.get(index_i++));

        while (index_j < r)
            temp.add(list.get(index_j++));

        for (int i = 0; i < temp.size(); i++)
            list.set(l + i, temp.get(i));
    }

    protected void MergeSort_Normal(int l, int r) {
        if (r - l > 1) {
            MergeSort_Normal(l, (l + r) / 2);
            MergeSort_Normal((l + r) / 2, r);
            MergeOP(l, (l + r) / 2, r);
        }
    }

    @Override
    public void sort() {
        MergeSort_Normal(0, list.size());
    }

    protected void MergeOP(PApplet applet, int l, int m, int r) {
        ArrayList<Node> temp = new ArrayList<>();

        int index_i = l, index_j = m;
        while (index_i < m && index_j < r) {
            DualColor(applet, l, r, index_i, index_j);
            if (list.get(index_i).GetNumber() < list.get(index_j).GetNumber())
                temp.add(list.get(index_i++));
            else
                temp.add(list.get(index_j++));
        }

        while (index_i < m) {
            SoloColor(applet, l, r, index_i);
            temp.add(list.get(index_i++));
        }

        while (index_j < r) {
            AsstColor(applet, l, r, index_j);
            temp.add(list.get(index_j++));
        }

        for (int i = 0; i < temp.size(); i++) {
            list.set(l + i, new Node(temp.get(i).GetNumber()));
            MainColor(applet, l, r, l + i);
        }
    }

    protected void MergeSort_Normal(PApplet applet, int l, int r) {
        if (r - l > 1) {
            MergeSort_Normal(applet, l, (l + r) / 2);
            MergeSort_Normal(applet, (l + r) / 2, r);
            MergeOP(applet, l, (l + r) / 2, r);
        }
    }

    @Override
    public void sort(PApplet applet) {
        MergeSort_Normal(applet, 0, list.size());
    }

    public void MainColor(PApplet applet, int l, int r, int m) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFF0000, false);
        });
    }

    public void SoloColor(PApplet applet, int l, int r, int m) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFF00FF00, false);
        });
    }

    public void AsstColor(PApplet applet, int l, int r, int m) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFFFF00, false);
        });
    }

    public void DualColor(PApplet applet, int l, int r, int a, int b) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(a, 0xFF00FF00, false);
            SetColor(b, 0xFFFFFF00, false);
        });
    }
}
