
# RxLocationSettingsRequest
###  RxJava wrapper for Android LocationSettingsRequest used to enable GPS.

<img src="https://github.com/ShabanKamell/RxLocationSettingsRequest/blob/master/blob/master/raw/sample.png" height="600">

#### If you tried to enable GPS before, you'll find it a complex task and encounter many problems. One of these problems is ` ResolvableApiException` which you must handle in your onActivityResult(). As a result you'll spread your logic everywhere!

# The Clean Solution
##### Using RxLocationSettingsRequest you just write few lines of code and all the work will be done for you!

```java
LocationRequest locationRequest = new ...
new RxLocationSettingsRequest()  
        .request(locationRequest, MainActivity.this) // FragmentActivity
        .subscribe(isLocationRequested -> {  
            if (isLocationRequested){  
                // Location is now enabled and you can implement your logic of  
                // location safely  
              }  
         });
```

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/rxlocationsettingsrequest/images/download.svg) ](https://bintray.com/shabankamel/android/rxlocationsettingsrequest/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-location-settings-request:1.0.2@aar'
}

allprojects {
 repositories { 
  maven { url "https://dl.bintray.com/shabankamel/android" } 
 }
}
```

### See 'app' module for the full code.

# License

## Apache license 2.0
