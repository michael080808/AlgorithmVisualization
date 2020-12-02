package cn.edu.seu.sort;

import processing.core.PApplet;

public class BubbleSort extends List implements Sort {
    public BubbleSort(int n, ListType t) {
        super(n, t);
    }

    protected void BubbleSort_Normal(int l, int r) {
        for (int i = l; i < r; i++)
            for (int j = i + 1; j < r; j++)
                if (list.get(j).GetNumber() < list.get(i).GetNumber())
                    Exchange(i, j);
    }

    @Override
    public void sort() {
        BubbleSort_Normal(0, list.size());
    }

    protected void BubbleSort_Normal(PApplet applet, int l, int r) {
        for (int i = l; i < r; i++)
            for (int j = i + 1; j < r; j++)
                if (list.get(j).GetNumber() < list.get(i).GetNumber()) {
                    Exchange(i, j);
                    MainColorAndAnother(applet, i, r, i, j);
                }
    }

    @Override
    public void sort(PApplet applet) {
        BubbleSort_Normal(applet, 0, list.size());
    }

    public void MainColorAndAnother(PApplet applet, int l, int r, int m, int n) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(n, 0xFF00FF00, false);
            SetColor(m, 0xFFFF0000, false);
        });
    }
}
