package cn.edu.seu;

import com.hamoid.*;
import cn.edu.seu.tree.*;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends PApplet {
    public int size = 50;

    public boolean finish = false;

    public ArrayList<Integer> insert = new ArrayList<>();

    public ArrayList<Integer> remove = new ArrayList<>();

    public DrawRedBlack tree = new DrawRedBlack();

    public VideoExport export;

    public Lock lock = new ReentrantLock();

    @Override
    public void settings() {
        // this.size(800, 600);
        this.fullScreen();
    }

    @Override
    public void setup() {
        tree.SetLock(lock);

        export = new VideoExport(this, "RedBlackTree.mp4");
        export.startMovie();

        this.surface.setResizable(true);

        for (int i = 0; i < size; i++) {
            insert.add(i + 1);
            remove.add(i + 1);
        }

        int Rand_i, Last_N, Rand_N;
        for (int i = 0; i < size; i++) {
            Rand_i = (int) (Math.random() * size);
            Rand_N = insert.get(Rand_i);
            Last_N = insert.get(insert.size() - 1);
            insert.set(Rand_i, Last_N);
            insert.set(insert.size() - 1, Rand_N);

            Rand_i = (int) (Math.random() * size);
            Rand_N = remove.get(Rand_i);
            Last_N = remove.get(remove.size() - 1);
            remove.set(Rand_i, Last_N);
            remove.set(remove.size() - 1, Rand_N);
        }

        ExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.submit(() -> {
            for (Integer integer : insert)
                tree.Insert(this, integer);

            tree.LevelTraversal(this);

            tree.Traversal(this, Structure.TraversalType.Inorder);
            tree.Traversal(this, Structure.TraversalType.Preorder);
            tree.Traversal(this, Structure.TraversalType.Postorder);

            for (Integer integer : remove)
                tree.Remove(this, integer);

            lock.lock();
            export.endMovie();
            finish = true;
            this.exit();
            lock.unlock();
        });
    }

    @Override
    public void draw() {
        this.background(200);
        tree.DrawTree(this, this.width, this.height);
        lock.lock();
        if (!finish)
            export.saveFrame();
        lock.unlock();
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Main"};
        Main main = new Main();
        PApplet.runSketch(processingArgs, main);
    }
}
