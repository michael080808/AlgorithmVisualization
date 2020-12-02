package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;

public class IntroSort extends List implements Sort {
    private final int threshold = 16;

    private final ArrayList<Integer> HeapColors = new ArrayList<>();

    public IntroSort(int n, ListType t) {
        super(n, t);
    }

    public int clog2(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    // 快排相关：选取中值
    protected int Median_Pivot(int l, int m, int r) {
        if (l < list.size() && m < list.size() && r < list.size()) {
            Node node_l = list.get(l);
            Node node_m = list.get(m);
            Node node_r = list.get(r);
            if ((node_l.GetNumber() <= node_m.GetNumber() && node_m.GetNumber() <= node_r.GetNumber()) || (node_r.GetNumber() <= node_m.GetNumber() && node_m.GetNumber() <= node_l.GetNumber()))
                return m;
            if ((node_m.GetNumber() <= node_l.GetNumber() && node_l.GetNumber() <= node_r.GetNumber()) || (node_r.GetNumber() <= node_l.GetNumber() && node_l.GetNumber() <= node_m.GetNumber()))
                return l;
            if ((node_l.GetNumber() <= node_r.GetNumber() && node_r.GetNumber() <= node_m.GetNumber()) || (node_m.GetNumber() <= node_r.GetNumber() && node_r.GetNumber() <= node_l.GetNumber()))
                return r;
        }
        return -1;
    }

    // 快排相关：参考值移动至队首
    protected void Move(int l, int p) {
        if (l < list.size() && p < list.size()) {
            Node Head = list.get(l);
            Node Swap = list.get(p);
            list.set(l, Swap);
            list.set(p, Head);
        }
    }

    // 快排相关：划分操作
    protected int Partition(int l, int r) {
        int index_l = l + 1;
        int index_r = r - 1;

        while (index_l < index_r) {
            while (list.get(index_l).GetNumber() < list.get(l).GetNumber())
                index_l++;
            while (list.get(index_r).GetNumber() > list.get(l).GetNumber())
                index_r--;
            if (index_l < index_r)
                Exchange(index_l++, index_r--);
        }

        if (list.get(index_r).GetNumber() < list.get(l).GetNumber())
            Exchange(l, index_r);
        return index_r;
    }

    // 堆排相关
    protected void HeapSort(int l, int r) {
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

    // 插入相关：与首位比较
    protected void Insertion_CheckF(int l, int r, int s) {
        for (int i = s; i < r; i++) {
            if (list.get(i).GetNumber() < list.get(l).GetNumber()) {
                for (int j = i; j > l; j--)
                    Exchange(j, j - 1);
            } else {
                for (int j = i; j > l; j--)
                    if (list.get(j).GetNumber() < list.get(j - 1).GetNumber())
                        Exchange(j, j - 1);
                    else
                        break;
            }
        }
    }

    // 插入相关：正常进行
    protected void Insertion_Normal(int l, int r, int s) {
        for (int i = s; i < r; i++) {
            for (int j = i; j > l; j--)
                if (list.get(j).GetNumber() < list.get(j - 1).GetNumber())
                    Exchange(j, j - 1);
                else
                    break;
        }
    }

    // 最后插入排序
    protected void Final_Insertion(int l, int r) {
        Insertion_CheckF(l, l + threshold, l + 1);
        Insertion_Normal(l, r, l + threshold);
    }

    // 内省排序递归
    protected void Introsort_Loop(int l, int r, int d) {
        if (r - l > threshold) {
            if (d == 0) {
                HeapSort(l, r);
            } else {
                // 获取当前参考值
                int p = Median_Pivot(l, (l + r) / 2, r - 1);
                // 参考值移动至头部
                Move(l, p);
                // 进行划分操作
                int m = Partition(l, r);
                // 向下递归
                Introsort_Loop(l, m, d - 1);
                Introsort_Loop(m, r, d - 1);
            }
        }
    }

    @Override
    public void sort() {
        Introsort_Loop(0, list.size(), 2 * clog2(list.size()));
        Final_Insertion(0, list.size());
    }

    // 快排相关：选取中值
    protected int Median_Pivot(PApplet applet, int l, int m, int r) {
        if (l < list.size() && m < list.size() && r < list.size()) {
            Node node_l = list.get(l);
            Node node_m = list.get(m);
            Node node_r = list.get(r);
            if ((node_l.GetNumber() < node_m.GetNumber() && node_m.GetNumber() < node_r.GetNumber())) {
                TripleColors(applet, m, l, r, true);
                return m;
            }
            if ((node_r.GetNumber() < node_m.GetNumber() && node_m.GetNumber() < node_l.GetNumber())) {
                TripleColors(applet, m, r, l, true);
                return m;
            }
            if ((node_m.GetNumber() < node_l.GetNumber() && node_l.GetNumber() < node_r.GetNumber())) {
                TripleColors(applet, l, m, r, true);
                return l;
            }
            if ((node_r.GetNumber() < node_l.GetNumber() && node_l.GetNumber() < node_m.GetNumber())) {
                TripleColors(applet, l, r, m, true);
                return l;
            }
            if ((node_l.GetNumber() < node_r.GetNumber() && node_r.GetNumber() < node_m.GetNumber())) {
                TripleColors(applet, r, l, m, true);
                return r;
            }
            if ((node_m.GetNumber() < node_r.GetNumber() && node_r.GetNumber() < node_l.GetNumber())) {
                TripleColors(applet, r, m, l, true);
                return r;
            }
        }
        return -1;
    }

    // 快排相关：参考值移动至队首
    protected void Move(PApplet applet, int l, int p) {
        if (l < list.size() && p < list.size()) {
            Node Head = list.get(l);
            Node Swap = list.get(p);
            list.set(l, Swap);
            list.set(p, Head);
            Prepare(applet, () -> {
                SetColor(l, 0xFFFF0000, true);
            });
        }
    }

    // 快排相关：划分操作
    protected int Partition(PApplet applet, int l, int r) {
        int index_l = l + 1;
        int index_r = r - 1;
        TripleColors(applet, l, r, l, index_l, index_r, true);

        while (index_l < index_r) {
            while (list.get(index_l).GetNumber() < list.get(l).GetNumber()) {
                index_l++;
                TripleColors(applet, l, r, l, index_l, index_r, true);
            }
            while (list.get(index_r).GetNumber() > list.get(l).GetNumber()) {
                index_r--;
                TripleColors(applet, l, r, l, index_l, index_r, true);
            }
            if (index_l < index_r) {
                Exchange(index_l, index_r);
                TripleColors(applet, l, r, l, index_l, index_r, true);
                index_l++;
                index_r--;
            }
        }

        if (list.get(index_r).GetNumber() < list.get(l).GetNumber()) {
            Exchange(l, index_r);
            TripleColors(applet, l, r, index_r, index_l, l, true);
        } else {
            TripleColors(applet, l, r, l, index_l, index_r, true);
        }
        return index_r;
    }

    // 堆排相关
    protected void HeapSort(PApplet applet, int l, int r) {
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

    // 最后插入排序
    protected void Final_Insertion(PApplet applet, int l, int r) {
        Insertion_CheckF(applet, l, l + threshold, l + 1);
        Insertion_Normal(applet, l, r, l + threshold);
    }

    // 内省排序递归
    protected void Introsort_Loop(PApplet applet, int l, int r, int d) {
        if (r - l > threshold) {
            if (d == 0) {
                HeapSort(applet, l, r);
            } else {
                // 获取当前参考值
                int p = Median_Pivot(applet, l, (l + r) / 2, r - 1);
                // 参考值移动至头部
                Move(applet, l, p);
                // 进行划分操作
                int m = Partition(applet, l, r);
                // 向下递归
                Introsort_Loop(applet, l, m, d - 1);
                Introsort_Loop(applet, m, r, d - 1);
            }
        }
    }

    @Override
    public void sort(PApplet applet) {
        Introsort_Loop(applet, 0, list.size(), 2 * clog2(list.size()));
        Final_Insertion(applet, 0, list.size());
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

    // 设置三种颜色
    protected void TripleColors(PApplet applet, int m, int a, int b, boolean ref) {
        Prepare(applet, () -> {
            SetColor(m, 0xFFFF0000, ref);
            SetColor(a, 0xFF00FF00, false);
            SetColor(b, 0xFFFFFF00, false);
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
