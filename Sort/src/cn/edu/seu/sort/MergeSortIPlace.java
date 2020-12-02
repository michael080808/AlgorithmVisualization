package cn.edu.seu.sort;

import processing.core.PApplet;

public class MergeSortIPlace extends List implements Sort {
    public MergeSortIPlace(int n, ListType t) {
        super(n, t);
    }

    // 原地归并排序
    protected void Reverse(int l, int r) {
        for (int i = l, j = r - 1; i < j; i++, j--)
            Exchange(i, j);
    }

    protected void RotateL(int l, int r, int n) {
        Reverse(l, l + n);
        Reverse(l + n, r);
        Reverse(l, r);
    }

    protected void MergeIP(int l, int m, int r) {
        int i = l;
        int j = m;
        int e = r + 1;

        while (i < j && j < e) {
            int index_m = j;
            while (i < j && list.get(i).GetNumber() < list.get(j).GetNumber())
                i++;

            while (j < r && list.get(j).GetNumber() < list.get(i).GetNumber())
                j++;

            RotateL(i, j, index_m - i);
            i += j - index_m;
            if (j == r) break;
        }
    }

    protected void MergeSort_IPlace(int l, int r) {
        if (r - l > 1) {
            MergeSort_IPlace(l, (l + r) / 2);
            MergeSort_IPlace((l + r) / 2, r);
            MergeIP(l, (l + r) / 2, r);
        }
    }

    @Override
    public void sort() {
        MergeSort_IPlace(0, list.size());
    }

    // 原地归并排序
    protected void Reverse(PApplet applet, int l, int r) {
        for (int i = l, j = r - 1; i < j; i++, j--)
            Exchange(i, j);
    }

    protected void RotateL(PApplet applet, int l, int r, int n) {
        Prepare(applet, () -> {
            SetColors(l, l + n, 0xFF0000FF);
            SetColors(l + n, r, 0xFFFF00FF);
        });
        Reverse(applet, l, l + n);
        Reverse(applet, l + n, r);
        Reverse(applet, l, r);
        Prepare(applet, () -> {
            SetColors(l, r - n, 0xFFFF00FF);
            SetColors(r - n, r, 0xFF0000FF);
        });
    }

    protected void MergeIP(PApplet applet, int l, int m, int r) {
        int i = l;
        int j = m;
        int e = r + 1;
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
        });
        while (i < j && j < e) {
            int index_m = j;
            while (i < j && list.get(i).GetNumber() < list.get(j).GetNumber()) {
                TripleColor(applet, l, r, index_m, i, j);
                i++;
            }

            while (j < r && list.get(j).GetNumber() < list.get(i).GetNumber()) {
                TripleColor(applet, l, r, index_m, i, j);
                j++;
            }

            RotateL(applet, i, j, index_m - i);
            i += j - index_m;
            if (j == r) break;
        }
    }

    protected void MergeSort_IPlace(PApplet applet, int l, int r) {
        if (r - l > 1) {
            MergeSort_IPlace(applet, l, (l + r) / 2);
            MergeSort_IPlace(applet, (l + r) / 2, r);
            MergeIP(applet, l, (l + r) / 2, r);
        }
    }

    @Override
    public void sort(PApplet applet) {
        MergeSort_IPlace(applet, 0, list.size());
    }

    public void TripleColor(PApplet applet, int l, int r, int m, int a, int b) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFF0000, false);
            SetColor(a, 0xFF00FF00, false);
            SetColor(b, 0xFFFFFF00, false);
        });
    }
}
