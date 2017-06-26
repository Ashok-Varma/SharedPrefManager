# SharedPref Manager

## What is this library about?
SharedPref Manager helps to manage your android Shared Preferences very effectively with ease

**(currently under active development, expect to see new releases almost daily)**

## Features

* Edit a SharedPreferences Item
* Add a SharedPreference Item
* Clear All SharedPreferences
* Delete a SharedPreference Item

## Download

Based on your IDE you can import library in one of the following ways

Gradle:
```groovy
debugCompile 'com.ashokvarma.android:sharedpref-manager:1.0.0'
releaseCompile 'com.ashokvarma.android:sharedpref-manager-no-op:1.0.0'
```
If you want this in library in production also then try this : 
```groovy
compile 'com.ashokvarma.android:sharedpref-manager:1.0.0'
```


or grab via Maven:
```xml
<dependency>
  <groupId>com.ashokvarma.android</groupId>
  <artifactId>sharedpref-manager</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

or Ivy:
```xml
<dependency org='com.ashokvarma.android' name='sharedpref-manager' rev='1.0.0'>
  <artifact name='$AID' ext='pom'></artifact>
</dependency>
```

or Download [the latest JAR][mavenAarDownload]


## Usage

All the Shared-Pref Names to be should be sent to SharedPrefManager. It does remaining heavy lifting.
#### Sample Code
```java
SharedPrefManager
        .launchSharedPrefManager(
                context
                , new ArrayList<>(Arrays.asList(new String[]{"SHARED_PREF_1_PRIVATE", "SHARED_PREF_2_PRIVATE"}))// All your MODE_PRIVATE shared Shared Preference names, Null if None
                , new ArrayList<>(Arrays.asList(new String[]{"SP_WORLD_READ"}))//All your MODE_WORLD_READABLE Shared Preference Names, Null if None
                , new ArrayList<>(Arrays.asList(new String[]{"SP_WORLD_WRITE"}))//All your MODE_WORLD_READABLE Shared Preference Names, Null if None
         );
```
MODE_WORLD_READABLE, MODE_WORLD_READABLE are not supported by android system in Android N(Nougat) and above devices, If sent those will be ignored in Android N and above devices.

## License

```
SharedPref Manager library for Android
Copyright (c) 2016 Ashok Varma (http://ashokvarma.me/).

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
[mavenLatestJarDownload]: https://search.maven.org/remote_content?g=com.ashokvarma.android&a=sharedpref-manager&v=LATEST
