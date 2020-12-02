package cn.edu.seu;

import cn.edu.seu.sort.*;
import processing.core.PApplet;
import com.hamoid.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends PApplet {
    VideoExport export;
    Lock lock = new ReentrantLock();

    // Advanced Sort Algorithm
    // Adaptive list = new Adaptive(240, List.ListType.RANDOM);
    // IntroSort list = new IntroSort(100, List.ListType.RANDOM);

    // For O(n²)
    // BubbleSort list = new BubbleSort(50, List.ListType.RANDOM);
    // Selection list = new Selection(50, List.ListType.RANDOM);
    // InsertionNormal list = new InsertionNormal(50, List.ListType.RANDOM);
    // InsertionCheckF list = new InsertionCheckF(50, List.ListType.RANDOM);
    ShellSort list = new ShellSort(50, List.ListType.RANDOM);

    // For O(n * log(n))
    // HeapSort list = new HeapSort(100, List.ListType.RANDOM);
    // MergeSortIPlace list = new MergeSortIPlace(100, List.ListType.RANDOM);
    // MergeSortNormal list = new MergeSortNormal(100, List.ListType.RANDOM);
    // QuickSort list = new QuickSort(100, List.ListType.RANDOM);
    // RandomizeQuickSort list = new RandomizeQuickSort(100, List.ListType.RANDOM);

    // For O(n)
    // RadixSort list = new RadixSort(100, List.ListType.RANDOM);

    @Override
    public void settings() {
        // 全屏
        this.fullScreen();
        // 800x600
        // this.size(800, 600);
    }

    @Override
    public void setup() {
        // 设置可变窗口
        // this.surface.setResizable(true);
        // 添加视频输出
        export = new VideoExport(this, list.getClass().getSimpleName() + ".mp4");
        // 设置视频帧率
        export.setFrameRate(60);
        // 设置互斥锁
        list.SetLock(lock);
        // 添加线程
        ExecutorService service = new ScheduledThreadPoolExecutor(1);
        // 提交线程
        service.submit(() -> {
            export.startMovie();
            list.sort(this);
            list.Prepare(this, () -> {
                list.Clear();
            });
            lock.lock();
            exit();
            lock.unlock();
        });
    }

    @Override
    public void draw() {
        this.background(200);
        list.Draw(this);
        lock.lock();
        export.saveFrame();
        lock.unlock();
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Main"};
        Main main = new Main();
        PApplet.runSketch(processingArgs, main);
    }
}
