package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;

public class Adaptive extends List implements Sort {
    private final int threshold = 15;

    private int buffer_size = 80;

    private boolean buffer_enable = true;

    public Adaptive(int n, ListType t) {
        super(n, t);
    }

    public void SetBufferSize(int s) {
        this.buffer_size = s;
    }

    public void SetBufferEnable(boolean e) {
        this.buffer_enable = e;
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

        while (i < r && i < j) {
            int index_m = j;
            while (i < j && list.get(i).GetNumber() < list.get(j).GetNumber())
                i++;

            while (j < e && list.get(j).GetNumber() < list.get(i).GetNumber())
                j++;

            RotateL(i, j, index_m - i);
            i += j - index_m;
        }
    }

    protected void MergeSort_IPlace(int l, int r) {
        if (r - l < threshold) {
            Insertion_CheckF(l, r, l + 1);
        } else {
            MergeSort_IPlace(l, (l + r) / 2);
            MergeSort_IPlace((l + r) / 2, r);
            MergeIP(l, (l + r) / 2, r);
        }
    }

    // 缓存归并排序
    protected void Merge2B(int l, int r, int step, int f, ArrayList<Integer> buf) {
        for (int i = l; i < r; i += step * 2) {
            int m = l + step;
            int t = f;
            if (m < r) {
                int index_l = l;
                int index_r = m;
                while (index_l < m && index_r < r) {
                    if (list.get(index_l).GetNumber() < list.get(index_r).GetNumber())
                        buf.set(t++, list.get(index_l++).GetNumber());
                    else
                        buf.set(t++, list.get(index_r++).GetNumber());
                }

                while (index_l < m) {
                    buf.set(t++, list.get(index_l++).GetNumber());
                }

                while (index_r < r) {
                    buf.set(t++, list.get(index_r++).GetNumber());
                }
            } else {
                for (int j = i; j < r; j++)
                    buf.set(f + j - l, list.get(j).Number);
            }
        }
    }

    protected void Merge2L(int l, int r, int step, int f, ArrayList<Integer> buf) {
        for (int i = l; i < r; i += step * 2) {
            int m = l + step;
            int t = l;
            if (m < r) {
                int index_l = l;
                int index_r = m;
                while (index_l < m && index_r < r) {
                    if (buf.get(index_l - l + f) < buf.get(index_r - l + f))
                        list.set(t++, new Node(buf.get(index_l++ - l + f)));
                    else
                        list.set(t++, new Node(buf.get(index_r++ - l + f)));
                }

                while (index_l < m) {
                    list.set(t++, new Node(buf.get(index_l++ - l + f)));
                }

                while (index_r < r) {
                    list.set(t++, new Node(buf.get(index_r++ - l + f)));
                }
            } else {
                for (int j = i; j < r; j++)
                    list.set(j, new Node(buf.get(f + j - l)));
            }
        }
    }

    protected void MergeIB(int l, int r, int f, ArrayList<Integer> buf) {
        long step = 0x40000000;
        while (step > threshold)
            step >>>= 1;
        boolean use_buffer = true;
        while (step < r - l) {
            if (step < threshold) {
                for (int i = l; i < r; i += step)
                    Insertion_CheckF(i, (int) Math.min(i + step, r), l + 1);
            } else {
                if (use_buffer) {
                    Merge2B(l, r, (int) step, l, buf);
                    use_buffer = false;
                } else {
                    Merge2L(l, r, (int) step, l, buf);
                    use_buffer = true;
                }
            }
            step <<= 1;
        }

        if (!use_buffer)
            for (int i = l; i < r; i++)
                list.set(i, new Node(buf.get(f + i - l)));
    }

    protected void MergeWB(int l, int m, int r, ArrayList<Integer> buf) {
        int length_l = m - l;
        int length_r = r - m;

        if (length_l > buf.size() && length_r > buf.size()) {
            if (length_l >= length_r) {
                int n = l + length_l / 2;
                int k = m + length_r / 2;
                while (!(m < k && k < r && list.get(k - 1).GetNumber() <= list.get(n).GetNumber() && list.get(k).GetNumber() > list.get(n).GetNumber()))
                    k = list.get(k).GetNumber() < list.get(n).GetNumber() ? k + 1 : k - 1;
                RotateL(n, k, m - n);
                MergeWB(l, n, n + k - m, buf);
                MergeWB(n + k - m, k, r, buf);
            } else {
                int n = m + length_r / 2;
                int k = l + length_l / 2;
                while (!(l < k && k < m && list.get(k - 1).GetNumber() <= list.get(n).GetNumber() && list.get(k).GetNumber() > list.get(n).GetNumber()))
                    k = list.get(k).GetNumber() < list.get(n).GetNumber() ? k + 1 : k - 1;
                RotateL(k, n, m - k);
                MergeWB(l, k, k + n - m, buf);
                MergeWB(k + n - m, n, r, buf);
            }
        } else if (length_l <= length_r && length_l <= buf.size()) {
            for (int i = 0; i < length_l; i++)
                buf.set(i, list.get(l + i).GetNumber());
            // 向左归并
            int index_i = 0, index_j = m, index_t = l;
            while (index_i < length_l && index_j < r) {
                if (buf.get(index_i) < list.get(index_j).GetNumber()) {
                    list.set(index_t++, new Node(buf.get(index_i++)));
                } else {
                    list.set(index_t++, new Node(list.get(index_j++).GetNumber()));
                }
            }

            while (index_i < length_l) {
                list.set(index_t++, new Node(buf.get(index_i++)));
            }
        } else if (length_r <= length_l && length_r <= buf.size()) {
            for (int i = 0; i < length_r; i++)
                buf.set(i, list.get(m + i).GetNumber());
            // 向右归并
            int index_i = length_r - 1, index_j = m - 1, index_t = r - 1;
            while (index_i >= 0 && index_j >= l) {
                if (buf.get(index_i) > list.get(index_j).GetNumber()) {
                    list.set(index_t--, new Node(buf.get(index_i--)));
                } else {
                    list.set(index_t--, new Node(list.get(index_j--).GetNumber()));
                }
            }

            while (index_i >= 0) {
                list.set(index_t--, new Node(buf.get(index_i--)));
            }
        }
    }

    protected void MergeSort_Buffer(int l, int r) {
        int m = (l + r) / 2;
        ArrayList<Integer> buffer = new ArrayList<>();
        for (int i = 0; i < buffer_size; i++)
            buffer.add(0);

        if (r - l > buffer_size) {
            MergeSort_Buffer(l, m);
            MergeSort_Buffer(m, r);
        } else {
            MergeIB(l, m, l, buffer);
            MergeIB(m, r, m, buffer);
        }

        MergeWB(l, m, r, buffer);
    }

    // 插入排序
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

    @Override
    public void sort() {
        if (buffer_enable)
            MergeSort_Buffer(0, list.size());
        else
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

        while (i < j && j < e) {
            int index_m = j;
            while (i < j && list.get(i).GetNumber() < list.get(j).GetNumber())
                i++;

            while (j < r && list.get(j).GetNumber() < list.get(i).GetNumber())
                j++;

            RotateL(applet, i, j, index_m - i);
            i += j - index_m;
            if (j == r) break;
        }
    }

    protected void MergeSort_IPlace(PApplet applet, int l, int r) {
        if (r - l < threshold) {
            Insertion_CheckF(applet, l, r, l + 1);
        } else {
            MergeSort_IPlace(applet, l, (l + r) / 2);
            MergeSort_IPlace(applet, (l + r) / 2, r);
            MergeIP(applet, l, (l + r) / 2, r);
        }
    }

    // 缓存归并排序
    protected void Merge2B(PApplet applet, int l, int r, int step, ArrayList<Integer> buf) {
        for (int i = l; i < r; i += step * 2) {
            int m = i + step;
            int e = Math.min(i + step * 2, r);
            int t = i - l;
            if (m < r) {
                int index_l = i;
                int index_r = m;
                while (index_l < m && index_r < e) {
                    DualColor(applet, i, e, index_l, index_r);
                    if (list.get(index_l).GetNumber() < list.get(index_r).GetNumber())
                        buf.set(t++, list.get(index_l++).GetNumber());
                    else
                        buf.set(t++, list.get(index_r++).GetNumber());
                }

                while (index_l < m) {
                    SoloColor(applet, i, e, index_l);
                    buf.set(t++, list.get(index_l++).GetNumber());
                }

                while (index_r < e) {
                    AsstColor(applet, i, e, index_r);
                    buf.set(t++, list.get(index_r++).GetNumber());
                }
            } else {
                for (int j = i; j < r; j++) {
                    SoloColor(applet, i, r, j);
                    buf.set(j - l, list.get(j).Number);
                }
            }
        }
    }

    protected void Merge2L(PApplet applet, int l, int r, int step, ArrayList<Integer> buf) {
        for (int i = 0; i < r - l; i += step * 2) {
            int m = i + step;
            int e = Math.min(i + step * 2, r - l);
            int t = l + i;
            if (m < r - l) {
                int index_l = i;
                int index_r = m;
                while (index_l < m && index_r < e) {
                    if (buf.get(index_l) < buf.get(index_r))
                        list.set(t, new Node(buf.get(index_l++)));
                    else
                        list.set(t, new Node(buf.get(index_r++)));
                    MainColor(applet, l + i, l + e, t++);
                }

                while (index_l < m) {
                    list.set(t, new Node(buf.get(index_l++)));
                    MainColor(applet, l + i, l + e, t++);
                }

                while (index_r < e) {
                    list.set(t, new Node(buf.get(index_r++)));
                    MainColor(applet, l + i, l + e, t++);
                }
            } else {
                for (int j = i; j < r - l; j++) {
                    list.set(t + j, new Node(buf.get(j)));
                    MainColor(applet, l + i, r, l + j);
                }
            }
        }
    }

    protected void MergeIB(PApplet applet, int l, int r, ArrayList<Integer> buf) {
        int step = 1;
        while (step * 2 < threshold)
            step *= 2;
        boolean temp = true;
        while (step / 2 < r - l) {
            if (step < threshold) {
                for (int i = l; i < r; i += step)
                    Insertion_CheckF(applet, i, Math.min(i + step, r), l + 1);
            } else {
                if (temp) {
                    Merge2B(applet, l, r, step / 2, buf);
                    temp = false;
                } else {
                    Merge2L(applet, l, r, step / 2, buf);
                    temp = true;
                }
            }
            step *= 2;
        }

        if (!temp)
            for (int i = l; i < r; i++) {
                list.set(i, new Node(buf.get(i - l)));
                MainColor(applet, l, r, i);
            }
    }

    protected void MergeWB(PApplet applet, int l, int m, int r, ArrayList<Integer> buf) {
        int length_l = m - l;
        int length_r = r - m;

        if (length_l > buf.size() && length_r > buf.size()) {
            if (length_l >= length_r) {
                int n = l + length_l / 2;
                int k = m + length_r / 2;
                while (!(m < k && k < r && list.get(k - 1).GetNumber() <= list.get(n).GetNumber() && list.get(k).GetNumber() > list.get(n).GetNumber()))
                    k = list.get(k).GetNumber() < list.get(n).GetNumber() ? k + 1 : k - 1;
                RotateL(applet, n, k, m - n);
                MergeWB(applet, l, n, n + k - m, buf);
                MergeWB(applet, n + k - m, k, r, buf);
            } else {
                int n = m + length_r / 2;
                int k = l + length_l / 2;
                while (!(l < k && k < m && list.get(k - 1).GetNumber() <= list.get(n).GetNumber() && list.get(k).GetNumber() > list.get(n).GetNumber()))
                    k = list.get(k).GetNumber() < list.get(n).GetNumber() ? k + 1 : k - 1;
                RotateL(applet, k, n, m - k);
                MergeWB(applet, l, k, k + n - m, buf);
                MergeWB(applet, k + n - m, n, r, buf);
            }
        } else if (length_l <= length_r && length_l <= buf.size()) {
            for (int i = 0; i < length_l; i++)
                buf.set(i, list.get(l + i).GetNumber());
            // 向左归并
            int index_i = 0, index_j = m, index_t = l;
            while (index_i < length_l && index_j < r) {
                if (buf.get(index_i) < list.get(index_j).GetNumber()) {
                    list.set(index_t, new Node(buf.get(index_i++)));
                    MainColorAndAnother(applet, l, r, index_t++, index_j);
                } else {
                    list.set(index_t, new Node(list.get(index_j).GetNumber()));
                    MainColorAndAnother(applet, l, r, index_t++, index_j++);
                }
            }

            while (index_i < length_l) {
                list.set(index_t, new Node(buf.get(index_i++)));
                MainColorAndAnother(applet, l, r, index_t++, index_j);
            }
        } else if (length_r <= length_l && length_r <= buf.size()) {
            for (int i = 0; i < length_r; i++)
                buf.set(i, list.get(m + i).GetNumber());
            // 向右归并
            int index_i = length_r - 1, index_j = m - 1, index_t = r - 1;
            while (index_i >= 0 && index_j >= l) {
                if (buf.get(index_i) > list.get(index_j).GetNumber()) {
                    list.set(index_t, new Node(buf.get(index_i--)));
                    MainColorAndAnother(applet, l, r, index_t--, index_j);
                } else {
                    list.set(index_t, new Node(list.get(index_j).GetNumber()));
                    MainColorAndAnother(applet, l, r, index_t--, index_j--);
                }
            }

            while (index_i >= 0) {
                list.set(index_t, new Node(buf.get(index_i--)));
                MainColor(applet, l, r, index_t--);
            }
        }
    }

    protected void MergeSort_Buffer(PApplet applet, int l, int r) {
        int m = (l + r) / 2;
        ArrayList<Integer> buffer = new ArrayList<>();
        for (int i = 0; i < buffer_size; i++)
            buffer.add(0);

        if (Math.ceil((r - l) / 2.0) > buffer_size) {
            MergeSort_Buffer(applet, l, m);
            MergeSort_Buffer(applet, m, r);
        } else {
            MergeIB(applet, l, m, buffer);
            MergeIB(applet, m, r, buffer);
        }

        MergeWB(applet, l, m, r, buffer);
    }

    // 插入排序
    protected void Insertion_CheckF(PApplet applet, int l, int r, int s) {
        MainColor(applet, l, r, l);
        for (int i = s; i < r; i++) {
            if (list.get(i).GetNumber() < list.get(l).GetNumber()) {
                for (int j = i; j > l; j--) {
                    Exchange(j, j - 1);
                    TripleColor(applet, l, r, l, j, j - 1);
                }
            } else {
                for (int j = i; j > l; j--)
                    if (list.get(j).GetNumber() < list.get(j - 1).GetNumber()) {
                        Exchange(j, j - 1);
                        DualColor(applet, l, r, j, j - 1);
                    } else
                        break;
            }
        }
    }

    @Override
    public void sort(PApplet applet) {
        if (buffer_enable) {
            MergeSort_Buffer(applet, 0, list.size());
        } else {
            MergeSort_IPlace(applet, 0, list.size());
        }
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

    public void MainColorAndAnother(PApplet applet, int l, int r, int m, int n) {
        Prepare(applet, () -> {
            SetColors(l, r, 0xFF00FFFF);
            SetColor(n, 0xFF00FF00, false);
            SetColor(m, 0xFFFF0000, false);
        });
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
