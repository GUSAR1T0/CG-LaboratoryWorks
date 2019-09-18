# CG-LaboratoryWorks

Computer Graphics — Laboratory Works (Java 1.8 + Gradle + libGDX)

___

## Move Polygon

_Polygon move and rotation actions by some trajectory_

> To open this scene: **Left "Ctrl" + "1"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values
2. **"S"** – show static frame of the scene
3. **"C"** – clear all points
4. **"Z"** – remove last added a point
5. **"Space"** [only _CUSTOM_ mode] – move polygon by a trajectory

### Screen

![view_move_polygon](/screens/move_polygon.png)

___

## Bezier Lines

_Quadratic Bezier lines_

> To open this scene: **Left "Ctrl" + "2"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values
2. **"C"** – clear all points
3. **"Z"** – remove last added a point

### Screen

![view_bezier](/screens/bezier.png)

___

## Reflection

_Reflection of the circle via line_

> To open this scene: **Left "Ctrl" + "3"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values
2. **"B"** – move the original circle object (red)
3. **"S"** – move the small point

### Screen

![view_reflection](/screens/reflection.png)

___

## Lines Intersection

_Lines intersection by 4 points_

> To open this scene: **Left "Ctrl" + "4"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values
2. **"R"** – choose 4 points through a random generator

### Screen

![view_lines_intersection](/screens/lines_intersection.png)

___

## Cycloid

_Cycloid move and rotation actions by a trajectory of some shape_

> To open this scene: **Left "Ctrl" + "5"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values
2. **"V"** – switch on/off a trajectory 

### Screen

![view_cycloid](/screens/cycloid.png)

___

## Cut Polygon

_Cut complex polygon as a set of some simple shapes_

> To open this scene: **Left "Ctrl" + "6"**

### Hotkeys

1. **"K"** – switch on/off all coordinate values

### Screen

![view_cut_polygon](/screens/cut_polygon.png)

___

## How to launch

1. Import the project through IntelliJ IDEA as based-on Gradle project
2. Add configuration for launch:
   1. Main class: `store.vxdesign.cg.desktop.DesktopLauncher`
   2. Working directory: `./CG-LaboratoryWorks/core/assets`
   3. Use classpath of module: `CG-LaboratoryWorks.desktop.main`
   4. JRE: `1.8`
3. Launch the project