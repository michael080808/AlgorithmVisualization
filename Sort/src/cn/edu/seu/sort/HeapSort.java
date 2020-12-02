package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;

public class HeapSort extends List implements Sort {
    private final ArrayList<Integer> HeapColors = new ArrayList<>();

    public HeapSort(int n, ListType t) {
        super(n, t);
    }

    public int clog2(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    // 堆排相关
    protected void HeapSort_Normal(int l, int r) {
        for (int i = l + 1; i < r; i++) {
            int curr = i;
            int root = l + (curr - l + 1) / 2 - 1;
            while (curr > l) {
                if (list.get(root).GetNumber() < list.get(curr).GetNumber())
                    Exchange(curr, root);
                curr = root;
                root = l + (curr - l + 1) / 2;
            }
        }

        for (int i = r; i > 0; i--) {
            Exchange(l, i - 1);

            int curr = l;
            int no_l = l + (curr - l + 1) * 2 - 1;
            int no_r = l + (curr - l + 1) * 2 - 0;
            while (no_l < i - 1) {
                int comp = no_r < i - 1 && list.get(no_r).GetNumber() > list.get(no_l).GetNumber() ? no_r : no_l;
                if (list.get(curr).GetNumber() < list.get(comp).GetNumber()) {
                    Exchange(curr, comp);
                    curr = comp;
                    no_l = l + (curr - l + 1) * 2 - 1;
                    no_r = l + (curr - l + 1) * 2 - 0;
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void sort() {
        HeapSort_Normal(0, list.size());
    }

    // 堆排相关
    protected void HeapSort_Normal(PApplet applet, int l, int r) {
        HeapColors(l, r);

        for (int i = l + 1; i < r; i++) {
            int curr = i;
            int root = l + (curr - l + 1) / 2 - 1;
            HeapColors(applet, l, i, curr);
            while (curr > l && list.get(root).GetNumber() < list.get(curr).GetNumber()) {
                Exchange(curr, root);
                curr = root;
                root = l + (curr - l + 1) / 2 - 1;
                HeapColors(applet, l, i, curr);
            }
        }

        HeapColors(applet, l, r, l);
        for (int i = r; i > l; i--) {
            Exchange(l, i - 1);
            int curr = l;
            int no_l = l + (curr - l + 1) * 2 - 1;
            int no_r = l + (curr - l + 1) * 2 - 0;
            HeapColors(applet, l, i - 1, curr);
            while (no_l < i - 1) {
                int comp = (no_r < i - 1 && list.get(no_r).GetNumber() > list.get(no_l).GetNumber()) ? no_r : no_l;
                if (list.get(curr).GetNumber() < list.get(comp).GetNumber()) {
                    Exchange(curr, comp);
                    curr = comp;
                    no_l = l + (curr - l + 1) * 2 - 1;
                    no_r = l + (curr - l + 1) * 2 - 0;
                    HeapColors(applet, l, i - 1, curr);
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void sort(PApplet applet) {
        HeapSort_Normal(applet, 0, list.size());
    }

    // 设置堆的颜色系列
    protected void HeapColors(int l, int r) {
        HeapColors.clear();
        for (int i = 0; i <= clog2(r - l); i++)
            HeapColors.add((int) (0xFF800000 + Math.random() * 0x00800000));
    }

    // 设置堆的颜色
    protected void HeapColors(PApplet applet, int l, int r, int m) {
        Prepare(applet, () -> {
            for (int i = l; i < r; i++)
                SetColor(i, HeapColors.get(clog2(i - l + 1)), false);
            SetColor(m, 0xFFFF0000, false);
        });
    }
}
