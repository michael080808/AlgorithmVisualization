package cn.edu.seu.tree;

import processing.core.PApplet;

public class DrawTreeHeap extends DrawBasic {
    public static class Node extends Structure.Node {
        protected double RdRank = 0;

        public Node(int n) {
            super(n);
        }

        public Node(int n, int f, int s, int t) {
            super(n, f, s, t);
        }

        public double GetRdRank() {
            return this.RdRank;
        }

        @Override
        public String GetNodeInfo() {
            return this.Number + "\n" + String.format("%.3f", this.RdRank);
        }
    }

    @Override
    public Node Insert(PApplet applet, int Number) {
        Node CurrNo = (Node) super.Insert(applet, new Node(Number));
        if (Path.size() > 0)
            CurrNo.RdRank = Math.random();
        OnPathPrepare(applet);
        Update(applet);
        return CurrNo;
    }

    private void Update(PApplet applet) {
        Node CurrNo = (Node) Path.get(Path.size() - 1);
        for (int i = Path.size() - 1; i > 0; i--) {
            Node CurrPt = (Node) Path.get(i - 1);
            Node PrevNo = (Node) Path.get(i);

            if (CurrPt.L != CurrNo && CurrPt.R != CurrNo) {
                if (CurrPt.L != null && PrevNo == CurrPt.L) {
                    CurrPt.L = CurrNo;
                } else {
                    CurrPt.R = CurrNo;
                }
                Prepare(applet, () -> {
                });
            }

            OnPathPrepare(applet, Path, i + 1);

            if (CurrPt.RdRank < CurrNo.RdRank) {
                if (i > 1) {
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        RotateRRPrepare(applet, CurrPt);
                        CurrNo = (Node) RotateRR(CurrPt);
                    } else {
                        RotateLLPrepare(applet, CurrPt);
                        CurrNo = (Node) RotateLL(CurrPt);
                    }
                } else {
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        RotateRRPrepare(applet, CurrPt);
                        Root = RotateRR(CurrPt);
                    } else {
                        RotateLLPrepare(applet, CurrPt);
                        Root = RotateLL(CurrPt);
                    }
                }
            } else break;
        }
    }
}
