package cn.edu.seu.sort;

import processing.core.PApplet;

public class InsertionCheckF extends List implements Sort {
    public InsertionCheckF(int n, ListType t) {
        super(n, t);
    }

    // 插入相关：与首位比较
    protected void Insertion_CheckF(int l, int r, int s) {
        for (int i = s; i < r; i++) {
            if (list.get(i).GetNumber() < list.get(l).GetNumber()) {
                for (int j = i; j > l; j--) {
                    Exchange(j, j - 1);
                }
            } else {
                for (int j = i; j > l; j--)
                    if (list.get(j).GetNumber() < list.get(j - 1).GetNumber()) {
                        Exchange(j, j - 1);
                    } else break;
            }
        }
    }


    @Override
    public void sort() {
        Insertion_CheckF(0, list.size(), 1);
    }

    // 插入相关：与首位比较
    protected void Insertion_CheckF(PApplet applet, int l, int r, int s) {
        for (int i = s; i < r; i++) {
            if (list.get(i).GetNumber() < list.get(l).GetNumber()) {
                for (int j = i; j > l; j--) {
                    Exchange(j, j - 1);
                    TripleColors(applet, l, i, l, j, j - 1, false);
                }
            } else {
                for (int j = i; j > l; j--)
                    if (list.get(j).GetNumber() < list.get(j - 1).GetNumber()) {
                        Exchange(j, j - 1);
                        TripleColors(applet, l, i, l, j, j - 1, false);
                    } else break;
            }
        }
    }

    @Override
    public void sort(PApplet applet) {
        Insertion_CheckF(applet, 0, list.size(), 1);
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
