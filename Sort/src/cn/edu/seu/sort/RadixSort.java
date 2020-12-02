package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;

public class RadixSort extends List implements Sort {
    private final ArrayList<Integer> RadixColors = new ArrayList<>();

    public RadixSort(int n, ListType t) {
        super(n, t);
    }

    @Override
    public void sort() {

    }

    protected void RadixSort_LSD(PApplet applet, int l, int r) {
        ArrayList<ArrayList<Node>> Bucket = new ArrayList<>();
        RadixColors(16);
        boolean Continue = true;
        for (int i = 0; i < 16; i++)
            Bucket.add(new ArrayList<>());

        for (int i = 0; i < 32 && Continue; i += 4) {
            Continue = false;
            for (ArrayList<Node> CurrBt : Bucket)
                CurrBt.clear();
            for (int j = 0; j < list.size(); j++) {
                int Partition = list.get(j).GetNumber() >> i;
                Continue |= (Partition > 0);
                RadixColors(applet, Bucket, j);
                Bucket.get(Partition & 0x0000000F).add(list.get(j));
            }
            list.clear();
            for (ArrayList<Node> CurrBt : Bucket)
                list.addAll(CurrBt);
            RadixColors(applet, Bucket);
        }
    }

    @Override
    public void sort(PApplet applet) {
        RadixSort_LSD(applet, 0, list.size());
    }

    protected void RadixColors(int number) {
        RadixColors.clear();
        for (int i = 0; i < number; i++)
            RadixColors.add((int) (0xFF800000 + Math.random() * 0x00800000));
    }

    protected void RadixColors(PApplet applet, ArrayList<ArrayList<Node>> bucket) {
        Prepare(applet, () -> {
            for (int i = 0; i < 16; i++)
                for (Node CurrNo : bucket.get(i))
                    CurrNo.ColorF = RadixColors.get(i);
        });
    }

    protected void RadixColors(PApplet applet, ArrayList<ArrayList<Node>> bucket, int m) {
        Prepare(applet, () -> {
            for (int i = 0; i < 16; i++)
                for (Node CurrNo : bucket.get(i))
                    CurrNo.ColorF = RadixColors.get(i);
            SetColor(m, 0xFFFF0000, false);
        });
    }
}
