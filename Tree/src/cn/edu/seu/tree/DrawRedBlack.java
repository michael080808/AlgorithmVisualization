package cn.edu.seu.tree;

import processing.core.PApplet;

public class DrawRedBlack extends DrawBasic {
    @Override
    public Node Insert(PApplet applet, int Number) {
        Node curr = super.Insert(applet, Number);
        if (Path.size() > 0) {
            curr.ColorF = 0xFFFF0000;
            curr.ColorT = 0xFFFFFFFF;
        }
        InsertUpdate(applet);
        return curr;
    }

    @Override
    public Node Remove(PApplet applet, int Number) {
        Node RmovNo = super.Search(applet, Number);
        Node RplcNo = super.Delete(applet, RmovNo);
        Node RplcPt = Path.size() > 1 ? Path.get(Path.size() - 2) : null;

        if (RmovNo != null && RplcNo != null) {
            RemoveUpdate(applet);
            if (RplcPt != null) {
                if (RplcPt.L != null && RplcNo == RplcPt.L)
                    RplcPt.L = null;
                if (RplcPt.R != null && RplcNo == RplcPt.R)
                    RplcPt.R = null;
            } else {
                Root = null;
            }
        }

        return RmovNo;
    }

    private void InsertUpdate(PApplet applet) {
        if (Root != null) {
            Node CurrGG;
            Node TempGP = null;

            while (Path.size() > 2) {
                OnPathPrepare(applet);
                Node CurrGP = Path.get(Path.size() - 3);
                Node CurrPt = Path.get(Path.size() - 2);
                Node CurrNo = Path.get(Path.size() - 1);
                Node CurrUn = CurrPt == CurrGP.L ? CurrGP.R : CurrGP.L;

                if (CurrPt.ColorF == 0xFFFF0000 && (CurrUn != null && CurrUn.ColorF == 0xFFFF0000)) {
                    CurrPt.ColorF = 0xFF000000;
                    CurrUn.ColorF = 0xFF000000;
                    CurrGP.ColorF = 0xFFFF0000;
                    Path.remove(Path.size() - 1);
                    Path.remove(Path.size() - 1);
                } else {
                    if (CurrPt.ColorF == 0xFFFF0000 && (CurrUn == null || CurrUn.ColorF == 0xFF000000)) {
                        if ((CurrGP.L != null && CurrGP.L == CurrPt) && (CurrPt.L != null && CurrPt.L == CurrNo)) {
                            RotateRRPrepare(applet, CurrGP);
                            TempGP = InsertRotateRR(CurrGP);
                        }
                        if ((CurrGP.L != null && CurrGP.L == CurrPt) && (CurrPt.R != null && CurrPt.R == CurrNo)) {
                            RotateLRPrepare(applet, CurrGP);
                            CurrGP.L = super.RotateLL(CurrGP.L);
                            Prepare(applet, () -> {
                            });
                            TempGP = InsertRotateRR(CurrGP);
                        }
                        if ((CurrGP.R != null && CurrGP.R == CurrPt) && (CurrPt.L != null && CurrPt.L == CurrNo)) {
                            RotateRLPrepare(applet, CurrGP);
                            CurrGP.R = super.RotateRR(CurrGP.R);
                            Prepare(applet, () -> {
                            });
                            TempGP = InsertRotateLL(CurrGP);
                        }
                        if ((CurrGP.R != null && CurrGP.R == CurrPt) && (CurrPt.R != null && CurrPt.R == CurrNo)) {
                            RotateLLPrepare(applet, CurrGP);
                            TempGP = InsertRotateLL(CurrGP);
                        }

                        if (Path.size() > 3) {
                            CurrGG = Path.get(Path.size() - 4);
                            if (CurrGG.L != null && CurrGP == CurrGG.L)
                                CurrGG.L = TempGP;
                            else
                                CurrGG.R = TempGP;
                        } else {
                            Root = TempGP;
                        }
                        Prepare(applet, () -> {
                        });
                    }
                    Path.clear();
                }
            }

            Root.ColorF = 0xFF000000;
            Root.ColorT = 0xFFFFFFFF;
        }
    }

    private void RemoveUpdate(PApplet applet) {
        while (Path.size() > 1) {
            OnPathPrepare(applet);
            if (Path.get(Path.size() - 1).ColorF == 0xFF000000) {
                Node CurrNo = Path.get(Path.size() - 1);
                Node CurrPt = Path.get(Path.size() - 2);
                if (CurrPt.L != null && CurrNo == CurrPt.L) {
                    Node CurrBr = CurrPt.R;
                    if (CurrBr != null && CurrBr.ColorF == 0xFFFF0000) {
                        RotateLLPrepare(applet, CurrPt);
                        if (Path.size() > 2) {
                            Node CurrGP = Path.get(Path.size() - 3);
                            Node TempPt = InsertRotateLL(CurrPt);
                            if (CurrGP.L != null && CurrPt == CurrGP.L)
                                CurrGP.L = TempPt;
                            else
                                CurrGP.R = TempPt;
                            Path.set(Path.size() - 2, TempPt);
                            Path.set(Path.size() - 1, TempPt.L);
                            Path.add(CurrNo);
                        } else {
                            Root = InsertRotateLL(CurrPt);
                            Path.set(0, Root);
                            Path.set(1, Root.L);
                            Path.add(CurrNo);
                        }
                        Prepare(applet, () -> {
                        });
                    } else {
                        if (CurrBr != null && (CurrBr.R != null && CurrBr.R.ColorF == 0xFFFF0000)) {
                            RotateLLPrepare(applet, CurrPt);
                            if (Path.size() > 2) {
                                Node CurrGP = Path.get(Path.size() - 3);
                                if (CurrPt.Number < CurrGP.Number)
                                    CurrGP.L = RemoveRotateLL(CurrPt);
                                else
                                    CurrGP.R = RemoveRotateLL(CurrPt);
                            } else Root = RemoveRotateLL(CurrPt);
                            Prepare(applet, () -> {
                            });
                            Path.clear();
                        } else if (CurrBr != null && ((CurrBr.R == null || CurrBr.R.ColorF == 0xFF000000) && (CurrBr.L != null && CurrBr.L.ColorF == 0xFFFF0000))) {
                            RotateRLPrepare(applet, CurrPt);
                            CurrPt.R.ColorF = 0xFFFF0000;
                            CurrPt.R = super.RotateRR(CurrPt.R);
                            CurrPt.R.ColorF = 0xFF000000;
                            Prepare(applet, () -> {
                            });
                            if (Path.size() > 2) {
                                Node CurrGP = Path.get(Path.size() - 3);
                                if (CurrPt.Number < CurrGP.Number)
                                    CurrGP.L = RemoveRotateLL(CurrPt);
                                else
                                    CurrGP.R = RemoveRotateLL(CurrPt);
                            } else Root = RemoveRotateLL(CurrPt);
                            Prepare(applet, () -> {
                            });
                            Path.clear();
                        } else if (CurrBr == null || ((CurrBr.R == null || CurrBr.R.ColorF == 0xFF000000) && (CurrBr.L == null || CurrBr.L.ColorF == 0xFF000000))) {
                            if (CurrBr != null)
                                CurrBr.ColorF = 0xFFFF0000;
                            Prepare(applet, () -> {
                            });
                            Path.remove(Path.size() - 1);
                        }
                    }
                } else {
                    Node CurrBr = CurrPt.L;
                    if (CurrBr != null && CurrBr.ColorF == 0xFFFF0000) {
                        RotateRRPrepare(applet, CurrPt);
                        if (Path.size() > 2) {
                            Node CurrGP = Path.get(Path.size() - 3);
                            Node TempPt = InsertRotateRR(CurrPt);
                            if (CurrGP.L != null && CurrPt == CurrGP.L)
                                CurrGP.L = TempPt;
                            else
                                CurrGP.R = TempPt;
                            Path.set(Path.size() - 2, CurrPt);
                            Path.set(Path.size() - 1, CurrPt.R);
                            Path.add(CurrNo);
                        } else {
                            Root = InsertRotateRR(CurrPt);
                            Path.set(0, Root);
                            Path.set(1, Root.R);
                            Path.add(CurrNo);
                        }
                        Prepare(applet, () -> {
                        });
                    } else {
                        if (CurrBr != null && (CurrBr.L != null && CurrBr.L.ColorF == 0xFFFF0000)) {
                            RotateRRPrepare(applet, CurrPt);
                            if (Path.size() > 2) {
                                Node CurrGP = Path.get(Path.size() - 3);
                                if (CurrPt.Number < CurrGP.Number)
                                    CurrGP.L = RemoveRotateRR(CurrPt);
                                else
                                    CurrGP.R = RemoveRotateRR(CurrPt);
                            } else Root = RemoveRotateRR(CurrPt);
                            Prepare(applet, () -> {
                            });
                            Path.clear();
                        } else if (CurrBr != null && ((CurrBr.L == null || CurrBr.L.ColorF == 0xFF000000) && (CurrBr.R != null && CurrBr.R.ColorF == 0xFFFF0000))) {
                            RotateLRPrepare(applet, CurrPt);
                            CurrPt.L.ColorF = 0xFFFF0000;
                            CurrPt.L = super.RotateLL(CurrPt.L);
                            CurrPt.L.ColorF = 0xFF000000;
                            Prepare(applet, () -> {
                            });
                            if (Path.size() > 2) {
                                Node CurrGP = Path.get(Path.size() - 3);
                                if (CurrPt.Number < CurrGP.Number)
                                    CurrGP.L = RemoveRotateRR(CurrPt);
                                else
                                    CurrGP.R = RemoveRotateRR(CurrPt);
                            } else Root = RemoveRotateRR(CurrPt);
                            Prepare(applet, () -> {
                            });
                            Path.clear();
                        } else if (CurrBr == null || ((CurrBr.L == null || CurrBr.L.ColorF == 0xFF000000) && (CurrBr.R == null || CurrBr.R.ColorF == 0xFF000000))) {
                            if (CurrBr != null)
                                CurrBr.ColorF = 0xFFFF0000;
                            Prepare(applet, () -> {
                            });
                            Path.remove(Path.size() - 1);
                        }
                    }
                }
            } else {
                Path.get(Path.size() - 1).ColorF = 0xFF000000;
                Prepare(applet, () -> {
                });
                Path.clear();
            }
        }

        if (Root != null)
            Root.ColorF = 0xFF000000;
        Prepare(applet, () -> {
        });
    }

    protected Node InsertRotateLL(Node root) {
        Node curr = super.RotateLL(root);
        root.ColorF = 0xFFFF0000;
        curr.ColorF = 0xFF000000;
        return curr;
    }

    protected Node InsertRotateRR(Node root) {
        Node curr = super.RotateRR(root);
        root.ColorF = 0xFFFF0000;
        curr.ColorF = 0xFF000000;
        return curr;
    }

    protected Node InsertRotateLR(Node root) {
        root.L = super.RotateLL(root.L);
        return this.InsertRotateRR(root);
    }

    protected Node InsertRotateRL(Node root) {
        root.R = super.RotateRR(root.R);
        return this.InsertRotateLL(root);
    }

    protected Node RemoveRotateLL(Node root) {
        Node curr = super.RotateLL(root);
        curr.ColorF = root.ColorF;
        root.ColorF = 0xFF000000;
        if (curr.R != null)
            curr.R.ColorF = 0xFF000000;
        return curr;
    }

    protected Node RemoveRotateRR(Node root) {
        Node curr = super.RotateRR(root);
        curr.ColorF = root.ColorF;
        root.ColorF = 0xFF000000;
        if (curr.L != null)
            curr.L.ColorF = 0xFF000000;
        return curr;
    }

    protected Node RemoveRotateLR(Node root) {
        root.L.ColorF = 0xFFFF0000;
        root.L = super.RotateLL(root.L);
        root.L.ColorF = 0xFF000000;
        return this.RemoveRotateRR(root);
    }

    protected Node RemoveRotateRL(Node root) {
        root.R.ColorF = 0xFFFF0000;
        root.R = super.RotateRR(root.R);
        root.R.ColorF = 0xFF000000;
        return this.RemoveRotateLL(root);
    }
}
