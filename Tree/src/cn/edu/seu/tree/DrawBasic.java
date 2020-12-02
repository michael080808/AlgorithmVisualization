package cn.edu.seu.tree;

import processing.core.PApplet;

import java.util.ArrayList;

public class DrawBasic extends Canvas {
    public Node Insert(PApplet applet, int Number) {
        Path.clear();
        CleanPrepare(applet);
        boolean IsNewN = false;
        Node CurrNo;
        if (Root == null) {
            Root = new Node(Number);
            CurrNo = Root;
            IsNewN = true;
            Path.add(CurrNo);
            OnPathPrepare(applet);
        } else {
            CurrNo = Root;
            Path.add(CurrNo);
            OnPathPrepare(applet);
            while (CurrNo.Number != Number) {
                if (Number < CurrNo.Number) {
                    if (CurrNo.L == null) {
                        CurrNo.L = new Node(Number);
                        IsNewN = true;
                    }
                    CurrNo = CurrNo.L;
                } else {
                    if (CurrNo.R == null) {
                        CurrNo.R = new Node(Number);
                        IsNewN = true;
                    }
                    CurrNo = CurrNo.R;
                }
                Path.add(CurrNo);
                OnPathPrepare(applet);
            }
        }

        if (!IsNewN)
            Path.clear();

        return CurrNo;
    }

    public Node Search(PApplet applet, int Number) {
        Path.clear();
        CleanPrepare(applet);
        Node CurrNo = Root;
        Path.add(CurrNo);

        while (CurrNo != null && CurrNo.Number != Number) {
            if (Number < CurrNo.Number) {
                CurrNo = CurrNo.L;
            } else {
                CurrNo = CurrNo.R;
            }
            Path.add(CurrNo);
            OnPathPrepare(applet);
        }

        if (CurrNo == null)
            Path.clear();

        return CurrNo;
    }

    public Node Remove(PApplet applet, int Number) {
        Node RmovNo = Search(applet, Number);
        Node RplcNo = Delete(applet, RmovNo);

        if (RmovNo != null && RplcNo != null) {
            if (Path.size() > 1) {
                Node RplcPt = Path.get(Path.size() - 2);
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

    protected Node Insert(PApplet applet, Node curr) {
        Path.clear();
        CleanPrepare(applet);
        boolean IsNewN = false;
        Node CurrNo;
        if (Root == null) {
            Root = curr;
            CurrNo = Root;
            IsNewN = true;
            Path.add(CurrNo);
            OnPathPrepare(applet);
        } else {
            CurrNo = Root;
            Path.add(CurrNo);
            while (CurrNo.Number != curr.Number) {
                if (curr.Number < CurrNo.Number) {
                    if (CurrNo.L == null) {
                        CurrNo.L = curr;
                        IsNewN = true;
                    }
                    CurrNo = CurrNo.L;
                } else {
                    if (CurrNo.R == null) {
                        CurrNo.R = curr;
                        IsNewN = true;
                    }
                    CurrNo = CurrNo.R;
                }
                Path.add(CurrNo);
                OnPathPrepare(applet);
            }
        }

        if (!IsNewN)
            Path.clear();

        return CurrNo;
    }

    protected Node Delete(PApplet applet, Node root) {
        if (root != null) {
            while (root.L != null || root.R != null) {
                int second = Path.size() - 1;
                if (root.L != null) {
                    Path.add(root.L);
                    OnPathPrepare(applet, Path.size() - 1, second);
                    while (Path.get(Path.size() - 1).R != null) {
                        Path.add(Path.get(Path.size() - 1).R);
                        OnPathPrepare(applet, Path.size() - 1, second);
                    }
                } else {
                    Path.add(root.R);
                    OnPathPrepare(applet);
                    while (Path.get(Path.size() - 1).L != null) {
                        Path.add(Path.get(Path.size() - 1).L);
                        OnPathPrepare(applet, Path.size() - 1, second);
                    }
                }

                Node swap = Path.get(Path.size() - 1);
                int number = swap.Number;
                swap.Number = root.Number;
                root.Number = number;
                root = swap;
                OnPathPrepare(applet, Path.size() - 1, second);
            }
        }

        return root;
    }

    public ArrayList<Node> LevelTraversal(PApplet applet) {
        return LevelTraversal(applet, Root);
    }

    protected ArrayList<Node> LevelTraversal(PApplet applet, Node root) {
        CleanPrepare(applet);
        ArrayList<Node> Travel = new ArrayList<>();
        if (root != null) {
            int IndexN = 0;
            Travel.add(root);
            while (IndexN < Travel.size()) {
                Node CurrNo = Travel.get(IndexN++);
                if (CurrNo.L != null)
                    Travel.add(CurrNo.L);
                if (CurrNo.R != null)
                    Travel.add(CurrNo.R);
                OnPathPrepare(applet, Travel, IndexN);
            }
        }
        return Travel;
    }

    public ArrayList<Node> Traversal(PApplet applet, TraversalType Type) {
        return Traversal(applet, Type, Root);
    }

    protected ArrayList<Node> Traversal(PApplet applet, TraversalType type, Node root) {
        CleanPrepare(applet);
        ArrayList<Node> Travel = new ArrayList<Node>();
        if (root != null) {
            ArrayList<Node> stackN = new ArrayList<Node>();
            ArrayList<Boolean> stackV = new ArrayList<Boolean>();
            stackN.add(root);
            stackV.add(false);
            while (stackN.size() > 0 && stackV.size() > 0) {
                Node CurrNo = stackN.get(stackN.size() - 1);
                Boolean VisitN = stackV.get(stackV.size() - 1);
                stackN.remove(stackN.size() - 1);
                stackV.remove(stackV.size() - 1);
                if (CurrNo == null) continue;
                if (VisitN) {
                    Travel.add(CurrNo);
                    OnPathPrepare(applet, Travel);
                } else {
                    switch (type) {
                        case Inorder:
                            stackN.add(CurrNo.R);
                            stackV.add(false);
                            stackN.add(CurrNo);
                            stackV.add(true);
                            stackN.add(CurrNo.L);
                            stackV.add(false);
                            break;
                        case Preorder:
                            stackN.add(CurrNo.R);
                            stackV.add(false);
                            stackN.add(CurrNo.L);
                            stackV.add(false);
                            stackN.add(CurrNo);
                            stackV.add(true);
                            break;
                        case Postorder:
                            stackN.add(CurrNo);
                            stackV.add(true);
                            stackN.add(CurrNo.R);
                            stackV.add(false);
                            stackN.add(CurrNo.L);
                            stackV.add(false);
                            break;
                    }
                }
            }
        }
        return Travel;
    }
}
