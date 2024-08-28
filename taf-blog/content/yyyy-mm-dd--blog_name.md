Title: Human readable title
Slug: page-id-visible-in-blog-url
Date: 2016-09-30 16:38
Category: TAF
Tags: tag one, tag two, etc
Authors: Name Surname
Status2: please change it to 'published' once your blog was reviewed and is ready to be published

By default all blogs have draft status. E.g. this page is available at <blog URL>/drafts/page-id-visible-in-blog-url.html

Please have a look at [Markdown-Cheetsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

Please use specific types for code section, e.g. .properties:
 
```properties
dataprovider.rest.uri=/rest/
import.softwarePkg.uri=/rest/softwarePackage/import
authentication.rest.uri=/rest/auth
memberList.rest.uri=/rest/auth/members
```

Or .java:
```java
    public static String getHostName() {
        return DataHandler.getHostByName("haproxy_0").getIp().toLowerCase();
    }

    public static String getProtocol() {
        return config.getString("launcher.protocol");
    }

    public static String getLauncherUrl() {
        final String namespace = config.getString("launcher.namespace");
        return getProtocol() + "://" + getHostName() + "/#" + namespace;
    }

    public static String getFavoritesUrl() {
        return getLauncherUrl() + "/favorites";
    }

    public static String getCategoriesUrl() {
        return getLauncherUrl() + "/groups";
    }
```

Or JSON:
```json
[
  {
    "hostname": "enmApache",
    "ip": "10.43.251.3",
    "type": "httpd",
    "users": [
      {
        "username": "root",
        "password": "shroot",
        "type": "admin"
      }
    ],
    "ports": {
        "http": 8080,
        "https": 443
    }
  }
]
```

All supported types are available at 

You will have to install Pelican Markdown Extensions to generate Blog locally:
[Markdown extensions](http://pythonhosted.org/Markdown/extensions/) -> [Third-Party-Extensions](https://github.com/waylan/Python-Markdown/wiki/Third-Party-Extensions) -> [Github Markdown](http://facelessuser.github.io/pymdown-extensions/extensions/github/)

Now you can use tables:

| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |


GitHub emojis :octocat: http://www.webpagefx.com/tools/emoji-cheat-sheet/

And links become links automatically, e.g. mihails.volkovs@ericsson.com
