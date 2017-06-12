# GLRippleView
Custom GLSurfaceView for Android to show ripple effect with OpenGL.

Simple | Cross-fade
---- | ----
![](art/ripple.gif) | ![](art/fade.gif)

Ripple algorithm of this library is based on [Adrian Boeing's article](http://adrianboeing.blogspot.jp/2011/02/ripple-effect-in-webgl.html).

## Getting Started
```groovy
dependencies {
  compile 'com.github.r21nomi:glrippleview:1.0.0'
}
```

## Usage
### Simple
```xml
<r21nomi.com.glrippleview.GLRippleView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:backgroundImage="@drawable/bg1" />
```
```kotlin
glRippleView.setRippleOffset(0.01f)
```

### Cross-fade
```kotlin
glRippleView.run {
    addBackgroundImages(listOf(
            BitmapFactory.decodeResource(resources, R.drawable.bg2),
            BitmapFactory.decodeResource(resources, R.drawable.bg3)
    ))
    setRippleOffset(0.01f)
    setFadeInterval(5000)
    startCrossFadeAnimation()
}
```

## License
```
Copyright 2017 Ryota Takemoto (r21nomi)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```