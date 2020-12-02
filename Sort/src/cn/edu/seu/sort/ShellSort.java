package cn.edu.seu.sort;

import processing.core.PApplet;

public class ShellSort extends List implements Sort {
    public ShellSort(int n, ListType t) {
        super(n, t);
    }

    protected void Insertion_Step(int l, int r, int step) {
        for (int i = l + step; i < r; i += step) {
            int j = i;
            while (j - step >= l && list.get(j).GetNumber() < list.get(j - step).GetNumber()) {
                Exchange(j, j - step);
                j -= step;
            }
        }
    }

    protected void ShellSort_Normal(int l, int r) {
        for (int i = r - l / 2; i > 0; i /= 2)
            for (int j = 0; j < i; j++)
                Insertion_Step(j, r, i);
    }

    @Override
    public void sort() {
        ShellSort_Normal(0, list.size());
    }

    protected void Insertion_Step(PApplet applet, int l, int r, int step) {
        for (int i = l + step; i < r; i += step) {
            int j = i;
            while (j - step >= l && list.get(j).GetNumber() < list.get(j - step).GetNumber()) {
                Exchange(j, j - step);
                MainColorAndAnother(applet, l, r, j - step, j);
                j -= step;
            }
        }
    }

    protected void ShellSort_Normal(PApplet applet, int l, int r) {
        for (int i = r - l / 2; i > 0; i /= 2)
            for (int j = 0; j < i; j++)
                Insertion_Step(applet, j, r, i);
    }

    @Override
    public void sort(PApplet applet) {
        ShellSort_Normal(applet, 0, list.size());
    }

    public void MainColorAndAnother(PApplet applet, int l, int r, int m, int n) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(n, 0xFF00FF00, false);
            SetColor(m, 0xFFFF0000, false);
        });
    }
}
