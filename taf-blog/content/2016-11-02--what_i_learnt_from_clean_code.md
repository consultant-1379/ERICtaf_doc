Title: What I learnt from reading Clean Code, twice.
Slug: what-i-learnt-from-reading-clean-code-twice
Date: 2016-11-02 16:30
Category: Software Craftsmanship
Tags: software craftsmanship, ways of working, design
Authors: Thomas Melville
Status: published
Summary: I'd like to share with you what I learnt from reading Uncle Bob's clean code. I highly recommend getting a copy for yourself. It's a book you read and then keep at hand to refer to.

!["My copy of Clean Code, loaded with markers"]({filename}images/cleancode.jpg)

The main takeaway from this book for me was:

> When you get the code working, you're not actually done. Take the time and make the effort to commit well designed **and readable code.**

To quote a [blog post](http://who-t.blogspot.ie/2009/12/on-commit-messages.html) I read recently:
 
> Any software project is a collaborative project. It has at least two developers, 
> the original developer and the original developer a few weeks or months later when the train of thought has long left the station."

If you have to spend time figuring out how the code works you haven't made it readable. 
Code should nearly read like prose, the system is a book, the classes are chapters and the methods are paragraphs.

I could go into the detail of the book but I would just be repeating the book, instead I highly recommend reading it.
There are plenty of code examples to help explain the recommended practices. 

Other takeaways:

* Everything should be as small as makes sense. Small things are easy to understand and unit test.
* It takes time and effort to get into the habit of writing clean code, and then discipline to stick to it.
* Unit tests have many uses: test the code, document the behaviour of the code, learn about a 3pp by writing unit tests for it.
* Test code is just as important as production code.
* Test code has a different set of engineering standards than production code.

A great quote about comments:

> The proper use of comments is to compensate for one's **failure** to express oneself in code.

There's more but I won't spoil the book for you :-)

If I read it another time I'm sure I'll learn more. It's packed with so much information, it's hard to take it all in in one reading.
