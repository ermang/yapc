#!/bin/bash
set -e

APP_NAME="Yapc"
MAIN_CLASS="com.eg.yapc.App"
MAIN_JAR="target/yapc-1.0-SNAPSHOT.jar"
OUTPUT_DIR="installer"
ICON_PATH="resources/Yapc.png"

# JavaFX
JAVA_FX_LIB="/home/ermang/Downloads/javafx-sdk-21.0.9/lib"
JAVA_FX_MODULES="javafx.controls,javafx.fxml"

# JDK
JAVA_HOME="/home/ermang/Downloads/jdk-17.0.12"

# Clean previous build
echo "Cleaning previous build..."
rm -rf "$OUTPUT_DIR/$APP_NAME"

# Runtime directory inside the app image
RUNTIME_DIR="$OUTPUT_DIR/$APP_NAME/lib/runtime"

# Remove any old runtime first
rm -rf "$RUNTIME_DIR"

# Package app image
echo "Packaging app image..."
"$JAVA_HOME/bin/jpackage" \
    --type app-image \
    --name "$APP_NAME" \
    --input target/ \
    --main-jar "$(basename $MAIN_JAR)" \
    --main-class "$MAIN_CLASS" \
    --dest "$OUTPUT_DIR" \
    --module-path "$JAVA_FX_LIB" \
    --add-modules "$JAVA_FX_MODULES" \
    --verbose

cp $JAVA_FX_LIB/libprism*.so $RUNTIME_DIR/lib/
cp $JAVA_FX_LIB/libglass*.so $RUNTIME_DIR/lib/
cp $JAVA_FX_LIB/libjavafx*.so $RUNTIME_DIR/lib/


echo "✅ App image created in $OUTPUT_DIR/$APP_NAME"
echo "Run it using: $OUTPUT_DIR/$APP_NAME/bin/$APP_NAME"
