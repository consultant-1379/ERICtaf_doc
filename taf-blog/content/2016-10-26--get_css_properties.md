Title: How to get css properties from TAF UI and selenium
Slug: get-css-properties-using-taf-ui
Date: 2016-10-26 16:00
Category: TAF
Tags: ui testing
Authors: Gerald Glennon
Status: published

When doing UI testing using the TAF framework, if you wanted to get css properties from the web page you will find out that it's not possible.
Only attributes set in the DOM can be accessed using the UI's api get property method. 

```java
    UiComponent button = browserTab.getGenericView("#button");
    button.getProperty("style");
```

The above will return only the DOM elements value but not its computed CSS. i.e. <button id="button" style="display:none;">help</button> in html.

To get the computed CSS of this element you will need to run a javascript command. The method evaluate() can execute javascript commands and return values.
Simply use the below command and change the .getPropertyValue(<enter css attribute>) value here to get whatever value you want.

In the example below it is getting the opacity of the UI-SDK loading widget so it can be checked to see if it has disappeared from the container.

```java
    Object opacity = getBrowserTab().evaluate("var element =  document.getElementsByClassName('eaContainer-Spinner')[0];" +
                    "return window.getComputedStyle(element, null).getPropertyValue('opacity');");
    
    String value = opacity.toString();

```

String value returned will be the opacity value. In this case the value can be anything from 0 - 100. 0 meaning its hidden and anything else meaning its on screen. This can be an integer value if you want but I will stick to string
values.
 
To turn this into a predicate for a wait condition to simply check if this spinner has been hidden from view you can write a predicate such as:
  
```java
      waitUntil(new GenericPredicate() {
          @Override
          public boolean apply() {
          Object opacity = getBrowserTab().evaluate("var element =  document.getElementsByClassName('eaContainer-Spinner')[0];" +
                          "return window.getComputedStyle(element, null).getPropertyValue('opacity');");
          
          String value = opacity.toString();
          return “0".equals(value);
    }
});

```

Whats happening here is that we have created a wait condition and defined a new predicate. This predicate will use what we have written above to check that the opacity is 0. 
Opacity 0 means the element is hidden from our screen. Once the opacity has reached 0 then the wait condition will return true and will no longer wait for that element to be hidden.

Email. gerald.glennon@ericsson.com
