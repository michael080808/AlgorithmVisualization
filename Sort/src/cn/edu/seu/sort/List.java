package cn.edu.seu.sort;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class List {
    public static class Node {
        protected int Number;

        protected int ColorF = 0xFFFFFFFF;

        protected boolean Reference = false;

        public Node(int n) {
            this.Number = n;
        }

        public Node(int n, int c) {
            this.Number = n;
            this.ColorF = c;
        }

        public int GetNumber() {
            return this.Number;
        }

        public int GetColorF() {
            return this.ColorF;
        }

        public boolean GetReference() {
            return this.Reference;
        }
    }

    public enum ListType {RANDOM, INORDER, REVERSE, PART_ORD, PART_REV}

    protected final ArrayList<Node> list = new ArrayList<>();

    private Lock lock = new ReentrantLock();

    protected void Exchange(int index1, int index2) {
        Node P = list.get(index1);
        Node Q = list.get(index2);
        list.set(index1, Q);
        list.set(index2, P);
    }

    protected void RandomExchange() {
        int index1 = 0, index2 = 0;
        while (index1 == index2) {
            index1 = (int) (Math.random() * list.size());
            index2 = (int) (Math.random() * list.size());
        }
        Exchange(index1, index2);
    }

    public List(int n, ListType t) {
        SetList(n, t);
    }

    public void SetList(int n, ListType t) {
        list.clear();

        ArrayList<Node> temp = new ArrayList<>();
        for (int i = 0; i < n; i++)
            temp.add(new Node(i + 1));

        switch (t) {
            case RANDOM:
                while (temp.size() > 0)
                    list.add(temp.remove((int) (Math.random() * temp.size())));
                break;
            case INORDER:
                while (temp.size() > 0)
                    list.add(temp.remove(0));
                break;
            case REVERSE:
                while (temp.size() > 0)
                    list.add(temp.remove(temp.size() - 1));
                break;
            case PART_ORD:
                while (temp.size() > 0)
                    list.add(temp.remove(0));
                RandomExchange();
            case PART_REV:
                while (temp.size() > 0)
                    list.add(temp.remove(temp.size() - 1));
                RandomExchange();
                break;
        }
    }

    public void SetLock(Lock lock) {
        this.lock = lock;
    }

    public boolean SetColor(int index, int color, boolean reference) {
        if (index < list.size()) {
            list.get(index).ColorF = color;
            list.get(index).Reference = reference;
        }
        return index < list.size();
    }

    public boolean SetColors(int index1, int index2, int color) {
        if (index1 < list.size() && index2 <= list.size() && index1 < index2)
            for (int i = index1; i < index2; i++)
                list.get(i).ColorF = color;
        return index1 < list.size() && index2 <= list.size() && index1 < index2;
    }

    public void Clear() {
        for (Node curr : list) {
            curr.ColorF = 0xFFFFFFFF;
            curr.Reference = false;
        }
    }

    public void Draw(PApplet applet) {
        lock.lock();
        float unit_w = applet.width / (list.size() * 1.0f);
        float unit_h = applet.height / (list.size() * 1.0f);

        for (int i = 0; i < list.size(); i++) {
            applet.stroke(0);
            applet.fill(list.get(i).ColorF);
            applet.rect(i * unit_w, applet.height - list.get(i).Number * unit_h, unit_w, list.get(i).Number * unit_h);
        }

        for (Node node : list) {
            if (node.Reference) {
                applet.stroke(node.ColorF);
                applet.line(0, applet.height - node.Number * unit_h, applet.width, applet.height - node.Number * unit_h);
            }
        }

        lock.unlock();
    }

    public void Prepare(PApplet applet, Operation operation) {
        applet.noLoop();
        lock.lock();
        Clear();
        operation.exec();
        lock.unlock();
        applet.loop();
        applet.delay(100);
    }
}
