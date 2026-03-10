#!/bin/bash
cd "$(dirname "$0")"

# Set Java 21
export JAVA_HOME="$HOME/.jdks/ms-21.0.10"
export PATH="$JAVA_HOME/bin:$PATH"

# Extract version from build.gradle.kts
VERSION=$(grep "^version" build.gradle.kts | sed 's/version = "\(.*\)"/\1/')

echo "Building zEssentials version $VERSION..."
./gradlew build

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo "Copying zEssentials-$VERSION.jar to server plugins folder..."
cp "target/zEssentials-$VERSION.jar" "$HOME/Desktop/Serveur dev/Serveur1.21.11/plugins/"

if [ $? -eq 0 ]; then
    echo "Done!"
else
    echo "Copy failed!"
fi
