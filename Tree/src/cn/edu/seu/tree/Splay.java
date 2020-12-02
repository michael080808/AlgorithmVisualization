package cn.edu.seu.tree;

public class Splay extends Structure {
    @Override
    public Node Insert(int Number) {
        Node CurrRt = super.Insert(Number);
        Update();
        return CurrRt;
    }

    @Override
    public Node Search(int Number) {
        Node CurrRt = super.Search(Number);
        Update();
        return CurrRt;
    }

    @Override
    public Node Remove(int Number) {
        this.Search(Number);
        return super.Remove(Number);
    }

    private void Update() {
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
                }

                if (i > 1) {
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        CurrNo = RotateRR(CurrPt);
                    } else {
                        CurrNo = RotateLL(CurrPt);
                    }
                } else {
                    if (CurrPt.L != null && CurrNo == CurrPt.L) {
                        Root = RotateRR(CurrPt);
                    } else {
                        Root = RotateLL(CurrPt);
                    }
                }
            }
        }
    }
}
