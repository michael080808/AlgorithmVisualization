package cn.edu.seu.sort;

import processing.core.PApplet;

public class InsertionNormal extends List implements Sort {
    public InsertionNormal(int n, ListType t) {
        super(n, t);
    }

    // 插入相关：正常进行
    protected void Insertion_Normal(int l, int r, int s) {
        for (int i = s; i < r; i++) {
            for (int j = i; j > l; j--)
                if (list.get(j).GetNumber() < list.get(j - 1).GetNumber()) {
                    Exchange(j, j - 1);
                } else break;
        }
    }

    @Override
    public void sort() {
        Insertion_Normal(0, list.size(), 1);
    }

    // 插入相关：正常进行
    protected void Insertion_Normal(PApplet applet, int l, int r, int s) {
        for (int i = s; i < r; i++) {
            for (int j = i; j > l; j--)
                if (list.get(j).GetNumber() < list.get(j - 1).GetNumber()) {
                    Exchange(j, j - 1);
                    TripleColors(applet, l, i, l, j, j - 1, false);
                } else break;
        }
    }

    @Override
    public void sort(PApplet applet) {
        Insertion_Normal(applet, 0, list.size(), 1);
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
