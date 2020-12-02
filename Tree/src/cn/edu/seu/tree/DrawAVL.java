package cn.edu.seu.tree;

import processing.core.PApplet;

import java.util.ArrayList;

public class DrawAVL extends DrawBasic {
    public static class Node extends Structure.Node {
        protected int DepthN = 0;

        public Node(int n) {
            super(n);
        }

        public Node(int n, int f, int s, int t) {
            super(n, f, s, t);
        }

        public int GetDepthN() {
            return this.DepthN;
        }

        @Override
        public String GetNodeInfo() {
            return this.Number + "\nH: " + this.DepthN;
        }
    }

    protected Integer MaxDepth = 0;

    public Integer GetMaxDepth() {
        return this.MaxDepth;
    }

    @Override
    public Node Insert(PApplet applet, int Number) {
        Node CurrRt = (Node) super.Insert(applet, new Node(Number));
        Update(applet);
        return CurrRt;
    }

    @Override
    public Node Remove(PApplet applet, int Number) {
        Node RmovNo = (Node) super.Remove(applet, Number);
        Update(applet);
        return RmovNo;
    }

    private void Update(PApplet applet) {
        while (Path.size() > 0) {
            OnPathPrepare(applet);
            Node TempNo = null;
            Node CurrNo = (Node) Path.get(Path.size() - 1);

            int DepthL = 0, DepthR = 0;
            int SubDtL = 0, SubDtR = 0;

            if (CurrNo.L != null)
                DepthL = ((Node) CurrNo.L).DepthN;
            if (CurrNo.R != null)
                DepthR = ((Node) CurrNo.R).DepthN;

            if (DepthL > DepthR && DepthL - DepthR > 1) {
                if (CurrNo.L != null && CurrNo.L.L != null)
                    SubDtL = ((Node) CurrNo.L.L).DepthN;
                if (CurrNo.L != null && CurrNo.L.R != null)
                    SubDtR = ((Node) CurrNo.L.R).DepthN;

                if (SubDtL < SubDtR) {
                    RotateLRPrepare(applet, CurrNo);
                    CurrNo.L = RotateLL(CurrNo.L);
                    Prepare(applet, () -> {
                    });
                    TempNo = RotateRR(CurrNo);
                } else {
                    RotateRRPrepare(applet, CurrNo);
                    TempNo = RotateRR(CurrNo);
                }

            }

            if (DepthR > DepthL && DepthR - DepthL > 1) {
                if (CurrNo.R != null && CurrNo.R.L != null)
                    SubDtL = ((Node) CurrNo.R.L).DepthN;
                if (CurrNo.R != null && CurrNo.R.R != null)
                    SubDtR = ((Node) CurrNo.R.R).DepthN;

                if (SubDtR < SubDtL) {
                    RotateRLPrepare(applet, CurrNo);
                    CurrNo.R = RotateRR(CurrNo.R);
                    Prepare(applet, () -> {
                    });
                    TempNo = RotateLL(CurrNo);
                } else {
                    RotateLLPrepare(applet, CurrNo);
                    TempNo = RotateLL(CurrNo);
                }
            }

            if (TempNo != null) {
                if (Path.size() > 1) {
                    Node CurrPt = (Node) Path.get(Path.size() - 2);
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        CurrPt.L = TempNo;
                    } else {
                        CurrPt.R = TempNo;
                    }
                } else {
                    Root = TempNo;
                }
                TempNo.Weight = 20;
            }

            DepthsUpdate();
            Prepare(applet, () -> {
            });
            Path.remove(Path.size() - 1);
        }
    }

    private void DepthsUpdate() {
        ArrayList<Structure.Node> Travel = LevelTraversal();
        for (int i = Travel.size(); i > 0; i--) {
            Node CurrNo = (Node) Travel.get(i - 1);
            int LoopDL = CurrNo.L != null ? ((Node) CurrNo.L).DepthN : 0;
            int LoopDR = CurrNo.R != null ? ((Node) CurrNo.R).DepthN : 0;
            CurrNo.DepthN = Math.max(LoopDL, LoopDR) + 1;
        }

        if (Root != null)
            this.MaxDepth = ((Node) Root).DepthN;
        else
            this.MaxDepth = 0;
    }

    protected Node RotateLL(Node root) {
        return (Node) super.RotateLL(root);
    }

    protected Node RotateRR(Node root) {
        return (Node) super.RotateRR(root);
    }

    protected Node RotateLR(Node root) {
        root.L = RotateLL(root.L);
        return RotateRR(root);
    }

    protected Node RotateRL(Node root) {
        root.R = RotateRR(root.R);
        return RotateLL(root);
    }
}
