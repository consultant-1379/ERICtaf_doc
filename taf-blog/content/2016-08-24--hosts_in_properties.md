Title: Referencing hosts in property files
Slug: hosts-in-properties
Date: 2016-08-24 11:49
Category: TAF
Tags: best practices, ui testing, new feature
Authors: Kirill Shepitko
Status: published
Summary: There's a much more elegant alternative to building URLs via multiple Java classes/methods.

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

Looks familiar, doesn't it? Every time you need to build a URL, you have to write stuff like this. You have to create classes just
to construct URLs. It takes time, space... and may contain bugs. And it's just looking bad.  

Since TAF **2.29.3** it's possible to reference TAF Host objects in the property files.

The naming convention is very simple: `${host:<host-name-in-your-config-file>[ port:<host-type>][ useSchema:<true|false>]}`

Usage examples: 

```properties
login.ui.url=${host:enmApache port:https useSchema:true}/login
rest.base=${host:enmApache port:http useSchema:true}/rest/
rest.addUser=${rest.base}/adduser
login.error.expected=Failed to log in to ${host:enmApache}. Please check credentials.
```

It will be resolved this way:

```properties
login.ui.url=https://10.43.251.3/login    // no HTTPS port defined, as it's the default one (443)
rest.base=http://10.43.251.3:8080/rest    // port is defined, since it differs from the default one (80)
rest.addUser=http://10.43.251.3:8080/rest/adduser
login.error.expected=Failed to log in to 10.43.251.3. Please check credentials.
```

in case of the following configuration:

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


With this in mind the code above can be transformed into a couple of property file definitions and simplified code:

```properties
launcher.url.base=${host:haproxy_0 port:https useSchema:true}
launcher.url.primary=${launcher.url.base}/#${launcher.namespace}
launcher.url.favorites=${launcher.url.primary}/favorites
launcher.url.categories=${launcher.url.primary}/groups
```

```java
public static String getFavoritesUrl() {
    return config.getString("launcher.url.favorites");
}

public static String getCategoriesUrl() {
    return config.getString("launcher.url.categories");
}
```

[More details here](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html)