import urllib.request
from pathlib import Path

base = Path(r"c:\dcj1")

# Create directories
wrapper_dir = base / "gradle" / "wrapper"
wrapper_dir.mkdir(parents=True, exist_ok=True)

# URLs for Gradle Wrapper files
urls = {
    wrapper_dir / "gradle-wrapper.properties": "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.properties",
    wrapper_dir / "gradle-wrapper.jar": "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar",
    base / "gradlew": "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradlew",
    base / "gradlew.bat": "https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradlew.bat"
}

for path, url in urls.items():
    print(f"Downloading {url} to {path}...")
    try:
        urllib.request.urlretrieve(url, path)
        print("Success.")
    except Exception as e:
        print(f"Failed to download {url}: {e}")

print("Gradle wrapper setup done.")
