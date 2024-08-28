Title: More code style checks on javascript code to improve Quality
Slug: additional-javascript-code-style-checks
Date: 2016-10-27 11:49
Category: TAF
Tags: best practices, code quality
Authors: Gerald Glennon
Status: published

Code Quality is something that is becoming more and more popular these days. With many languages you have maven plugins, validators, SonarQube etc.

But what about javascript. For many UI-SDK developers jshint comes right out of the box as a code checking tool. But for some teams this is not enough.
We want more validation at an early stage to make sure our code is pristine and maintainable and easy to understand.

For Nodejs projects one tool that we use is called Javascript Code Style Checker (JSCS). Please check their homepage http://jscs.info/overview

This allows for extra code style checking and will fail the build if there are issues. To attach to a UI-SDK app simply:

1) Add dependencies to package.json
```json
    "devDependencies": {
    "jscs": "~1.7.3",
    "jshint": "~2.5.10",
    "glob": "~4.0.6",
    "underscore": "~1.7.0"
  }
```

2) Create rule file called .jscsrc in the same directory as your project. Then add you rule set to this file. This is an example rule set
```json
{
    "excludeFiles": [
        "*.config.js",
        ".cdt/**",
        "configs/**",
        "help/**",
        "login/**",
        "node_modules/**",
        "target/**",
        "node/**"
    ],
    "requireSpaceAfterKeywords": true,
    "requireSpaceBeforeBlockStatements": true,
    "requireParenthesesAroundIIFE": true,
    "requireSpacesInConditionalExpression": true,
    "requireSpacesInFunction": {
        "beforeOpeningRoundBrace": true,
        "beforeOpeningCurlyBrace": true
    },
    "disallowSpacesInCallExpression": true,
    "requireBlocksOnNewline": 1,
    "disallowSpacesInsideParentheses": true,
    "disallowQuotedKeysInObjects": "allButReserved",
    "disallowSpaceAfterObjectKeys": true,
    "requireSpaceBeforeObjectValues": true,
    "requireCommaBeforeLineBreak": true,
    "disallowSpaceAfterPrefixUnaryOperators": true,
    "disallowSpaceBeforePostfixUnaryOperators": true,
    "requireSpaceBeforeBinaryOperators": true,
    "requireSpaceAfterBinaryOperators": true,
    "disallowImplicitTypeConversion": [
        "numeric",
        "boolean",
        "binary",
        "string"
    ],
    "requireCamelCaseOrUpperCaseIdentifiers": true,
    "disallowKeywords": ["with"],
    "disallowMultipleLineBreaks": true,
    "disallowMixedSpacesAndTabs": true,
    "disallowTrailingWhitespace": true,
    "disallowTrailingComma": true,
    "disallowKeywordsOnNewLine": ["else"],
    "requireLineFeedAtFileEnd": true,
    "disallowNewlineBeforeBlockStatements": true,
    "validateParameterSeparator": ", "
}
```

3) Add this to your build.json so it will run with your build in UI-SDK. Add this to the phases section.
```json
"code-style-verify": {
            "depends": [
                "dev-dependencies"
            ],
            "execute": {
                "command": "$(node) node_modules/jscs/bin/jscs",
                "attributes": [
                    ".",
                    "--config=.jscsrc",
                    "--reporter=inline"
                ]
            }
        },
```

4) Finally add this phase to the analyse phase of the build in the build.json.
```json
 "analyze": {
            "depends": [
                "source-verify",
                "code-style-verify",
                "code-verify",
                "css-verify"
            ]
        },
```


For more reading have a look at the 10 best javascript coding guides http://noeticforce.com/best-javascript-style-guide-for-maintainable-code
Email: gerald.glennon@gmail.com