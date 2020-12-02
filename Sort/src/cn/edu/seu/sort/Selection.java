package cn.edu.seu.sort;

import processing.core.PApplet;

public class Selection extends List implements Sort {
    public Selection(int n, ListType t) {
        super(n, t);
    }

    protected void Selection(int l, int r) {
        for (int i = 0; i < r; i++) {
            int m = i;
            for (int j = i; j < r; j++)
                if (list.get(j).GetNumber() < list.get(m).GetNumber())
                    m = j;
            Exchange(i, m);
        }
    }

    @Override
    public void sort() {
        Selection(0, list.size());
    }

    protected void Selection(PApplet applet, int l, int r) {
        for (int i = 0; i < r; i++) {
            int m = i;
            for (int j = i; j < r; j++) {
                if (list.get(j).GetNumber() < list.get(m).GetNumber())
                    m = j;
                DualColor(applet, i, r, m, j);
            }
            Exchange(i, m);
            MainColor(applet, i, r, i);
        }
    }

    @Override
    public void sort(PApplet applet) {
        Selection(applet, 0, list.size());
    }

    public void MainColor(PApplet applet, int l, int r, int m) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(m, 0xFFFF0000, false);
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
