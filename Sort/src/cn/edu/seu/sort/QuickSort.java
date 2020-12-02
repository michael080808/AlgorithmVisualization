package cn.edu.seu.sort;

import processing.core.PApplet;

public class QuickSort extends List implements Sort {
    public QuickSort(int n, ListType t) {
        super(n, t);
    }

    protected int Partition(int l, int r) {
        int i = l + 1, j = r - 1;
        while (i < j) {
            while (list.get(i).GetNumber() < list.get(l).GetNumber())
                i++;
            while (list.get(j).GetNumber() > list.get(l).GetNumber())
                j--;
            if (i < j)
                Exchange(i, j);
        }

        if (list.get(j).GetNumber() < list.get(l).GetNumber())
            Exchange(j, l);
        return j;
    }

    protected void QuickSort_Normal(int l, int r) {
        int m = Partition(l, r);
        if (m - l > 1)
            QuickSort_Normal(l, m - 0);
        if (r - m > 1)
            QuickSort_Normal(m + 1, r);
    }

    @Override
    public void sort() {
        QuickSort_Normal(0, list.size());
    }

    protected int Partition(PApplet applet, int l, int r) {
        if (l < list.size())
            MainColor(applet, l, r, l, true);

        int i = l + 1, j = r - 1;
        while (i < j) {
            while (list.get(i).GetNumber() < list.get(l).GetNumber())
                TripleColors(applet, l, r, l, i++, j, true);
            while (list.get(j).GetNumber() > list.get(l).GetNumber())
                TripleColors(applet, l, r, l, i, j--, true);
            if (i < j) {
                Exchange(i, j);
                TripleColors(applet, l, r, l, i, j, true);
            }
        }

        if (l < list.size())
            MainColorAndAnother(applet, l, r, l, j);
        if (list.get(j).GetNumber() < list.get(l).GetNumber()) {
            Exchange(j, l);
            MainColorAndAnother(applet, l, r, j, l);
        }
        return j;
    }

    protected void QuickSort_Normal(PApplet applet, int l, int r) {
        int m = Partition(applet, l, r);
        if (m - l > 1)
            QuickSort_Normal(applet, l, m - 0);
        if (r - m > 1)
            QuickSort_Normal(applet, m + 1, r);
    }

    @Override
    public void sort(PApplet applet) {
        QuickSort_Normal(applet, 0, list.size());
    }

    // 设置主颜色
    public void MainColor(PApplet applet, int l, int r, int m, boolean ref) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFF0000, ref);
        });
    }

    public void MainColorAndAnother(PApplet applet, int l, int r, int m, int n) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(n, 0xFF00FF00, false);
            SetColor(m, 0xFFFF0000, false);
        });
    }

    // 设置三种颜色
    protected void TripleColors(PApplet applet, int l, int r, int m, int a, int b, boolean ref) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFF0000, ref);
            SetColor(a, 0xFF00FF00, false);
            SetColor(b, 0xFFFFFF00, false);
        });
    }
}
