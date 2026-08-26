# 🎨 Customize Your Launcher - Beginner Guide

Learn to customize your Liquid Glass Launcher with simple changes!

---

## 🎯 Customization 1: Change Colors

### **Where**: `app/res/values/colors.xml`

**Current colors:**
```xml
<resources>
    <color name="glass_light">#FFFFFF</color>
    <color name="glass_border">#FFFFFF</color>
    <color name="gradient_start">#667eea</color>
    <color name="gradient_end">#764ba2</color>
    <color name="background">#000000</color>
</resources>
```

### **Color Options to Try:**

| Color Name | Hex Code | Example |
|-----------|----------|---------|
| Red | `#FF0000` | 🔴 |
| Green | `#00FF00` | 🟢 |
| Blue | `#0000FF` | 🔵 |
| Pink | `#FF69B4` | 🩷 |
| Purple | `#800080` | 🟣 |
| Orange | `#FFA500` | 🟠 |
| Cyan | `#00FFFF` | 🔵 |
| Yellow | `#FFFF00` | 🟡 |

### **Example: Make it Red-Orange**

```xml
<resources>
    <color name="glass_light">#FFFFFF</color>
    <color name="glass_border">#FFFFFF</color>
    <color name="gradient_start">#FF6B35</color>      <!-- Changed: Orange -->
    <color name="gradient_end">#FF0000</color>        <!-- Changed: Red -->
    <color name="background">#1a1a1a</color>          <!-- Changed: Dark Gray -->
</resources>
```

**Steps:**
1. Open `colors.xml` in Android Studio
2. Replace hex codes with ones above
3. Press `Ctrl+S` to save
4. Click Run ▶️ to see changes

---

## 📏 Customization 2: Change Grid Layout

### **Where**: `app/src/main/kotlin/.../ui/screen/LauncherScreen.kt`

**Current**: 4 apps per row

### **Change to 3 Apps Per Row:**

Find this line:
```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(4)) {
```

Change to:
```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(3)) {
```

### **Change to 5 Apps Per Row:**
```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(5)) {
```

### **Change to 6 Apps Per Row:**
```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(6)) {
```

**Steps:**
1. Open `LauncherScreen.kt`
2. Find `GridCells.Fixed(4)`
3. Change 4 to your number (3, 5, 6, etc)
4. Save and Run ▶️

---

## ✨ Customization 3: Change Glass Effect Intensity

### **Where**: `app/src/main/kotlin/.../ui/components/GlassCard.kt`

**Current blur effect:**
```kotlin
.blur(radius = 10.dp)
```

### **Make Blur Stronger (More Frosted):**
```kotlin
.blur(radius = 20.dp)    // Double the blur
```

### **Make Blur Weaker (Clearer Glass):**
```kotlin
.blur(radius = 5.dp)     // Half the blur
```

### **Make Glass More Transparent:**

Find this:
```kotlin
Color.White.copy(alpha = 0.25f)   // 0.25 = 25% opaque
```

Change to:
```kotlin
Color.White.copy(alpha = 0.15f)   // More transparent
```

**Transparency Scale:**
- `0.0` = Completely invisible
- `0.25` = 25% visible (current)
- `0.5` = 50% visible
- `1.0` = Completely visible

---

## 🔤 Customization 4: Change App Name & Title

### **Where**: `app/res/values/strings.xml`

**Current:**
```xml
<string name="app_name">LiquidLauncher</string>
```

### **Change To:**
```xml
<string name="app_name">My Glass Launcher</string>
```

Or:
```xml
<string name="app_name">iOS Style Launcher</string>
```

**Steps:**
1. Open `strings.xml`
2. Change the app_name value
3. Save and Run ▶️
4. Name appears in app drawer and launcher

---

## 🔲 Customization 5: Change Card Size & Spacing

### **Where**: `LauncherScreen.kt`

**Current spacing between cards:**
```kotlin
horizontalArrangement = Arrangement.spacedBy(8.dp),
verticalArrangement = Arrangement.spacedBy(8.dp)
```

### **Make Cards Further Apart:**
```kotlin
horizontalArrangement = Arrangement.spacedBy(16.dp),    // Double spacing
verticalArrangement = Arrangement.spacedBy(16.dp)
```

### **Make Cards Closer:**
```kotlin
horizontalArrangement = Arrangement.spacedBy(4.dp),     // Half spacing
verticalArrangement = Arrangement.spacedBy(4.dp)
```

**Current card size:**
```kotlin
GlassCard(modifier = Modifier.size(80.dp)) {
```

### **Make Cards Bigger:**
```kotlin
GlassCard(modifier = Modifier.size(100.dp)) {   // Bigger
```

### **Make Cards Smaller:**
```kotlin
GlassCard(modifier = Modifier.size(60.dp)) {    // Smaller
```

---

## 🔄 Customization 6: Change Rounded Corners

### **Where**: `GlassCard.kt`

**Current:**
```kotlin
.clip(RoundedCornerShape(24.dp))
```

### **More Rounded (Circle-like):**
```kotlin
.clip(RoundedCornerShape(32.dp))
```

### **Less Rounded (More Square):**
```kotlin
.clip(RoundedCornerShape(8.dp))
```

### **Very Square:**
```kotlin
.clip(RoundedCornerShape(0.dp))
```

---

## 📝 Customization 7: Change Border

### **Where**: `GlassCard.kt`

**Current border:**
```kotlin
.border(width = 1.5.dp, color = Color.White.copy(alpha = 0.40f))
```

### **Thicker Border:**
```kotlin
.border(width = 3.dp, color = Color.White.copy(alpha = 0.40f))
```

### **Thinner Border:**
```kotlin
.border(width = 0.5.dp, color = Color.White.copy(alpha = 0.40f))
```

### **No Border (Remove):**
```kotlin
// Just delete the .border() line
```

### **Colored Border:**
```kotlin
.border(width = 1.5.dp, color = Color.Blue.copy(alpha = 0.40f))
```

---

## 🌈 Customization 8: Change Background Gradient

### **Where**: `LauncherScreen.kt`

**Current gradient:**
```kotlin
.background(
    brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF667eea),  // Start color
            Color(0xFF764ba2)   // End color
        )
    )
)
```

### **Linear Gradient (Left to Right):**
```kotlin
.background(
    brush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2)
        )
    )
)
```

### **Solid Color (No Gradient):**
```kotlin
.background(Color(0xFF1a1a1a))
```

### **Custom Color Gradient:**
```kotlin
.background(
    brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF0000),  // Red
            Color(0xFFFFFF00)   // Yellow
        )
    )
)
```

---

## 📋 Quick Reference Card

| What | Where | How |
|------|-------|-----|
| Colors | `colors.xml` | Change hex codes |
| App Name | `strings.xml` | Edit `app_name` |
| Grid Layout | `LauncherScreen.kt` | Change `GridCells.Fixed(4)` to other number |
| Card Size | `LauncherScreen.kt` | Change `.size(80.dp)` |
| Spacing | `LauncherScreen.kt` | Change `spacedBy(8.dp)` |
| Rounded Corners | `GlassCard.kt` | Change `RoundedCornerShape(24.dp)` |
| Blur Effect | `GlassCard.kt` | Change `.blur(radius = 10.dp)` |
| Transparency | `GlassCard.kt` | Change `alpha = 0.25f` |
| Border | `GlassCard.kt` | Change `.border()` |

---

## 🧪 Test Your Changes

After each change:

1. Press `Ctrl+S` to save
2. Click the **Run ▶️** button (top toolbar)
3. Wait for app to reload (10-30 seconds)
4. See your changes on the emulator!

---

## ❌ Undo a Change

If something breaks:
1. Press `Ctrl+Z` to undo (works in text editor)
2. Or close file without saving and reopen
3. Make smaller changes next time!

---

## 💡 Pro Tips

✅ **Change ONE thing at a time** - makes it easier to spot problems
✅ **Take screenshots** of changes you like
✅ **Comment out** (add `//`) instead of deleting
✅ **Experiment!** - Try different numbers and colors
✅ **Test often** - Run the app after each change

---

## 🎯 Challenge Yourself!

Try these combinations:
1. **Dark Mode**: Black background + white text
2. **Gaming**: Red/black colors + large spacing
3. **Minimal**: Light gray + no borders
4. **Colorful**: Rainbow gradient + small cards
5. **iOS Clone**: Same as iOS 16

---

## 🆘 Something Broke?

Check **TROUBLESHOOTING.md** for common issues and fixes!

Happy customizing! 🚀
