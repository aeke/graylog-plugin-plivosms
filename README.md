<img src="http://s32.postimg.org/54t7a8ck5/plivosms.png"/>
# Plivo SMS Plugin for Graylog

[![Build Status](https://travis-ci.org/aeke/graylog-plugin-plivosms.svg?branch=master)](https://travis-ci.org/aeke/graylog-plugin-plivosms)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000)](https://github.com/aeke/graylog-plugin-plivosms/blob/master/LICENSE)

An alarm callback plugin for integrating the <a href="https://www.plivo.com/sms-api/">Plivo SMS API</a> into <a href="https://www.graylog.org/">Graylog</a>

Required Graylog version: **1.0 and later**

Installation
------------

[Download the plugin](https://github.com/aeke/graylog-plugin-plivosms/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.


Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.

Plugin Release
--------------

We are using the maven release plugin:

```
$ mvn release:prepare
[...]
$ mvn release:perform
```

This sets the version numbers, creates a tag and pushes to GitHub. Travis CI will build the release artifacts and upload to GitHub automatically.
