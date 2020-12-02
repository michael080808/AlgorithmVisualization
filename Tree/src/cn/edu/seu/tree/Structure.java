package cn.edu.seu.tree;

import java.util.ArrayList;

public class Structure {
    public static class Node {
        protected int Number;

        protected int LevelN = 0;
        protected int OrderN = 0;

        protected int ColorF = 0xFFFFFFFF;
        protected int ColorS = 0x00000000;
        protected int ColorT = 0x00000000;
        protected int Weight = 1;

        protected Node L = null;
        protected Node R = null;

        protected float Coordinate = 0.0f;

        public Node(int n) {
            this.Number = n;
        }

        public Node(int n, int f, int s, int t) {
            this.Number = n;
            this.ColorF = f;
            this.ColorS = s;
            this.ColorT = t;
        }

        public int GetNumber() {
            return this.Number;
        }

        public int GetLevelN() {
            return this.LevelN;
        }

        public int GetOrderN() {
            return this.OrderN;
        }

        public int GetColorF() {
            return this.ColorF;
        }

        public int GetColorS() {
            return this.ColorS;
        }

        public int GetColorT() {
            return this.ColorT;
        }

        public int GetWeight() {
            return this.Weight;
        }

        public void SetColorF(int f) {
            this.ColorF = f;
        }

        public void SetColorS(int s) {
            this.ColorS = s;
        }

        public void SetColorT(int t) {
            this.ColorT = t;
        }

        public void SetWeight(int w) {
            this.Weight = w;
        }

        public Node GetNodeL() {
            return this.L;
        }

        public Node GetNodeR() {
            return this.R;
        }

        public float GetCoordinate() {
            return this.Coordinate;
        }

        public String GetNodeInfo() {
            return String.valueOf(this.Number);
        }
    }

    public enum TraversalType {Inorder, Preorder, Postorder}

    protected Integer MaxLevel = 0;

    protected Node Root = null;

    protected ArrayList<Node> Path = new ArrayList<>();

    public Integer GetMaxLevel() {
        return this.MaxLevel;
    }

    public Node Insert(int Number) {
        Path.clear();
        boolean IsNewN = false;
        Node CurrNo;
        if (Root == null) {
            Root = new Node(Number);
            CurrNo = Root;
            IsNewN = true;
            Path.add(CurrNo);
        } else {
            CurrNo = Root;
            Path.add(CurrNo);
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
            }
        }

        if (!IsNewN)
            Path.clear();

        return CurrNo;
    }

    public Node Search(int Number) {
        Path.clear();

        Node CurrNo = Root;
        Path.add(CurrNo);

        while (CurrNo != null && CurrNo.Number != Number) {
            if (Number < CurrNo.Number) {
                CurrNo = CurrNo.L;
            } else {
                CurrNo = CurrNo.R;
            }
            Path.add(CurrNo);
        }

        if (CurrNo == null)
            Path.clear();

        return CurrNo;
    }

    public Node Remove(int Number) {
        Node RmovNo = Search(Number);
        Node RplcNo = Delete(RmovNo);

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

    protected Node Insert(Node curr) {
        Path.clear();
        boolean IsNewN = false;
        Node CurrNo;
        if (Root == null) {
            Root = curr;
            CurrNo = Root;
            IsNewN = true;
            Path.add(CurrNo);
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
            }
        }

        if (!IsNewN)
            Path.clear();

        return CurrNo;
    }

    protected Node Delete(Node root) {
        if (root != null) {
            while (root.L != null || root.R != null) {
                if (root.L != null) {
                    Path.add(root.L);
                    while (Path.get(Path.size() - 1).R != null)
                        Path.add(Path.get(Path.size() - 1).R);
                } else {
                    Path.add(root.R);
                    while (Path.get(Path.size() - 1).L != null)
                        Path.add(Path.get(Path.size() - 1).L);
                }

                Node swap = Path.get(Path.size() - 1);
                int number = swap.Number;
                swap.Number = root.Number;
                root.Number = number;
                root = swap;
            }
        }

        return root;
    }

    public ArrayList<Node> LevelTraversal() {
        return LevelTraversal(Root);
    }

    protected ArrayList<Node> LevelTraversal(Node root) {
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
            }
        }
        return Travel;
    }

    public ArrayList<Node> Traversal(TraversalType Type) {
        return Traversal(Type, Root);
    }

    protected ArrayList<Node> Traversal(TraversalType type, Node root) {
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
                if (VisitN)
                    Travel.add(CurrNo);
                else {
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

    protected Node RotateLL(Node root) {
        Node curr = root.R;
        root.R = curr.L;
        curr.L = root;
        return curr;
    }

    protected Node RotateRR(Node root) {
        Node curr = root.L;
        root.L = curr.R;
        curr.R = root;
        return curr;
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
