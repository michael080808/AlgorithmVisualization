## 排序算法与二叉树平衡可视化代码

基于Processing 3进行的可视化算法操作

因为需要使用λ表达式将匿名函数作为参数传递

未使用Processing IDE进行代码编写(IDE仅支持到Java 1.6)

需要在Java 1.8及其以上环境下

手动添加Processing 3的核心库和VideoExport库

并对应VideoExport所需安装对应平台的ffmpeg程序进行录屏

### 排序算法

#### GCC使用的算法

* Adaptive MergeSort - 自适应归并排序
* IntroSort          - 内省排序

#### 对于O(n²)的排序算法

* BubbleSort         - 冒泡排序
* Selection          - 选择排序
* InsertionNormal    - 插入排序
* InsertionCheckF    - 插入排序(与首位先进行比较)
* ShellSort          - 希尔排序

#### 对于O(n * log(n))的排序算法
* HeapSort           - 堆排序
* MergeSortIPlace    - 归并排序(原地)
* MergeSortNormal    - 归并排序(经典)
* QuickSort          - 快速排序
* RandomizeQuickSort - 快速排序(随机化版本)

#### 对于O(n)的排序算法
* RadixSort          - 基数排序(二进制, 16个桶, 低位先排)

### 二叉树平衡

* DrawAVL            - AVL树
* DrawRedBlack       - 红黑树
* DrawTreeHeap       - 树堆(Treap)
* DrawSplay          - 伸展树(Splay)