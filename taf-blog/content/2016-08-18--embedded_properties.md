Title: Did you know you can reference other properties in property files?
Slug: embedded-properties
Date: 2016-08-18 13:40
Category: TAF
Tags: best practices, new feature
Authors: Kirill Shepitko
Status: published
Summary: Currently a lot of people are gluing properties together in Java code, because they don’t know that it can be done at the property file level.

Currently a lot of people are gluing properties together in Java code, because they don’t know that it can be done at the property file level. And the properties in file look like this:
 
```properties
dataprovider.rest.uri=/rest/
import.softwarePkg.uri=/rest/softwarePackage/import
authentication.rest.uri=/rest/auth
memberList.rest.uri=/rest/auth/members
```

And what if you change the base REST endpoint? Will you remember to update all URIs? Even if they are spread 
(or even copy-pasted) among different files?

In fact, you can reference one property inside another one. For example: 

```properties
dataprovider.rest.uri=/rest/
import.softwarePkg.uri=${dataprovider.rest.uri}softwarePackage/import
authentication.rest.uri=${dataprovider.rest.uri}/auth
memberList.rest.uri=${authentication.rest.uri}/members
```

After that, if you retrieve `import.softwarePkg.uri` from 
[TafConfiguration.getString()](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html), 
it will have value `/rest/softwarePackage/import`, and `memberList.rest.uri` would resolve to `/rest/auth/members`.

Much better, isn't it?

Please note that the referenced property can be in a different property file, that's not a problem. 
And it can reference other properties as well!

---

:warning: **IMPORTANT:** Embedded properties will be resolved only if you use something more specific than `TafConfiguration.getProperty()` - for example, `getString()` or `getInt()`.

---