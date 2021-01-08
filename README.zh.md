#SlidePuzzle

一个简单拼图布局。

[![](https://jitpack.io/v/Vindicated-Rt/SlidePuzzle.svg)](https://jitpack.io/#Vindicated-Rt/SlidePuzzle)

[English](https://github.com/Vindicated-Rt/SlidePuzzle/blob/master/README.md)

## 运行示意图

<img src="https://s3.ax1x.com/2021/01/08/sKohVS.gif" style="zoom:20%" />

## 如何使用

在工程中使用该项目

**Step 1.** **Gradle**(Module)

```css
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

**Step 2.** **Gradle**(Project)

```css
dependencies {
  implementation 'com.github.Vindicated-Rt:SlidePuzzle:0.2'
}
```

**Step 3.** 这样使用

在布局文件中添加。

```xml
<com.mystery.slidepuzzlelib.SlidePuzzleLayout
    android:id="@+id/slide_puzzle"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:puzzleColor="@color/design_default_color_error" />
```

用CheckPuzzle() 函数检测拼图。

```java
SlidePuzzleLayout slidePuzzleLayout;
slidePuzzleLayout = findViewById(R.id.slide_puzzle);

if (slidePuzzleLayout.CheckPuzzle()){
    // TODO: puzzle success check
}
```

你可以在点击事件中使用它。
