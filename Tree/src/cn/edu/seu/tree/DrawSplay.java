package cn.edu.seu.tree;

import processing.core.PApplet;

public class DrawSplay extends DrawBasic {
    @Override
    public Node Insert(PApplet applet, int Number) {
        Node CurrRt = super.Insert(applet, Number);
        Update(applet);
        return CurrRt;
    }

    @Override
    public Node Search(PApplet applet, int Number) {
        Node CurrRt = super.Search(applet, Number);
        Update(applet);
        return CurrRt;
    }

    @Override
    public Node Remove(PApplet applet, int Number) {
        this.Search(applet, Number);
        return super.Remove(applet, Number);
    }

    private void Update(PApplet applet) {
        if (Path.size() > 0) {
            Node CurrNo = Path.get(Path.size() - 1);
            for (int i = Path.size() - 1; i > 0; i--) {
                Node CurrPt = Path.get(i - 1);
                Node PrevNo = Path.get(i);

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

                if (i > 1) {
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        RotateRRPrepare(applet, CurrPt);
                        CurrNo = RotateRR(CurrPt);
                    } else {
                        RotateLLPrepare(applet, CurrPt);
                        CurrNo = RotateLL(CurrPt);
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
            }
        }
    }
}
