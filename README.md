# SlidePuzzle

A simple puzzle layout.

[![](https://jitpack.io/v/Vindicated-Rt/SlidePuzzle.svg)](https://jitpack.io/#Vindicated-Rt/SlidePuzzle)

[中文版](https://github.com/Vindicated-Rt/SlidePuzzle/blob/master/README.zh.md)

## ScreenShot

<img src="https://s3.ax1x.com/2021/01/08/sKohVS.gif" style="zoom:20%" />

## How to

To get the Git project into your build:

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

**Step 3.** Use like this

Add this in a layout file.

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

Use CheckPuzzle() to check puzzle.

```java
SlidePuzzleLayout slidePuzzleLayout;
slidePuzzleLayout = findViewById(R.id.slide_puzzle);

if (slidePuzzleLayout.CheckPuzzle()){
    // TODO: puzzle success check
}
```

You can do it in OnClick().
