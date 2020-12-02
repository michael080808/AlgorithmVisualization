package cn.edu.seu.tree;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Canvas extends Structure {
    Lock lock = new ReentrantLock();

    protected enum OperationState {OpNull, Search, Insert, Return, Finish}

    protected OperationState State = OperationState.OpNull;

    public void SetLock(Lock lock) {
        this.lock = lock;
    }

    public float PositionCalc() {
        if (Root != null) {
            ArrayList<ArrayList<Node>> Levels = new ArrayList<>();

            Root.LevelN = 0;
            this.MaxLevel = 0;
            ArrayList<Node> Travel = LevelTraversal(Root);
            for (Node CurrNo : Travel) {
                if (CurrNo.L != null)
                    CurrNo.L.LevelN = CurrNo.LevelN + 1;
                if (CurrNo.R != null)
                    CurrNo.R.LevelN = CurrNo.LevelN + 1;
                this.MaxLevel = Math.max(this.MaxLevel, CurrNo.LevelN);

                if (CurrNo.LevelN >= Levels.size())
                    Levels.add(new ArrayList<>());
                ArrayList<Node> CurrLv = Levels.get(CurrNo.LevelN);
                CurrNo.OrderN = CurrLv.size();
                CurrLv.add(CurrNo);
            }

            for (int i = Levels.size(); i > 0; i--) {
                ArrayList<Node> CurrLv = Levels.get(i - 1);
                for (int j = 0; j < CurrLv.size(); j++) {
                    Node CurrNo = CurrLv.get(j);
                    if (j == 0)
                        CurrNo.Coordinate = 0;
                    else
                        CurrNo.Coordinate = CurrLv.get(j - 1).Coordinate + 2;

                    float Offset;

                    if (CurrNo.R != null && CurrNo.L != null) {
                        if (CurrNo.Coordinate > (CurrNo.L.Coordinate + CurrNo.R.Coordinate) / 2) {
                            Offset = CurrNo.Coordinate + (CurrNo.R.Coordinate - CurrNo.L.Coordinate) / 2 - CurrNo.R.Coordinate;
                            Travel = LevelTraversal(CurrNo.L);
                            for (Node ItemNo : Travel)
                                ItemNo.Coordinate += Offset;
                            Travel = LevelTraversal(CurrNo.R);
                            for (Node ItemNo : Travel)
                                ItemNo.Coordinate += Offset;

                            ArrayList<Node> AnonLv = Levels.get(CurrNo.R.LevelN);
                            for (int p = CurrNo.R.OrderN + 1; p < AnonLv.size(); p++) {
                                Travel = LevelTraversal(AnonLv.get(p));
                                for (Node ItemNo : Travel)
                                    ItemNo.Coordinate += Offset;
                            }
                        } else {
                            Offset = Float.MAX_VALUE;
                            ArrayList<Integer> SubMax = new ArrayList<>();
                            Travel = LevelTraversal(CurrNo.L);
                            for (Node ItemNo : Travel) {
                                if (ItemNo.LevelN - CurrNo.L.LevelN >= SubMax.size())
                                    SubMax.add(ItemNo.OrderN);
                                else
                                    SubMax.set(ItemNo.LevelN - CurrNo.L.LevelN, Math.max(ItemNo.OrderN, SubMax.get(ItemNo.LevelN - CurrNo.L.LevelN)));
                            }

                            for (int p = 0; p < SubMax.size(); p++) {
                                ArrayList<Node> AnonLv = Levels.get(p + CurrNo.L.LevelN);
                                int CurPos = SubMax.get(p);
                                if (CurPos + 1 < AnonLv.size())
                                    Offset = Math.min(Offset, AnonLv.get(CurPos + 1).Coordinate - 2 - AnonLv.get(CurPos).Coordinate);
                            }

                            if (Offset > 0)
                                for (Node ItemNo : Travel)
                                    ItemNo.Coordinate += Offset;

                            CurrNo.Coordinate = (CurrNo.L.Coordinate + CurrNo.R.Coordinate) / 2;
                        }
                    } else if (CurrNo.R != null) {
                        if (CurrNo.Coordinate >= CurrNo.R.Coordinate) {
                            Offset = CurrNo.Coordinate + 1 - CurrNo.R.Coordinate;
                            Travel = LevelTraversal(CurrNo.R);
                            for (Node ItemNo : Travel)
                                ItemNo.Coordinate += Offset;

                            ArrayList<Node> AnonLv = Levels.get(CurrNo.R.LevelN);
                            for (int p = CurrNo.R.OrderN + 1; p < AnonLv.size(); p++) {
                                Travel = LevelTraversal(AnonLv.get(p));
                                for (Node ItemNo : Travel)
                                    ItemNo.Coordinate += Offset;
                            }
                        } else {
                            CurrNo.Coordinate = CurrNo.R.Coordinate - 1;
                        }
                    } else if (CurrNo.L != null) {
                        if (CurrNo.Coordinate <= CurrNo.L.Coordinate)
                            CurrNo.Coordinate = CurrNo.L.Coordinate + 1;
                        else {
                            Offset = CurrNo.Coordinate - 1 - CurrNo.L.Coordinate;
                            Travel = LevelTraversal(CurrNo.L);
                            for (Node ItemNo : Travel)
                                ItemNo.Coordinate += Offset;

                            ArrayList<Node> AnonLv = Levels.get(CurrNo.L.LevelN);
                            for (int p = CurrNo.L.OrderN + 1; p < AnonLv.size(); p++) {
                                Travel = LevelTraversal(AnonLv.get(p));
                                for (Node ItemNo : Travel)
                                    ItemNo.Coordinate += Offset;
                            }
                        }
                    }
                }
            }

            float MinPos = Float.MAX_VALUE;
            float MaxPos = 0;
            Travel = LevelTraversal(Root);
            for (Node ItemNo : Travel) {
                MaxPos = Math.max(MaxPos, ItemNo.Coordinate);
                MinPos = Math.min(MinPos, ItemNo.Coordinate);
            }

            for (Node ItemNo : Travel)
                ItemNo.Coordinate -= MinPos;

            return MaxPos - MinPos;
        } else
            return 0;
    }

    public float CoordinateX(float Offset, float Square, float X) {
        return Offset + (X + 1.0f) / 2.0f * Square;
    }

    public float CoordinateY(float Offset, float Square, float Y) {
        return Offset + (Y + 0.5f) / 1.0f * Square;
    }

    public void DrawTree(PApplet applet, float W, float H) {
        int MaxLvl = this.GetMaxLevel();
        float MaxPos = this.PositionCalc();

        float Unit_W = 2.0f * W / (MaxPos + 2);
        float Unit_H = 1.0f * H / (MaxLvl + 1);
        float Square;
        float Move_W = 0.0f;
        float Move_H = 0.0f;
        if (Unit_W < Unit_H) {
            Square = Unit_W;
            Move_H = (H - (MaxLvl + 1) * Unit_W * 1.0f) / 2;
        } else {
            Square = Unit_H;
            Move_W = (W - (MaxPos + 2) * Unit_H * 0.5f) / 2;
        }
        float Radius = 0.8f * Square;

        ArrayList<Node> Travel = this.LevelTraversal();

        lock.lock();
        for (Structure.Node CurrNo : Travel) {
            applet.stroke(0);
            applet.strokeWeight(1);
            if (CurrNo.GetNodeL() != null) {
                applet.line(CoordinateX(Move_W, Square, CurrNo.GetCoordinate()),
                        CoordinateY(Move_H, Square, CurrNo.GetLevelN()),
                        CoordinateX(Move_W, Square, CurrNo.GetNodeL().GetCoordinate()),
                        CoordinateY(Move_H, Square, CurrNo.GetNodeL().GetLevelN()));
            }

            if (CurrNo.GetNodeR() != null) {
                applet.line(CoordinateX(Move_W, Square, CurrNo.GetCoordinate()),
                        CoordinateY(Move_H, Square, CurrNo.GetLevelN()),
                        CoordinateX(Move_W, Square, CurrNo.GetNodeR().GetCoordinate()),
                        CoordinateY(Move_H, Square, CurrNo.GetNodeR().GetLevelN()));
            }

            applet.fill(CurrNo.GetColorF());
            applet.stroke(CurrNo.GetColorS());
            applet.strokeWeight(CurrNo.GetWeight() > 1 ? Square * 0.1f : 1);
            applet.circle(CoordinateX(Move_W, Square, CurrNo.GetCoordinate()), CoordinateY(Move_H, Square, CurrNo.GetLevelN()), Radius);

            applet.fill(CurrNo.GetColorT());
            applet.textSize(Radius * 0.5f * 0.5f);
            applet.textAlign(applet.CENTER, applet.CENTER);
            applet.text(CurrNo.GetNodeInfo(), CoordinateX(Move_W, Square, CurrNo.GetCoordinate()), CoordinateY(Move_H, Square, CurrNo.GetLevelN()));
        }
        lock.unlock();
    }

    public void Prepare(PApplet applet, Operation operation) {
        applet.noLoop();
        lock.lock();
        operation.exec();
        lock.unlock();
        applet.loop();
        applet.delay(100);
    }

    public void CleanPrepare(PApplet applet) {
        Prepare(applet, this::CleanStrokeColors);
    }

    public void OnPathPrepare(PApplet applet) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            for (int i = 0; i < this.Path.size(); i++) {
                this.Path.get(i).Weight = 20;
                this.Path.get(i).ColorS = i == this.Path.size() - 1 ? 0xFFFF00FF : 0xFF0000FF;
            }
        });
    }

    public void OnPathPrepare(PApplet applet, ArrayList<Node> list) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).Weight = 20;
                list.get(i).ColorS = i == list.size() - 1 ? 0xFFFF00FF : 0xFF0000FF;
            }
        });
    }

    public void OnPathPrepare(PApplet applet, ArrayList<Node> list, int index) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            for (int i = 0; i < index && i < list.size(); i++) {
                list.get(i).Weight = 20;
                list.get(i).ColorS = i == index - 1 ? 0xFFFF00FF : 0xFF0000FF;
            }
        });
    }

    public void OnPathPrepare(PApplet applet, int first, int second) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            for (int i = 0; i < this.Path.size(); i++) {
                this.Path.get(i).Weight = 20;
                this.Path.get(i).ColorS = i == first ? 0xFFFF00FF : (i == second ? 0xFFFF7F00 : 0xFF0000FF);
            }
        });
    }

    public void SubTreeColor(Node root, int color) {
        if (root != null) {
            ArrayList<Node> list = LevelTraversal(root);
            for (Node curr : list) {
                curr.Weight = 20;
                curr.ColorS = color;
            }
        }
    }

    public void RotateLLPrepare(PApplet applet, Node root) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            root.Weight = 20;
            root.ColorS = 0xFFFF00FF;
            root.R.Weight = 20;
            root.R.ColorS = 0xFFFF7F00;

            SubTreeColor(root.L, 0xFF00FF00);
            SubTreeColor(root.R.L, 0xFF00FFFF);
            SubTreeColor(root.R.R, 0xFF0000FF);
        });
    }

    public void RotateRRPrepare(PApplet applet, Node root) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            root.Weight = 20;
            root.ColorS = 0xFFFF00FF;
            root.L.Weight = 20;
            root.L.ColorS = 0xFFFF7F00;

            SubTreeColor(root.R, 0xFF00FF00);
            SubTreeColor(root.L.L, 0xFF0000FF);
            SubTreeColor(root.L.R, 0xFF00FFFF);
        });
    }

    public void RotateLRPrepare(PApplet applet, Node root) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            root.Weight = 20;
            root.ColorS = 0xFFFF00FF;
            root.L.Weight = 20;
            root.L.ColorS = 0xFFFF7F00;
            root.L.R.Weight = 20;
            root.L.R.ColorS = 0xFF7F00FF;

            SubTreeColor(root.R, 0xFF00FF00);
            SubTreeColor(root.L.L, 0xFF0000FF);
            SubTreeColor(root.L.R.L, 0xFF00FFFF);
            SubTreeColor(root.L.R.R, 0xFFFFFF00);
        });
    }

    public void RotateRLPrepare(PApplet applet, Node root) {
        Prepare(applet, () -> {
            CleanStrokeColors();
            root.Weight = 20;
            root.ColorS = 0xFFFF00FF;
            root.R.Weight = 20;
            root.R.ColorS = 0xFFFF7F00;
            root.R.L.Weight = 20;
            root.R.L.ColorS = 0xFF7F00FF;

            SubTreeColor(root.L, 0xFF00FF00);
            SubTreeColor(root.R.R, 0xFF0000FF);
            SubTreeColor(root.R.L.L, 0xFFFFFF00);
            SubTreeColor(root.R.L.R, 0xFF00FFFF);
        });
    }

    public void CleanStrokeColors() {
        ArrayList<Node> Travel = this.LevelTraversal();
        for (Structure.Node CurrNo : Travel) {
            CurrNo.Weight = 1;
            CurrNo.ColorS = 0xFF000000;
        }
    }
}
